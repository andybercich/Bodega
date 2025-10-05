package org.example.Services;

import org.example.Entities.Descuento;
import org.example.Entities.Producto;
import org.example.Repositories.DescuentoRepository;
import org.example.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DescuentoService extends BaseService<Descuento,Long, DescuentoRepository>{

    @Autowired
    private final ProductoRepository productoRepository;

    public DescuentoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Page<Descuento> getDescuentosPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener descuentos paginados: " + e.getMessage());
        }
    }

    @Override
    public Descuento save(Descuento descuento) throws Exception {
        try {
            if (descuento.getProductos() == null) {
                descuento.setProductos(new ArrayList<>());
            }
            List<Producto> productos = new ArrayList<>();
            for (Producto p : descuento.getProductos()) {
                Producto producto = productoRepository.findById(p.getId())
                        .orElseThrow(() -> new Exception("Producto con id " + p.getId() + " no encontrado"));

                producto.setDescuento(descuento);
                productos.add(producto);
            }
            descuento.setProductos(productos);
            return repository.save(descuento);
        } catch (Exception e) {
            throw new Exception("Error al crear descuento: " + e.getMessage());
        }
    }

    @Override
    public Descuento update(Long id, Descuento descuento) throws Exception {
        try {
            Descuento descuentoExistente = repository.findById(id)
                    .orElseThrow(() -> new Exception("Descuento no encontrado"));

            descuentoExistente.setValor(descuento.getValor());
            descuentoExistente.setFechaInicio(descuento.getFechaInicio());
            descuentoExistente.setFechaFin(descuento.getFechaFin());
            descuentoExistente.setEstado(descuento.isEstado());

            List<Producto> nuevosProductos = new ArrayList<>();
            List<Long> nuevosIds = descuento.getProductos().stream()
                    .map(Producto::getId)
                    .toList();

            for (Producto p : descuentoExistente.getProductos()) {
                if (!nuevosIds.contains(p.getId())) {
                    p.setDescuento(null);
                }
            }

            for (Producto p : descuento.getProductos()) {
                Producto producto = productoRepository.findById(p.getId())
                        .orElseThrow(() -> new Exception("Producto no encontrado con id " + p.getId()));
                producto.setDescuento(descuentoExistente);
                nuevosProductos.add(producto);
            }

            descuentoExistente.setProductos(nuevosProductos);

            return repository.save(descuentoExistente);

        } catch (Exception e) {
            throw new Exception("Error al actualizar descuento: " + e.getMessage());
        }
    }


    public BigDecimal calcularPrecioFinal(String codigoProducto) {
        Producto producto = productoRepository.findByCodigo(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        double precioBase = producto.getPrecio();
        Descuento descuento = producto.getDescuento();

        if (descuento != null && descuento.isValid()) {
            double porcentaje = descuento.getValor() / 100.0;
            double precioFinal = precioBase - (precioBase * porcentaje);
            return BigDecimal.valueOf(precioFinal).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(precioBase);
    }

}
