package com.m2code.controller;

import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.CategoryDto;
import com.m2code.dtos.PageableResponse;
import com.m2code.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {
    private final CategoryService categoryService;

    @Value("${category.upload.path}")
    private String path;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "create category - admin operation")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update category - admin operation")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto updateCategory = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updateCategory);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "get category by id")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable String categoryId) {
        CategoryDto updateCategory = categoryService.findCategoryById(categoryId);
        return ResponseEntity.ok(updateCategory);
    }

    @GetMapping
    @Operation(summary = "get all category")
    public ResponseEntity<PageableResponse<CategoryDto>> findAllCategory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir
    ) {
        PageableResponse<CategoryDto> allCategory = categoryService.findAllCategory(page, size, sortBy, sortDir);
        return ResponseEntity.ok(allCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete category - admin operation")
    public ResponseEntity<ApiResponseMessage<String>> deleteCategory(@PathVariable String categoryId) throws IOException {
        categoryService.deletecategory(categoryId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Category with id : " + categoryId + " deleted")
                .success(true).build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @PutMapping("/image/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "upload category image- admin operation")
    public ResponseEntity<ApiResponseMessage<String>> uploadCategoryImage(@RequestParam("image") MultipartFile file,
                                                                          @PathVariable String categoryId) throws IOException {
        String fileName = categoryService.uploadImage(file, categoryId, path);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder().message("Cover image uploaded with file name : " + fileName).build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @GetMapping("/image/{categoryId}")
    @Operation(summary = "serve category image")
    public void serveImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        categoryService.serveImage(categoryId, path, response);
    }

}
