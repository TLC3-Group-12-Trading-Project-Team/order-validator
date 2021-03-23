package com.example.ordervalidation.ClientOrdersService;

import com.example.ordervalidation.clientorders.OrderRequest;
import com.example.ordervalidation.clientorders.OrderResponse;
import com.example.ordervalidation.order.OrderService;
import com.example.ordervalidation.order.Orders;
import com.example.ordervalidation.portfolio.PortfolioService;
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
    private  final OrderService orderService;

    @Autowired
    private  final PortfolioService portfolioService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    Environment env;

    @Autowired
    public ClientOrdersService(OrderService orderService, PortfolioService portfolioService){
        this.orderService = orderService;
        this.portfolioService = portfolioService;
    }

    public OrderResponse checkOrderValidity(OrderRequest request) {
        OrderResponse response = new OrderResponse();

        Long  clientId = this.portfolioService.getClientId((long) request.getPortfolioId());
        String URL = "http://localhost:25000/client/balance/".concat(String.valueOf(clientId));
        Double balance = restTemplate.getForObject(URL,Double.class);

        if(request.getSide().equals("BUY")){
            if((request.getPrice()* request.getQuantity())<=balance){
                Orders orders = new Orders();
                orders.setStatus("OPEN");
                orders.setSide(request.getSide());
                orders.setProduct(request.getProduct());
                orders.setCreatedAt(LocalDateTime.now());
                orders.setPrice(request.getPrice());
                orders.setQuantity(request.getQuantity());
                orders.setPortfolio(portfolioService.getPortfolio((long) request.getPortfolioId()));
                orderService.createOrders(orders);
                response.setIsOrderValid(true);
                response.setMessage("Client order is valid");
                try{
                    Jedis client = new Jedis("localhost", 6379);
                    client.publish("orderValidation", objectMapper.writeValueAsString(orders));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                response.setIsOrderValid(false);
                response.setMessage("Client balance insufficient for the order");
            }
        }

        return response;
    }

}