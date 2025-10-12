package org.example.Services;

import org.example.Entities.Compra;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Entities.Producto;
import org.example.Repositories.CompraRepository;
import org.example.Repositories.ProductoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleaningOrders {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public CleaningOrders(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }


    //Metodo para limpiar bbdd de compras que pasaron más de 24 horas sin confirmarse, y las setea a Canceladas y
    // reestablece el stock
    //y las Compras que han sido canceladas hace mas de 4 dias las borra definitivamente
    @Scheduled(cron = "0 0 0 * * ?")
    public void limpiarCompras() {
        LocalDateTime ahora = LocalDateTime.now();

        LocalDateTime limitePendientes = ahora.minusHours(24);
        List<Compra> pendientes = compraRepository.findByEstadoCompraAndFechaCompraBefore(
                EstadoCompra.PAGOPENDIENTE, limitePendientes
        );

        for (Compra compra : pendientes) {
            compra.getDetalles().forEach(detalle -> {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            });

            compra.setEstadoCompra(EstadoCompra.CANCELADA);
            compraRepository.save(compra);

            System.out.println("Compra cancelada y stock reestablecido: " + compra.getId());
        }

        LocalDateTime limiteEliminar = ahora.minusDays(3);
        int eliminadas = compraRepository.deleteByEstadoCompraAndFechaCompraBefore(
                EstadoCompra.CANCELADA, limiteEliminar
        );

        System.out.println("Compras canceladas eliminadas: " + eliminadas);
    }
}
