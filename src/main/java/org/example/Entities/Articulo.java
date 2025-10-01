package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Enum.Tags;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="articulos")
public class Articulo extends Base{

    private String titulo;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String texto;

    @OneToOne(mappedBy = "articulo")
    @JsonManagedReference
    private Imagen imagen;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    private Tags tag;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
    }

}