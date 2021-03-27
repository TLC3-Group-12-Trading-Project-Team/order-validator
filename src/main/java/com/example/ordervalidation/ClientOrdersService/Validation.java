package com.example.ordervalidation.ClientOrdersService;

import com.example.ordervalidation.order.Orders;
import com.example.ordervalidation.portfolio.Portfolio;

import java.time.LocalDateTime;

public class Validation {

    //checking for the client balance
    public boolean clientBalance(double incomingBalance, double actualBalance) {
        return (actualBalance != 0 && incomingBalance <= actualBalance);
    }

    //checking for the buy limit
    public boolean totalBuyLimit(int buylimit, int quantity) {
        return (buylimit != 0 && buylimit <= quantity);
    }

    //creating of the order after the validation is done
    public Orders createOrder(String open, String side, String product, double price, int quantity, Portfolio portfolio, String action) {
        Orders order = new Orders();
        order.setStatus(open);
        order.setSide(side);
        order.setProduct(product);
        order.setCreatedAt(LocalDateTime.now());
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setPortfolio(portfolio);
        order.setAction(action);

        return order;
    }
    public Boolean buyPriceChecking(double price, double bidPrice, double max_price_shift) {
        return (price < (bidPrice + max_price_shift)/2 && price > (bidPrice - max_price_shift)/2 );
    }

    public Boolean sellPriceChecking(double price, double askPrice, double max_price_shift) {
        return (price < (askPrice + max_price_shift)/2 && price > (askPrice - max_price_shift)/2 );
    }
}
