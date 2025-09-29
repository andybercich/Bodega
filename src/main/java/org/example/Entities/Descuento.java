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
@Table(name = "descuentos")
public class Descuento extends Base {

    private double valor;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name= "fecha_fin")
    private LocalDate fechaFin;

    @OneToMany(mappedBy = "descuento", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Producto> productos = new ArrayList<>();

    public boolean isValid() {
        LocalDate hoy = LocalDate.now();
        return (hoy.isEqual(fechaInicio) || hoy.isAfter(fechaInicio)) &&
                (hoy.isEqual(fechaFin) || hoy.isBefore(fechaFin));
    }

}
