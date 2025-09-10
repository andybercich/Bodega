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

    public Articulo guardarArticuloConFoto(Articulo articulo) throws Exception {
        try {
            if (articulo.getImagen() != null) {
                articulo.getImagen().setArticulo(articulo);
            }
            return repository.save(articulo);
        } catch (Exception e) {
            throw new Exception("Error al guardar articulo y su imagen: " + e.getMessage());
        }
    }

    public Page<Articulo> getArticulosPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener articulos paginados: " + e.getMessage());
        }
    }



}
