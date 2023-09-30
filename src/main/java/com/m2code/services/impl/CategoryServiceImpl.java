package com.m2code.services.impl;

import com.m2code.dtos.CategoryDto;
import com.m2code.dtos.PageableResponse;
import com.m2code.entities.Category;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.helper.Util;
import com.m2code.repositories.CategoryRepository;
import com.m2code.services.CategoryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Value("${category.upload.path}")
    private String path;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCategoryId(Util.getId());
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto findCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category is not available with id: " + categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> findAllCategory(int pageN, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Util.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }


    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category is not available with id: " + categoryId));
        Category categoryToUpdate = modelMapper.map(categoryDto, Category.class);
        categoryToUpdate.setCategoryId(category.getCategoryId());
        return modelMapper.map(categoryRepository.save(categoryToUpdate), CategoryDto.class);
    }

    @Override
    public void deletecategory(String categoryId) throws IOException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category is not available with id: " + categoryId));
        String fullImagePath = path.concat("/").concat(category.getCoverImage());
        if (new File(fullImagePath).exists()) {
            Files.delete(Path.of(fullImagePath));
        }
        categoryRepository.delete(category);
    }

    @Override
    public String uploadImage(MultipartFile file, String categoryId, String path) throws IOException {
        String fileName = Util.uploadFile(file, path);
        CategoryDto categoryDto = findCategoryById(categoryId);
        categoryDto.setCoverImage(fileName);
        categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return fileName;
    }

    @Override
    public void serveImage(String categoryId, String path, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = findCategoryById(categoryId);
        InputStream inputStream = Util.serveFile(categoryDto.getCoverImage(), path);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
