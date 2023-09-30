package com.m2code.services;

import com.m2code.dtos.CategoryDto;
import com.m2code.dtos.PageableResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto findCategoryById(String categoryId);

    PageableResponse<CategoryDto> findAllCategory(int page, int size, String sortBy, String sortDir);

    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    void deletecategory(String categoryId) throws IOException;

    String uploadImage(MultipartFile file, String categoryId, String path) throws IOException;

    void serveImage(String categoryId, String path, HttpServletResponse response) throws IOException;
}
