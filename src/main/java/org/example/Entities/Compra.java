package org.example.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.Entities.Enum.Estado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Compra extends Base{
    private String codigoSeguimiento;

    @ManyToOne
    private Direccion direccionEnvio;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private LocalDateTime fechaCompra;

    private boolean direccionUsuario;

    @ManyToOne
    private CodigoDescuento codigoDescuento;

    private BigDecimal total;

    @ManyToOne
    private Usuario usuario;

}
