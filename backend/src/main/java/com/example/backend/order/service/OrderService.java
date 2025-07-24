package com.example.backend.order.service;

import com.example.backend.order.dto.OrderListDto;
import com.example.backend.order.entity.OrderManage;
import com.example.backend.order.repository.OrderManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderManageRepository orderManageRepository;

    /**
     * 주문 목록 조회
     */
    public List<OrderListDto> getOrderList() {
        return orderManageRepository.findAll().stream()
                .map(order -> OrderListDto.builder()
                        .seq(order.getSeq())
                        .orderNo(order.getOrderNo())
                        .orderOption(order.getOrderOption())
                        .orderDate(order.getInsertDttm()) // ✅ 주문일자 매핑
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * 주문 저장
     */
    public void saveOrder(OrderListDto orderListDto) {
        OrderManage order = new OrderManage();
        order.setOrderNo(orderListDto.getOrderNo());
        order.setOrderOption(orderListDto.getOrderOption());

        //  OrderDate를 reservDt로 저장
        if (orderListDto.getOrderDate() != null) {
            order.setReservDt(orderListDto.getOrderDate().toLocalDate());
        } else {
            // 값이 없으면 현재 날짜로 설정
            order.setReservDt(LocalDate.now());
        }

        orderManageRepository.save(order);
    }
}