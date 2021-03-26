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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;


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
        double bidPrice = marketData_1.getBID_PRICE() + marketData_2.getBID_PRICE();
        double max_shift_shift = marketData_1.getMAX_PRICE_SHIFT() + marketData_2.getMAX_PRICE_SHIFT();
        double askPrice = marketData_1.getASK_PRICE() + marketData_2.getASK_PRICE();
        System.out.println(marketData_1 + " " + marketData_2);


        if (request.getSide().equalsIgnoreCase("BUY")) {
            if (marketData_1 != null && marketData_2 != null) {
                if (validation.clientBalance((request.getPrice() * request.getQuantity()), balance)) {
                    if (validation.totalBuyLimit((request.getQuantity()), buy_limit)) {
                        if (validation.buyPriceChecking(request.getPrice(), bidPrice, max_shift_shift)) {
                            Orders orders = validation.createOrder("OPEN", request.getSide(), request.getProduct(), request.getPrice(), request.getQuantity(), portfolioService.getPortfolio((long) request.getPortfolioId()),request.getAction());

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
                            response.setMessage("Recheck your price");
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
        }
        if (request.getSide().equalsIgnoreCase("SELL")) {
            if (marketData_1 != null && marketData_2 != null) {
                if (request.getQuantity() < (marketData_1.getSELL_LIMIT() + marketData_2.getSELL_LIMIT())) {
                    if (validation.sellPriceChecking(request.getPrice(), askPrice, max_shift_shift)) {
                        Orders orders = validation.createOrder("OPEN", request.getSide(), request.getProduct(), request.getPrice(), request.getQuantity(), portfolioService.getPortfolio((long) request.getPortfolioId()), request.getAction());

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
                        response.setMessage("Recheck your price");
                    }
                } else {
                    response.setIsOrderValid(false);
                    response.setMessage("Recheck your quantity");
                }

            } else {
                response.setIsOrderValid(false);
                response.setMessage("market data is not available");
            }
        }

        return response;
    }

}