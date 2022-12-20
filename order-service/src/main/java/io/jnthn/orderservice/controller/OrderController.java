package io.jnthn.orderservice.controller;

import io.jnthn.orderservice.dto.OrderRequest;
import io.jnthn.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOder(orderRequest);
        return "Order Placed Successfully";
    }
}
