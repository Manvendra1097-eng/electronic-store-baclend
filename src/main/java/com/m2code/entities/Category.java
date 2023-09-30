package com.m2code.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;
    @Column(name = "category_title", nullable = false)
    private String title;
    @Column(name = "category_desc", length = 1000)
    private String description;
    private String coverImage;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;
}
