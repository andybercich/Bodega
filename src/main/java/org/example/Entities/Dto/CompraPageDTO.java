package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Entities.Compra;

import java.util.List;

@Data
@AllArgsConstructor
public class CompraPageDTO {
    private List<Compra> content;
    private int page;
    private int size;
    private int totalPages;
}