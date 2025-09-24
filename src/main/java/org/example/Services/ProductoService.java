package org.example.Services;

import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Dto.ProductoDTO;
import org.example.Entities.Dto.ProductoPageDTO;
import org.example.Entities.EspecificationsSearch.ProductoSpecifications;
import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.example.Repositories.ImagenRepository;
import org.example.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductoService extends BaseService<Producto,Long, ProductoRepository>{

    /*public Producto guardarProductoConFotos(Producto producto) throws Exception {
        try {
            for (Imagen imagen : producto.getImagenes()) {
                imagen.setProducto(producto);
            }
            return repository.save(producto);
        }catch (Exception e){
            throw new Exception("Error al guardar producto y sus imagenes"+e.getMessage());
        }
    }*/

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

    public List<ProductoDTO> getProductosPadre() throws Exception{
        try{
            return repository.findByProductoPadreIsNull()
                    .stream()
                    .map(ProductoDTO::fromEntity)
                    .toList();
        }catch (Exception e){
            throw new Exception("No se pudieron obtener los productos padre: "+e.getMessage());
        }
    }

    public List<ProductoDTO> getHijosByPadre(Long padreId) throws Exception{
        try{
            return repository.findByProductoPadreId(padreId)
                    .stream()
                    .map(ProductoDTO::fromEntity)
                    .toList();
        }catch (Exception e){
            throw new Exception("No se pudo obtener los los productos hijos: "+e.getMessage());
        }
    }

    public ProductoPageDTO buscarProductos(
            boolean nuevo,
            Integer stockMin,
            Integer stockMax,
            Double precioMin,
            Double precioMax,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean conPadre,
            List<Long> categorias,
            Boolean destacado,
            Boolean conDescuento,
            String keyword,
            int page,
            int size
    ) throws Exception{
        try {
            Pageable pageable;
            if (nuevo){
                 pageable = PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "fechaCreacion")
                );
            }else{
                 pageable = PageRequest.of(
                        page,
                        size);
            }


        Specification<Producto> spec = ProductoSpecifications.filtrar(
                stockMin,
                stockMax,
                precioMin,
                precioMax,
                fechaDesde,
                fechaHasta,
                conPadre,
                categorias,
                destacado,
                conDescuento,
                keyword
        );
        Page<Producto> productos = repository.findAll(spec, pageable);
        return new ProductoPageDTO(productos.getContent(), page, size, productos.getTotalPages());

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

    public List<ProductoDTO> obtenerRelacionados(Long productoId) {
        Producto productoOriginal = repository.findById(productoId).orElseThrow();

        Long padreId = repository.findById(productoId)
                .map(p -> p.getProductoPadre() != null ? p.getProductoPadre().getId() : null)
                .orElse(null);

        Specification<Producto> spec = ProductoSpecifications.obtenerRelacionados(productoId, padreId);
        List<Producto> productosRelacionados= repository.findAll(spec);

        if (productosRelacionados.size()<10){

            Pageable limit = PageRequest.of(0, 10);
            productosRelacionados.addAll(repository.findByCategoriaIDLimited(productoOriginal.getCategoria().getId(),
                    productoId, limit));
        }

        return productosRelacionados
                .stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

}
