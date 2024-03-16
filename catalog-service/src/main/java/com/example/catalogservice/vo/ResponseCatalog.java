package com.example.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseCatalog {
    private String productId;
    private String productName;
    private Integer stock;
    private Integer unitPrice;
    private Date createdDate;
}
