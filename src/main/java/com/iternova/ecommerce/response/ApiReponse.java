package com.iternova.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiReponse {

    private String message;
    private boolean status;
}
