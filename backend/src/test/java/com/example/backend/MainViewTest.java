package com.example.backend;

import com.example.backend.sale.dto.SaleListDto;
import com.example.backend.sale.repository.SaleRepository;
import com.example.backend.sale.service.SaleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * <pre>
 * packageName    : com.example.backend
 * fileName       : MainViewTest
 * author         : minki-jeon
 * date           : 2025-10-17 (금)
 * description    : 메인화면 출력 결과 검증
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-10-17 (금)        minki-jeon       최초 생성
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("메인화면")
public class MainViewTest {

    @Value("${image.prefix}")
    private String imagePrefix;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleService saleService;

    @Test
    @DisplayName("상품목록 조회")
    void viewSaleList() {

        // ## given - 테스트 준비
        // 1. SaleListDto
        SaleListDto sale1 = new SaleListDto();
        sale1.setSeq(1);
        sale1.setThumbnail("thumb1.jpg");

        SaleListDto sale2 = new SaleListDto();
        sale2.setSeq(2);
        sale2.setThumbnail("thumb2.jpg");

        List<SaleListDto> mockSaleList = List.of(sale1, sale2);

        // 2. Page
        Pageable pageable = PageRequest.of(0, 12);
        Page<SaleListDto> mockPage = new PageImpl<>(mockSaleList, pageable, 25);

        // 3. saleRepository.searchSaleList 호출 시, Page 객체 반환 설정
        when(saleRepository.searchSaleList(eq(null), eq(""), any(Pageable.class))).thenReturn(mockPage);


        // ## when - 테스트 대상 메소드 호출
        Map<String, Object> result = saleService.list(null, "", 1, 12);


        // ## then - 호출 결과 검증
        // 1. NotNull, ("pageInfo", "saleList") 포함 확인
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("pageInfo", "saleList");

        // 2. "pageInfo" 검증
        Map<String, Integer> pageInfo = (Map<String, Integer>) result.get("pageInfo");
        // 총 페이지 3개
        assertThat(pageInfo.get("totalPages")).isEqualTo(3);
        assertThat(pageInfo.get("currentPageNumber")).isEqualTo(1);
        assertThat(pageInfo.get("leftPageNumber")).isEqualTo(1);
        assertThat(pageInfo.get("rightPageNumber")).isEqualTo(3);

        // 3. "saleList"를 검증합니다.
        List<SaleListDto> saleList = (List<SaleListDto>) result.get("saleList");
        assertThat(saleList).hasSize(2); // 결과 데이터 2개
        // 썸네일 경로 확인합니다.
        assertThat(saleList.get(0).getThumbnailPath())
                .isEqualTo(imagePrefix + "prj4/saleImageThumb/1/thumb1.jpg");
        assertThat(saleList.get(1).getThumbnailPath())
                .isEqualTo(imagePrefix + "prj4/saleImageThumb/2/thumb2.jpg");
        System.out.println("SaleList 결과 데이터 : " + saleList);

        // 4. saleRepository의 searchSaleList 메소드가 정확히 1번 호출되었는지 확인합니다.
        verify(saleRepository, times(1)).searchSaleList(eq(null), eq(""), any(Pageable.class));

    }

}
