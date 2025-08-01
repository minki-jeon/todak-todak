package com.example.backend.product.service;

import com.example.backend.category.dto.CategoryDto;
import com.example.backend.category.repository.CategoryRepository;
import com.example.backend.product.dto.*;
import com.example.backend.product.entity.Product;
import com.example.backend.product.entity.ProductImage;
import com.example.backend.product.entity.ProductImageId;
import com.example.backend.product.repository.ProductImageRepository;
import com.example.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final S3Client s3Client;

    @Value("${image.prefix}")
    private String imagePrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public void add(ProductAddForm dto) {
        // TODO [@minki] 권한 체크 (관리자)

        // 조합번호 생성 (코드 + 현재일자 + 시퀀스)
        String code = "PR";

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        String date = formatter.format(now);

        Integer maxSeq = productRepository.findMaxSeq();
        int latestSeq = (maxSeq != null) ? maxSeq + 1 : 1;
        String seqStr = String.format("%07d", latestSeq);

        Product product = new Product();
        product.setProductNo(code + date + seqStr);
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setName(dto.getName());
        product.setStandard(dto.getStandard());
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setNote(dto.getNote());
        product.setState("상태값");

        productRepository.save(product);

        saveImages(product, dto.getImages());

    }

    private void saveImages(Product product, List<MultipartFile> images) {
        if (images != null && images.size() > 0) {
            for (MultipartFile image : images) {
                if (image != null && image.getSize() > 0) {
                    ProductImage entity = new ProductImage();
                    ProductImageId id = new ProductImageId();
                    id.setProductNo(product.getProductNo());
                    id.setName(image.getOriginalFilename());
                    entity.setProduct(product);
                    entity.setId(id);

                    productImageRepository.save(entity);

                    // AWS s3 파일 업로드
                    String objectKey = "prj4/productImage/" + product.getSeq() + "/" + image.getOriginalFilename();
                    uploadFile(image, objectKey);

                }
            }
        }
    }

    private void uploadFile(MultipartFile file, String objectKey) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest
                    .builder().bucket(bucketName).key(objectKey).acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 전송이 실패하였습니다.");
        }
    }

    public boolean validateForAdd(ProductAddForm dto) {

        if (dto.getCategory() == null || dto.getCategory().trim().isBlank()) {
            return false;
        }
        if (dto.getBrand() == null || dto.getBrand().trim().isBlank()) {
            return false;
        }
        if (dto.getName() == null || dto.getName().trim().isBlank()) {
            return false;
        }
        if (dto.getStandard() == null || dto.getStandard().trim().isBlank()) {
            return false;
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            return false;
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            return false;
        }
        if (dto.getNote() == null || dto.getNote().trim().isBlank()) {
            return false;
        }

        return true;
    }

    public Map<String, Object> list(String keyword, Integer pageNumber) {
        Page<ProductListDto> productListDtoPage = productRepository.searchProductList(keyword, PageRequest.of(pageNumber - 1, 10));
        int totalPages = productListDtoPage.getTotalPages();
        int rightPageNumber = ((pageNumber - 1) / 10 + 1) * 10;
        int leftPageNumber = rightPageNumber - 9;
        rightPageNumber = Math.min(rightPageNumber, totalPages);
        leftPageNumber = Math.max(leftPageNumber, 1);
        var pageInfo = Map.of("totalPages", totalPages,
                "rightPageNumber", rightPageNumber,
                "leftPageNumber", leftPageNumber,
                "currentPageNumber", pageNumber);

        return Map.of("pageInfo", pageInfo, "productList", productListDtoPage.getContent());
    }

    public ProductDto getProductBySeq(Integer seq) {
        ProductDto dto = productRepository.findProductBySeq(seq);
        Product product = new Product();
        product.setProductNo(dto.getProductNo());

        List<ProductImage> imageList = productImageRepository.findByProduct(product);
        List<ProductImageDto> images = new ArrayList<>();
        for (ProductImage image : imageList) {
            ProductImageDto imageDto = new ProductImageDto();
            imageDto.setName(image.getId().getName());
            imageDto.setPath(imagePrefix + "prj4/productImage/" + seq + "/" + image.getId().getName());
            images.add(imageDto);
        }
        dto.setImages(images);

        return dto;
    }

    public void updateDelYn(Integer seq) {
        Product dbData = productRepository.findById(seq).get();
        // del_yn = true
        dbData.setDelYn(true);
        // update_dttm = NOW()
        LocalDateTime now = LocalDateTime.now();
        dbData.setUpdateDttm(now);

        productRepository.save(dbData);
    }

    public boolean validateForUpdate(ProductUpdateForm dto) {

        if (dto.getCategory() == null || dto.getCategory().trim().isBlank()) {
            return false;
        }
        if (dto.getBrand() == null || dto.getBrand().trim().isBlank()) {
            return false;
        }
        if (dto.getName() == null || dto.getName().trim().isBlank()) {
            return false;
        }
        if (dto.getStandard() == null || dto.getStandard().trim().isBlank()) {
            return false;
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            return false;
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            return false;
        }
        if (dto.getNote() == null || dto.getNote().trim().isBlank()) {
            return false;
        }

        return true;
    }

    public void update(ProductUpdateForm dto) {
        Product dbData = productRepository.findById(dto.getSeq()).get();

        dbData.setCategory(dto.getCategory());
        dbData.setBrand(dto.getBrand());
        dbData.setName(dto.getName());
        dbData.setStandard(dto.getStandard());
        dbData.setStock(dto.getStock());
        dbData.setPrice(dto.getPrice());
        dbData.setNote(dto.getNote());

        // update_dttm = NOW()
        LocalDateTime now = LocalDateTime.now();
        dbData.setUpdateDttm(now);

        deleteFiles(dbData, dto.getDeleteImages());
        saveImages(dbData, dto.getImages());

        productRepository.save(dbData);
    }

    private void deleteFiles(Product dbData, String[] deleteImages) {
        if (deleteImages != null && deleteImages.length > 0) {
            for (String image : deleteImages) {
                // table 의 record 지우고
                ProductImageId id = new ProductImageId();
                id.setProductNo(dbData.getProductNo());
                id.setName(image);
                productImageRepository.deleteById(id);

                // s3의 파일 지우기
                String objectKey = "prj4/productImage/" + dbData.getSeq() + "/" + image;
                deleteFile(objectKey);
            }
        }
    }

    private void deleteFile(String objectKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder().bucket(bucketName).key(objectKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public List<ProductNameListDto> productListByCategory(String categoryName) {
        return productRepository.findProductByCategory(categoryName);
    }

    public ProductDto productByProductNo(String productNo) {
        Product product = productRepository.findByProductNo(productNo);
        ProductDto productDto = new ProductDto();
        productDto.setSeq(product.getSeq());
        productDto.setProductNo(product.getProductNo());
        productDto.setName(product.getName());
        productDto.setStock(product.getStock());
        productDto.setPrice(product.getPrice());

        return productDto;
    }
}
