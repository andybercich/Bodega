package org.example.Entities.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageGenericDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalPages;


}