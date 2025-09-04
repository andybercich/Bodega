package org.example.Entities.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Producto;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
public class ProductoPageDTO extends PageGenericDTO<ProductoDTO>{

    public ProductoPageDTO(List<Producto> listProduct, int page, int size, int totalPages) {
        super(
                listProduct.stream()
                        .map(ProductoDTO::fromEntity)
                        .collect(Collectors.toList()),
                page,
                size,
                totalPages
        );
    }

}
