package com.m2code.services.impl;

import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.ProductDto;
import com.m2code.entities.Category;
import com.m2code.entities.Product;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.helper.Util;
import com.m2code.repositories.CategoryRepository;
import com.m2code.repositories.ProductRepository;
import com.m2code.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Value("${product.upload.path}")
    private String path;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(Util.getId());
        product.setAddedAt(new Date(System.currentTimeMillis()));
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(Util.getId());
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available for id : " + categoryId));
        product.setCategory(category);
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto setCategoryForProduct(String productId, String categoryId) {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available for id : " + categoryId));
        dbProduct.setCategory(category);
        return modelMapper.map(productRepository.save(dbProduct), ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(dbProduct.getProductId());
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    @Override
    public ProductDto findProductById(String productId) {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));
        return modelMapper.map(dbProduct, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> findAllProduct(int pageN, int size, String sortBy, String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Util.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> findAllProductLive(int pageN, int size, String sortBy, String sortDir) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Util.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> findAllProductByCategory(int pageN, int size, String sortBy, String sortDir, String categoryId) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category is not available for id : " + categoryId));
        Page<Product> page = productRepository.findByCategory(category, pageable);
        PageableResponse<ProductDto> pageableResponse = Util.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> searchProduct(int pageN, int size, String sortBy, String sortDir, String keyword) {
        Sort sort = getSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<Product> page = productRepository.findByTitleContaining(keyword, pageable);
        PageableResponse<ProductDto> pageableResponse = Util.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public void deleteProduct(String productId) throws IOException {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));

        if (StringUtils.isEmpty(dbProduct.getProductImage())) {
            productRepository.delete(dbProduct);
            return;
        }
        String fullImagePath = path.concat("/").concat(dbProduct.getProductImage());
        if (new File(fullImagePath).exists()) {
            Files.delete(Path.of(fullImagePath));
        }
        productRepository.delete(dbProduct);
    }

    @Override
    public void uploadImage(MultipartFile file, String productId) throws IOException {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));
        String fileName = Util.uploadFile(file, path);
        dbProduct.setProductImage(fileName);
        productRepository.save(dbProduct);
    }

    @Override
    public void serveImage(String productId, HttpServletResponse response) throws IOException {
        Product dbProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not available for id : " + productId));
        InputStream inputStream = Util.serveFile(dbProduct.getProductImage(), path);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

    private static Sort getSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
}
