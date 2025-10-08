package org.example.Entities.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemRequest {

    private String codigo;
    private String title;
    private int quantity;
    private BigDecimal unitPrice;
    private String picture_url;
}
