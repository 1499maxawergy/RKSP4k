package ru.maxawergy.cartservice.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", url = "http://order.default:8082")
public interface OrderServiceFeignClient {

    @GetMapping("/order")
    ResponseEntity<String> getOrderInfo(@RequestParam(value = "token") String token);
}
