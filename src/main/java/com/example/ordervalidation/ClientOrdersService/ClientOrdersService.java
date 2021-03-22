package com.example.ordervalidation.ClientOrdersService;

import com.example.ordervalidation.clientorders.OrderRequest;
import com.example.ordervalidation.clientorders.OrderResponse;
import com.example.ordervalidation.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class ClientOrdersService {

    @Autowired
    private  final OrderService orderService;

    public ClientOrdersService(OrderService orderService){
        this.orderService = orderService;
    }

    public OrderResponse checkOrderValidity(OrderRequest request){
        OrderResponse response = new OrderResponse();

        if(request.getSide().equals("BUY")){
            if((request.getPrice()* request.getQuantity())>0){
                System.out.println(request.getPrice()* request.getQuantity());
                response.setIsOrderValid(true);
                response.setMessage("Client order is valid");
            }else{
                response.setIsOrderValid(false);
                response.setMessage("Client balance insufficient for the order");
            }
        }

        return response;
    }

}