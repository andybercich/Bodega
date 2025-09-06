package org.example.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "DetalleCompra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DetalleCompra extends Base{

    private int cantidad;

    private BigDecimal precioUnitario;

    @ManyToOne(optional = false, cascade = { CascadeType.MERGE})
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "Ingresa un producto válido")
    private Producto producto;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "compra_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Compra compra;

    @Override
    public String toString() {
        return "Compra{id=" + getId() + ", prod=" + producto + ", precio unit=" + precioUnitario + "}";
    }


}
