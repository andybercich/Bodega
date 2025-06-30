package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="articulos")
public class Articulo extends Base{

    private String titulo;

    private String texto;

    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    @JsonManagedReference("articulo-imagen")
    private List<Imagen> fotos = new ArrayList<>();


}
