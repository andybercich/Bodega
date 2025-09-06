package org.example.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.Entities.Enum.TipoCodigo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "codigo_descuento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CodigoDescuento extends Base {

    private String codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private double tope;

    private int limiteUsado;

    private double porcentajeDescuento;

    @Enumerated(EnumType.STRING)
    private TipoCodigo tipoCodigo;

    @ManyToOne
    private Usuario usuarioAsignado;

    @ManyToMany
    @JoinTable(name = "codigo_descuento_usos",
            joinColumns = @JoinColumn(name = "codigo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuariosQueUsaron = new ArrayList<>();

    public boolean isValid() {
        LocalDate hoy = LocalDate.now();
        return (hoy.isEqual(fechaInicio) || hoy.isAfter(fechaInicio)) &&
                (hoy.isEqual(fechaFin) || hoy.isBefore(fechaFin));
    }


}
