package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "El código de descuento no puede estar vacío")
    @Size(min = 4, max = 15, message = "El código de descuento debe tener entre 4 y 15 caracteres")
    @Column(unique = true)
    private String codigo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @PositiveOrZero(message = "El tope debe ser mayor o igual a 0")
    @NotNull(message = "El codigo de descuento necesita un tope de total descontado")
    private double tope;

    @PositiveOrZero(message = "El límite usado no puede ser negativo")
    @NotNull(message = "El codigo de descuento necesita un tope de limites para descontar")
    private int limiteUsado;

    @DecimalMin(value = "0.0", inclusive = false, message = "El porcentaje de descuento debe ser mayor a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje de descuento no puede superar el 100%")
    @NotNull(message = "El codigo de descuento necesita un porcentaje de descuento")
    private double porcentajeDescuento;

    @NotNull(message = "El tipo de código es obligatorio")
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
