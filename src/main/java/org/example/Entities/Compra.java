package org.example.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.Entities.Enum.EstadoCompra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private EstadoCompra estadoCompra;

    @Column(name = "fecha_compra", updatable = false)
    private LocalDateTime fechaCompra;

    private boolean direccionUsuario;

    @ManyToOne
    private CodigoDescuento codigoDescuento;

    private BigDecimal total;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<DetalleCompra> detalles = new ArrayList<>();

    @Override
    public String toString() {
        return "Compra{id=" + getId() + ", total=" + total + ", fechaCompra=" + fechaCompra + "}";
    }


    public void calcularTotal() throws Exception {
        try{
            System.out.println("Esoty acaaa");
            BigDecimal totalBruto = detalles.stream()
                    .map(d -> {
                        Producto producto = d.getProducto();
                        BigDecimal precioOriginal = BigDecimal.valueOf(producto.getPrecio());
                        BigDecimal precioFinal = precioOriginal;

                        Descuento descuento = producto.getDescuento();
                        if (descuento != null) {
                            LocalDate hoy = LocalDate.now();
                            if ((descuento.getFechaInicio() == null || !hoy.isBefore(descuento.getFechaInicio())) &&
                                    (descuento.getFechaFin() == null || !hoy.isAfter(descuento.getFechaFin()))) {

                                BigDecimal porcentaje = BigDecimal.valueOf(descuento.getValor());
                                BigDecimal montoDescuento = precioOriginal.multiply(porcentaje).divide(BigDecimal.valueOf(100));
                                precioFinal = precioOriginal.subtract(montoDescuento);
                            }
                        }

                        d.setPrecioUnitario(precioFinal);

                        return precioFinal.multiply(BigDecimal.valueOf(d.getCantidad()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("Esoty acaaa2");

            if (codigoDescuento != null && codigoDescuento.isValid()) {
                BigDecimal porcentaje = BigDecimal.valueOf(codigoDescuento.getPorcentajeDescuento());
                BigDecimal descuento = totalBruto.multiply(porcentaje).divide(BigDecimal.valueOf(100));

                BigDecimal tope = BigDecimal.valueOf(codigoDescuento.getTope());
                if (descuento.compareTo(tope) > 0) {
                    descuento = tope;
                }

                totalBruto = totalBruto.subtract(descuento);
            }
            System.out.println("Esoty acaaa3");
            this.total = totalBruto.setScale(2, RoundingMode.HALF_UP);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCompra = LocalDateTime.now();
    }



}
