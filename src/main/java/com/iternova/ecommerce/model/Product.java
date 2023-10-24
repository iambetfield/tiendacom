package com.iternova.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private int price;
    @Column(name = "discounted_percent")
    private int discountPercent;
    @Column(name = "discounted_price")
    private int discountedPrice;

    private int quantity;

    private String brand;

    private String color;


    @ElementCollection
    @Column(name = "sizes")
    private Set<Size> sizes = new HashSet<Size>();
    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL,orphanRemoval = true)
    private List<Rating> ratings= new ArrayList();
    @OneToMany(mappedBy = "product", cascade =  CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews = new ArrayList();
    @Column(name = "num_ratings")
    private int numRatings;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime createdAt;
}
