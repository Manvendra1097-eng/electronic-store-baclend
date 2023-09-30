package com.m2code.controller;

import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.ProductDto;
import com.m2code.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "create product- admin operation")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PostMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "create product with category- admin operation")
    public ResponseEntity<ProductDto> createProductWithCategory(@Valid @RequestBody ProductDto productDto, @PathVariable String categoryId) {
        ProductDto product = productService.createProductWithCategory(productDto, categoryId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("{productId}/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "set category for product- admin operation")
    public ResponseEntity<ProductDto> setCategoryForProduct(@PathVariable String productId, @PathVariable String categoryId) {
        ProductDto product = productService.setCategoryForProduct(productId, categoryId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update product- admin operation")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto product = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get product by id")
    public ResponseEntity<ProductDto> findProductById(@PathVariable String productId) {
        ProductDto product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "get all product")
    public ResponseEntity<PageableResponse<ProductDto>> findAllProduct(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, @RequestParam(required = false, defaultValue = "title") String sortBy, @RequestParam(required = false, defaultValue = "ASC") String sortDir) {
        PageableResponse<ProductDto> pageableResponse = productService.findAllProduct(page, size, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/live")
    @Operation(summary = "get all live product")
    public ResponseEntity<PageableResponse<ProductDto>> findAllProductLive(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, @RequestParam(required = false, defaultValue = "title") String sortBy, @RequestParam(required = false, defaultValue = "ASC") String sortDir) {
        PageableResponse<ProductDto> pageableResponse = productService.findAllProductLive(page, size, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "get product by category")
    public ResponseEntity<PageableResponse<ProductDto>> findAllProductByCategory(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, @RequestParam(required = false, defaultValue = "title") String sortBy, @RequestParam(required = false, defaultValue = "ASC") String sortDir, @PathVariable String categoryId) {
        PageableResponse<ProductDto> pageableResponse = productService.findAllProductByCategory(page, size, sortBy, sortDir, categoryId);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary = "search product")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size, @RequestParam(required = false, defaultValue = "title") String sortBy, @RequestParam(required = false, defaultValue = "ASC") String sortDir, @PathVariable String keyword) {
        PageableResponse<ProductDto> pageableResponse = productService.searchProduct(page, size, sortBy, sortDir, keyword);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete product- admin operation")
    public ResponseEntity<ApiResponseMessage<String>> deleteProduct(@PathVariable String productId) throws IOException {
        productService.deleteProduct(productId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Product deleted with id : " + productId).build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @PutMapping("image/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "upload product image- admin operation")
    public ResponseEntity<ApiResponseMessage<String>> uploadImage(@RequestParam("image") MultipartFile file, @PathVariable String productId) throws IOException {
        productService.uploadImage(file, productId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Product image uploaded").build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @GetMapping("image/{productId}")
    @Operation(summary = "searve product image")
    public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        productService.serveImage(productId, response);
    }

}
