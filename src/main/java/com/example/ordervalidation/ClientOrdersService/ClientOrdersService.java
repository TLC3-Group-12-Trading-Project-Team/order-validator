package com.example.ordervalidation.ClientOrdersService;

import com.example.ordervalidation.clientorders.OrderRequest;
import com.example.ordervalidation.clientorders.OrderResponse;
import com.example.ordervalidation.marketdata.ExchangeData;
import com.example.ordervalidation.order.OrderService;
import com.example.ordervalidation.order.Orders;
import com.example.ordervalidation.portfolio.PortfolioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;


@Service
public class ClientOrdersService {

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final PortfolioService portfolioService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    Environment env;

    @Autowired
    public ClientOrdersService(OrderService orderService, PortfolioService portfolioService) {
        this.orderService = orderService;
        this.portfolioService = portfolioService;
    }

    public OrderResponse checkOrderValidity(OrderRequest request) throws JsonProcessingException {
        OrderResponse response = new OrderResponse();
        Validation validation = new Validation();

        Long clientId = this.portfolioService.getClientId((long) request.getPortfolioId());
        String URL = "http://localhost:25000/client/balance/".concat(String.valueOf(clientId));
        Double balance = restTemplate.getForObject(URL, Double.class);

        ExchangeData marketData_1 = objectMapper
                .readValue(restTemplate.getForObject("https://exchange.matraining.com/md/".concat(request.getProduct()), String.class),
                        ExchangeData.class);

        ExchangeData marketData_2 = objectMapper
                .readValue(restTemplate.getForObject("https://exchange2.matraining.com/md/".concat(request.getProduct()), String.class),
                        ExchangeData.class);
        int buy_limit = marketData_1.getBUY_LIMIT() + marketData_2.getBUY_LIMIT();
        System.out.println(marketData_1 + " " + marketData_2);


        if (request.getSide().equalsIgnoreCase("BUY")) {
            if (marketData_1 != null && marketData_2 != null) {
                if (validation.clientBalance((request.getPrice() * request.getQuantity()), balance)) {
                    if (validation.totalBuyLimit((request.getQuantity()), buy_limit)) {
                        Orders orders = validation.createOrder("OPEN", request.getSide(), request.getProduct(), request.getPrice(), request.getQuantity(), portfolioService.getPortfolio((long) request.getPortfolioId()));

                        orderService.createOrders(orders);
                        response.setIsOrderValid(true);
                        response.setMessage("Client order is valid");
                        try {
                            Jedis client = new Jedis("localhost", 6379);
                            client.publish("orderValidation", objectMapper.writeValueAsString(orders));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        response.setIsOrderValid(false);
                        response.setMessage("The quantity is not enough to buy");
                    }
                } else {
                    response.setIsOrderValid(false);
                    response.setMessage("Client balance insufficient for the order");
                }
            } else {
                response.setIsOrderValid(false);
                response.setMessage("market data is not available");
            }
        } else if (request.getSide().equalsIgnoreCase("SELL")) {

        }

        return response;
    }

}