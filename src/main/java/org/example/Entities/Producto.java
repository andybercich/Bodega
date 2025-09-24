package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="productos")
public class Producto extends Base {

    private String codigo;

    private String nombre;

    private double precio;

    private String descripcion;

    private int stock;

    @ManyToOne
    @JoinColumn(name = "fk_categoria")
    private Categoria categoria;

    private boolean destacado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "producto_padre")
    private Producto productoPadre;

    private int cantidad;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL)
    @JsonManagedReference("producto-imagen")
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_descuento")
    private Descuento descuento;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
    }

}
