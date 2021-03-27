package com.example.ordervalidation.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

        @PutMapping(path="update-order-status/{orderId}")
    public void updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestBody String status){
        orderService.updateOrderStatus(orderId,status);
    }
}
