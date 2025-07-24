package com.example.backend.order.controller;

import com.example.backend.order.dto.OrderListDto;
import com.example.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<List<OrderListDto>> getOrderList() {
        List<OrderListDto> orderList = orderService.getOrderList();
        return ResponseEntity.ok(orderList);
    }

    /**
     * 주문 저장 API
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveOrder(@RequestBody OrderListDto orderListDto) {
        orderService.saveOrder(orderListDto);
        return ResponseEntity.ok().build();
    }
}