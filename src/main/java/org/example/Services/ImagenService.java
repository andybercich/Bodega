package org.example.Services;

import org.example.Entities.Articulo;
import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.example.Repositories.ArticuloRepository;
import org.example.Repositories.ImagenRepository;
import org.example.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImagenService extends BaseService<Imagen,Long, ImagenRepository>{

    @Autowired
    private final ProductoRepository productoRepository;

    @Autowired
    private final ArticuloRepository articuloRepository;

    public ImagenService(ProductoRepository productoRepository, ArticuloRepository articuloRepository) {
        this.productoRepository = productoRepository;
        this.articuloRepository = articuloRepository;
    }

    @Transactional
    public Imagen agregarImagenAProducto(Long productoId, ImagenDTO dto) throws Exception {
        try {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getImagenes().size() >= 4) {
                throw new RuntimeException("El producto ya tiene el máximo de 4 imágenes");
            }

            Imagen imagen = new Imagen();
            imagen.setUrl(dto.getUrl());
            imagen.setAlt(dto.getAlt());
            imagen.setProducto(producto);
            imagen.setEstado(true);

            return repository.save(imagen);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public Imagen agregarImagenArticulo(Long articuloId, ImagenDTO dto) throws Exception {
        try {
            Articulo articulo = articuloRepository.findById(articuloId)
                    .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));


            Imagen imagen = new Imagen();
            imagen.setUrl(dto.getUrl());
            imagen.setAlt(dto.getAlt());
            imagen.setArticulo(articulo);
            imagen.setEstado(true);

            return repository.save(imagen);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public List<Imagen> obtenerImagenesPorProducto(Long productoId) throws Exception{
        try{
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            return repository.findByProducto(producto);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
