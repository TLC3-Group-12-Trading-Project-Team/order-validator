package com.example.ordervalidation.ClientOrdersService;

import com.example.ordervalidation.order.Orders;
import com.example.ordervalidation.portfolio.Portfolio;

import java.time.LocalDateTime;

public class Validation {

    public boolean clientBalance(double incomingBalance, double actualBalance) {
        return (actualBalance != 0 && incomingBalance <= actualBalance);
    }

    public boolean totalBuyLimit(int buylimit, int quantity) {
        return (buylimit != 0 && buylimit <= quantity);
    }

    public Orders createOrder(String open, String side, String product, double price, int quantity, Portfolio portfolio) {
        Orders order = new Orders();
        order.setStatus(open);
        order.setSide(side);
        order.setProduct(product);
        order.setCreatedAt(LocalDateTime.now());
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setPortfolio(portfolio);

        return order;
    }
}
