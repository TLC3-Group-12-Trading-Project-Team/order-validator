package com.example.ordervalidation.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public void createOrders(Orders orders){
        this.orderRepository.save(orders);
    }

    // Update Order Status
    public void updateOrderStatus(Long id, String status){
        Orders order = orderRepository.findById(id).orElse(null);
        if(order!=null){
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}
