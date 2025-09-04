package org.example.Entities.Dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Categoria;
import org.example.Entities.Descuento;
import org.example.Entities.Imagen;
import org.example.Entities.Producto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductoDTO {


    private String codigo;

    private String nombre;

    private double precio;

    private String descripcion;

    private int stock;

    private Categoria categoria;

    private boolean destacado;

    private LocalDate fechaCreacion;

    private ProductoDTO productoPadre;

    private int cantidad;

    private List<ImagenDTO> imagenes = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_descuento")
    private DescuentoDTO descuento;

    public static ProductoDTO fromEntity(Producto producto){
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());
        dto.setDestacado(producto.isDestacado());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setCantidad(producto.getCantidad());

        if (producto.getProductoPadre() != null) {
            dto.setProductoPadre(ProductoDTO.fromEntity(producto.getProductoPadre()));
        }

        if (producto.getDescuento() != null) {
            dto.setDescuento(DescuentoDTO.fromEntity(producto.getDescuento()));
        }

        if (producto.getImagenes() != null) {
            dto.setImagenes(producto.getImagenes().stream()
                    .map(ImagenDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
