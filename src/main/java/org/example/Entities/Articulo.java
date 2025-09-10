package org.example.Entities;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_imagen")
    private Imagen imagen;

    private LocalDate fechaCreacion;

    private Tags tag;

}