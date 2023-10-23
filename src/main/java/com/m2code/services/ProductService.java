package com.m2code.services;

import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.ProductDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto createProductWithCategory(ProductDto productDto, String categoryId);

    ProductDto setCategoryForProduct(String productId, String categoryId);

    ProductDto updateProduct(ProductDto productDto, String productId);

    ProductDto findProductById(String productId);

    PageableResponse<ProductDto> findAllProduct(int page, int size, String sortBy, String sortDir);

    PageableResponse<ProductDto> findAllProductLive(int page, int size, String sortBy, String sortDir);

    PageableResponse<ProductDto> findAllProductByCategory(int page, int size, String sortBy, String sortDir, String categoryId);

    PageableResponse<ProductDto> searchProduct(int page, int size, String sortBy, String sortDir, String keyword);

    public PageableResponse<ProductDto> searchProductByTileAndCategory(int pageN, int size, String sortBy,
                                                                       String sortDir, String keyword);

    void deleteProduct(String productId) throws IOException;

    void uploadImage(MultipartFile file, String productId) throws IOException;

    void serveImage(String productId, HttpServletResponse response) throws IOException;
}
