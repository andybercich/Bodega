package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imagen extends Base{

    private String url;

    private String alt;

    @ManyToOne
    @JoinColumn(name = "fk_articulo")
    @JsonBackReference("articulo-imagen")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "fk_producto")
    @JsonBackReference("producto-imagen")
    private Producto producto;

}
