package com.example.ordervalidation.order;

import com.example.ordervalidation.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders/{orderId}")
    public Optional<Orders> getOrder(@PathVariable(value = "orderId") Long orderId){
        return orderRepository.findById(orderId);
    }

    @PutMapping("/orders/{orderId}")
    public Orders updateOrder(@PathVariable Long orderId,@RequestBody Orders orderRequest){
        return orderRepository.findById(orderId).map(orders -> {
            orders.setStatus(orderRequest.getStatus());
            return orderRepository.save(orders);
        }).orElseThrow(() -> new ResourceNotFoundException("orderId " + orderId + " not found"));
    }

}
