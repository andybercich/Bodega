package org.example.Services;

import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Imagen;
import org.example.Entities.Producto;
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

    public ImagenService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Imagen agregarImagenAProducto(Long productoId, ImagenDTO dto) throws Exception{
        try{
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Imagen imagen = new Imagen();
            imagen.setUrl(dto.getUrl());
            imagen.setAlt(dto.getAlt());
            imagen.setProducto(producto);

            return repository.save(imagen);
        }catch (Exception e){
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
