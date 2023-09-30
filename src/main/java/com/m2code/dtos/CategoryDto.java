package com.m2code.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotEmpty(message = "Category required title")
    private String title;
    @NotEmpty(message = "Category description is required")
    private String description;
    private String coverImage;
}
