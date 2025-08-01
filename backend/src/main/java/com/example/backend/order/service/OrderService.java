package com.example.backend.order.service;

import com.example.backend.order.dto.OrderDetailDto;
import com.example.backend.order.dto.OrderManageDto;
import com.example.backend.order.entity.OrderItem;
import com.example.backend.order.entity.OrderManage;
import com.example.backend.order.repository.OrderItemRepository;
import com.example.backend.order.repository.OrderManageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service // ✅ 비즈니스 로직을 처리하는 서비스 계층 클래스
@RequiredArgsConstructor // ✅ 생성자 주입을 Lombok이 자동 생성
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 (조회용 서비스)
public class OrderService {

    private final OrderManageRepository orderManageRepository; // 주문 정보 저장소
    private final OrderItemRepository orderItemRepository;     // 주문상품 저장소

    /**
     * 📌 주문 목록 조회
     * - 회원별로 주문 내역을 조건 필터링하여 조회
     * - 필터 조건: 상태, 날짜범위, 상품명 키워드
     *
     * @param memberSeq 회원 식별자
     * @param status 주문 상태 필터 (선택)
     * @param keyword 상품명 키워드 (선택)
     * @param startDate 시작일 필터 (선택)
     * @param endDate 종료일 필터 (선택)
     * @return 주문 목록 DTO 리스트
     */
    public List<OrderManageDto> findOrders(
            Integer memberSeq,
            String status,
            String keyword,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 🔍 해당 회원의 전체 주문 조회
        List<OrderManage> orderManages = orderManageRepository.findByMember_Seq(memberSeq);
        List<OrderManageDto> result = new ArrayList<>();

        for (OrderManage order : orderManages) {
            boolean matches = true;

            // ✅ 주문 상태 필터
            if (status != null && !status.isBlank()) {
                if (!status.equals(order.getStatus())) {
                    matches = false;
                }
            }

            // ✅ 시작일 필터
            if (startDate != null) {
                if (order.getOrderDate().toLocalDate().isBefore(startDate)) {
                    matches = false;
                }
            }

            // ✅ 종료일 필터
            if (endDate != null) {
                if (order.getOrderDate().toLocalDate().isAfter(endDate)) {
                    matches = false;
                }
            }

            // ✅ 키워드 필터 (상품명 포함 여부)
            if (keyword != null && !keyword.isBlank()) {
                boolean found = false;
                for (OrderItem item : order.getItems()) {
                    if (item.getProduct() != null && item.getProduct().getName() != null) {
                        if (item.getProduct().getName().contains(keyword)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    matches = false;
                }
            }

            // ✅ 조건에 맞는 주문만 DTO 변환
            if (matches) {
                List<String> names = new ArrayList<>();
                for (OrderItem item : order.getItems()) {
                    if (item.getProduct() != null && item.getProduct().getName() != null) {
                        names.add(item.getProduct().getName());
                    }
                }

                String productNames = String.join(", ", names); // 상품명들 → 문자열

                OrderManageDto dto = new OrderManageDto(
                        order.getSeq(),
                        order.getOrderNo(),
                        order.getOrderDate(),
                        productNames,
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getTrackNo(),
                        order.getDelYn()
                );

                result.add(dto);
            }
        }

        return result;
    }

    /**
     * 📌 주문 상세 조회
     * - 주문 번호를 기반으로 주문 및 상품 정보 전체 조회
     *
     * @param orderSeq 주문 번호 (PK)
     * @return 주문 상세 DTO
     */
    public OrderDetailDto getOrderDetail(Integer orderSeq) {
        // 🔍 주문 조회
        OrderManage order = orderManageRepository.findById(orderSeq).orElse(null);

        // 🔍 주문 상품 목록 조회
        List<OrderItem> items = orderItemRepository.findByOrderManage(order);

        // ✅ DTO에 담을 정보 수집
        List<String> productNames = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();

        for (OrderItem item : items) {
            productNames.add(item.getProduct().getName());
            quantities.add(item.getQuantity());
            prices.add(item.getProduct().getPrice());
        }

        // ✅ 상세 DTO 생성 및 반환
        return new OrderDetailDto(
                order.getSeq(),
                order.getOrderNo(),
                order.getOrderDate(),
                productNames,
                quantities,
                prices,
                order.getTotalPrice(),
                order.getStatus(),
                order.getTrackNo()
        );
    }
}