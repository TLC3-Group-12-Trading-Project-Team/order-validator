package com.example.ordervalidation.endpoint;

import com.example.ordervalidation.ClientOrdersService.ClientOrdersService;
import com.example.ordervalidation.clientorders.OrderRequest;
import com.example.ordervalidation.clientorders.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ClientOrdersEndpoint {
    private static final String NAMESPACE = "http://www.example.com/orderValidation/clientorders";

    @Autowired
    private ClientOrdersService service;

    @PayloadRoot(namespace = NAMESPACE, localPart = "OrderRequest")
    @ResponsePayload
    public OrderResponse getOrderValidility(@RequestPayload OrderRequest request){
        return service.checkOrderValidity(request);
    }
}
