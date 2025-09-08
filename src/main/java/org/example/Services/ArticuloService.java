package org.example.Services;

import org.example.Entities.Articulo;
import org.example.Entities.Imagen;
import org.example.Repositories.ArticuloRepository;
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

}
