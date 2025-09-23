package org.example.Services;

import org.example.Entities.Articulo;
import org.example.Entities.Imagen;
import org.example.Repositories.ArticuloRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticuloService extends BaseService<Articulo,Long, ArticuloRepository> {

    public Page<Articulo> getArticulosPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener articulos paginados: " + e.getMessage());
        }
    }

    @Override
    public Articulo activateById(Long id) throws Exception {
        try{
            Articulo articulo = repository.findById(id)
                    .orElseThrow(() -> new Exception("Artículo no encontrado"));

            articulo.setEstado(true);
            if (articulo.getImagen() != null) {
                articulo.getImagen().setEstado(true);
            }
            return repository.save(articulo);
        }catch (Exception e){
            throw new Exception("Error al activar Articulo: " + e.getMessage());
        }
    }

    @Override
    public Articulo deleteById(Long id) throws Exception {
        try{
            Articulo articulo = repository.findById(id)
                    .orElseThrow(() -> new Exception("Artículo no encontrado"));

            articulo.setEstado(false);

            if (articulo.getImagen() != null) {
                articulo.getImagen().setEstado(false);
            }
            return repository.save(articulo);
        }catch (Exception e){
            throw new Exception("Error al eliminar Articulo: " + e.getMessage());
        }
    }

}
