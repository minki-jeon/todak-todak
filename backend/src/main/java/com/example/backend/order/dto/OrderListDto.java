package com.example.backend.order.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderListDto {
    private Integer seq;                // OrderManage의 PK
    private String orderNo;          // 주문번호
    private String orderOption;      // 옵션
    private LocalDateTime orderDate; // 주문 일자
}