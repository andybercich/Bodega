package org.example.Services;

import org.example.Entities.Dto.ProductoDTO;
import org.example.Entities.Dto.ProductoPageDTO;
import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService extends BaseService<Producto,Long, ProductoRepository>{

    public Producto guardarProductoConFotos(Producto producto) throws Exception {
        try {
            for (Imagen imagen : producto.getImagenes()) {
                imagen.setProducto(producto);
            }
            return repository.save(producto);
        }catch (Exception e){
            throw new Exception("Error al guardar producto y susu imagenes"+e.getMessage());
        }
    }

    public ProductoPageDTO getProductosDestacados(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Producto> productosDestacados = repository.findByDestacadoTrueOrderByFechaCreacionDesc(pageable);


            return new ProductoPageDTO(productosDestacados.getContent(), page,
                    size, productosDestacados.getTotalPages());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public List<Producto> filtroGeneral(Integer stockMin,
                                        Integer stockMax,
                                        LocalDate fechaDesde,
                                        LocalDate fechaHasta,
                                        Boolean conPadre,
                                        String categoria,
                                        Boolean destacado,
                                        Boolean conDescuento) throws Exception {
        try {

            return repository.filtrarProductos(stockMin,stockMax, fechaDesde, fechaHasta, conPadre, categoria,
                    destacado,conDescuento);

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<ProductoDTO> getAllProductos() throws Exception {
        try{
            return repository.findAll()
                    .stream()
                    .map(ProductoDTO::fromEntity)
                    .toList();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public ProductoDTO getProductoById(Long id) throws Exception {
        try {
            Producto producto = repository.findById(id)
                    .orElseThrow(() -> new Exception("Producto no encontrado con id: " + id));
            return ProductoDTO.fromEntity(producto);

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
