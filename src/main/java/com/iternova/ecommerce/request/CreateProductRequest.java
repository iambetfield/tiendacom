package com.iternova.ecommerce.request;

import com.iternova.ecommerce.model.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private Set<Size> size = new HashSet<>();

    private String imageUrl;
    private String brand;
    private String title;
    private String color;
    private int discountedPrice;
    private int price;
    private int discountPercent;
    private String description;

    private int quantity;
    private String topLabelCategory;
    private String secondLabelCategory;
    private String thirdLabelCategory;
}
