package org.example.Services;

import jakarta.transaction.Transactional;
import org.example.Entities.*;
import org.example.Entities.Dto.CompraPageDTO;
import org.example.Entities.Dto.ProductoDTO;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Entities.EspecificationsSearch.CompraSpecifications;
import org.example.Repositories.CompraRepository;
import org.example.Repositories.ProductoRepository;
import org.example.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService extends BaseService<Compra, Long, CompraRepository>{

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CodigoDescuentoService codigoDescuentoService;

    @Override
    @Transactional
    public Compra save(Compra orden) {
        try {

            /*Usuario user = usuarioRepository.findByMail( SecurityContextHolder.getContext().getAuthentication().getName() ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));*/ //orden.setUsuario(user); /* if (!orden.isDireccionUsuario()) { direccionService.saveToken(orden.getDireccion()); } else { boolean direccionValida = user.getDirecciones().stream() .anyMatch(d -> d.getId().equals(orden.getDireccion().getId())); if (!direccionValida) { throw new RuntimeException("La dirección no pertenece al usuario"); } } System.out.println(orden.getDetalles());*/

            for (DetalleCompra d : orden.getDetalles()) {
                Producto producto = productoRepository.getReferenceById(d.getProducto().getId());

                if (producto.getProductoPadre() != null) {
                    Producto productoPadre = producto.getProductoPadre();
                    if (productoPadre.getStock() < d.getCantidad() * d.getProducto().getCantidad()) {
                        throw new RuntimeException("No hay stock suficiente para el producto: " + productoPadre.getNombre());
                    }
                    productoPadre.setStock(productoPadre.getStock() - (d.getCantidad() * d.getProducto().getCantidad()));
                    productoRepository.saveAndFlush(productoPadre);
                } else {
                    if (producto.getStock() < d.getCantidad()) {
                        throw new RuntimeException("No hay stock suficiente para el producto: " + producto.getNombre());
                    }
                    producto.setStock(producto.getStock() - d.getCantidad());
                    productoRepository.saveAndFlush(producto);
                }

                BigDecimal precioOriginal = BigDecimal.valueOf(producto.getPrecio());
                if (producto.getDescuento() != null && producto.getDescuento().isValid()) {
                    BigDecimal porcentaje = BigDecimal.valueOf(producto.getDescuento().getValor());
                    BigDecimal montoDescuento = precioOriginal.multiply(porcentaje).divide(BigDecimal.valueOf(100));
                    precioOriginal = precioOriginal.subtract(montoDescuento);
                }

                d.setPrecioUnitario(precioOriginal);
                d.setProducto(producto);
                d.setCompra(orden);
            }

            orden.setFechaCompra(LocalDateTime.now());

            if (orden.getCodigoDescuento() != null) {
                Usuario usuario = orden.getUsuario();
                AplicarCodigo resultado = codigoDescuentoService.aplicarCodigo(
                        orden.getCodigoDescuento().getCodigo(),
                        usuario
                );

                if (!resultado.isValido()) {
                    throw new RuntimeException("El código de descuento no puede ser aplicado.");
                }
            }

            orden.calcularTotal();

            return repository.save(orden);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar la orden de compra: " + e.getMessage(), e);
        }
    }


    @Transactional
    @Override
    public Compra update(Long id,Compra nuevaCompra) {
        try {
            Compra compraExistente = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            for (DetalleCompra detalleAnterior : compraExistente.getDetalles()) {
                Producto producto = productoRepository.findById(detalleAnterior.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (producto.getProductoPadre() != null) {
                    Producto padre = producto.getProductoPadre();
                    padre.setStock(padre.getStock() + detalleAnterior.getCantidad()*detalleAnterior.getProducto().getCantidad());
                    productoRepository.save(padre);
                } else {
                    producto.setStock(producto.getStock() + detalleAnterior.getCantidad());
                    productoRepository.save(producto);
                }
            }

            compraExistente.getDetalles().clear();
            repository.save(compraExistente);

            for (DetalleCompra nuevoDetalle : nuevaCompra.getDetalles()) {
                Producto producto = productoRepository.findById(nuevoDetalle.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (producto.getProductoPadre() != null) {
                    Producto padre = producto.getProductoPadre();
                    if (padre.getStock() < nuevoDetalle.getCantidad()*nuevoDetalle.getProducto().getCantidad()) {
                        throw new RuntimeException("No hay stock suficiente para: " + padre.getNombre());
                    }
                    padre.setStock(padre.getStock() - nuevoDetalle.getCantidad());
                    productoRepository.save(padre);
                } else {
                    if (producto.getStock() < nuevoDetalle.getCantidad()) {
                        throw new RuntimeException("No hay stock suficiente para: " + producto.getNombre());
                    }
                    producto.setStock(producto.getStock() - nuevoDetalle.getCantidad());
                    productoRepository.save(producto);
                }
                BigDecimal precioOriginal = BigDecimal.valueOf(producto.getPrecio());
                if (producto.getDescuento().isValid()){
                    precioOriginal = BigDecimal.valueOf(producto.getPrecio());
                    BigDecimal porcentaje = BigDecimal.valueOf(producto.getDescuento().getValor());
                    BigDecimal montoDescuento = precioOriginal.multiply(porcentaje).divide(BigDecimal.valueOf(100));
                    nuevoDetalle.setPrecioUnitario( precioOriginal.subtract(montoDescuento));
                }
                nuevoDetalle.setPrecioUnitario(precioOriginal);

                nuevoDetalle.setProducto(producto);
                nuevoDetalle.setCompra(nuevaCompra);
                nuevoDetalle.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio()));
            }

            nuevaCompra.setFechaCompra(LocalDateTime.now());
            nuevaCompra.calcularTotal();

            return repository.save(nuevaCompra);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la orden de compra: " + e.getMessage(), e);
        }
    }


    public Page<Compra> getComprasPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener compras paginados: " + e.getMessage());
        }
    }

    public Compra actualizarEstado(Long compraId, EstadoCompra estadoCompra) throws Exception{
        try{
            Compra compra = repository.findById(compraId)
                    .orElseThrow(() -> new RuntimeException("La compra no existe"));

            compra.setEstadoCompra(estadoCompra);
            return compra;
        }catch (Exception e){
            throw new Exception("Error al actualizar estado de la compra: "+e.getMessage());
        }
    }

    public Compra actualizarCodSeguimiento(Long compraId, String codigoSeguimiento) throws Exception{
        try{
            Compra compra = repository.findById(compraId)
                    .orElseThrow(() -> new RuntimeException("La compra no existe"));

            compra.setCodigoSeguimiento(codigoSeguimiento);
            return compra;
        }catch (Exception e){
            throw new Exception("Error al actualizar código de seguiemiento de la compra: "+e.getMessage());
        }
    }

    public List<Compra> getByIdUser(Long id){


        if (!usuarioRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El id usuario no existe");
        }

        return repository.findByUsuarioId(id);

    }

    public CompraPageDTO buscarCompras(
            String codigoSeguimiento,
            String nombreUsuario,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            List<EstadoCompra> estados,
            boolean nuevo,
            int page,
            int size
    ) throws Exception {
        try {
            Pageable pageable;
            if (nuevo) {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaCompra"));
            } else {
                pageable = PageRequest.of(page, size);
            }

            Specification<Compra> spec = CompraSpecifications.filtrar(
                    codigoSeguimiento,
                    nombreUsuario,
                    fechaDesde,
                    fechaHasta,
                    estados
            );

            Page<Compra> compras = repository.findAll(spec, pageable);

            return new CompraPageDTO(compras.getContent(), page, size, compras.getTotalPages());

        } catch (Exception e) {
            throw new Exception("Error al buscar compras: " + e.getMessage(), e);
        }
    }

    public boolean confirmarPago (Long idCompra){

        if (!repository.existsById(idCompra)){
            throw new RuntimeException("NO EXISTE UNA COMPRA CON ESE ID");
        }

        Compra compra = repository.findById(idCompra).orElseThrow();

        compra.setEstadoCompra(EstadoCompra.PREPARANDO);

        repository.saveAndFlush(compra);
        return true;


    }

    public int eliminarComprasCanceladas() {
        LocalDateTime ahora = LocalDateTime.now();

        LocalDateTime limite = ahora.minusDays(3);

        int eliminadas = repository.deleteByEstadoCompraAndFechaCompraBefore(
                EstadoCompra.CANCELADA, limite
        );

        System.out.println("Compras canceladas eliminadas: " + eliminadas);
        return eliminadas;
    }

}
