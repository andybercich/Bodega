package org.example.Services;

import org.example.Entities.Articulo;
import org.example.Entities.Imagen;
import org.example.Repositories.ArticuloRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticuloService extends BaseService<Articulo,Long, ArticuloRepository> {

    public Articulo guardarArticuloConFotos(Articulo articulo) throws Exception {
        try {
            for (Imagen imagen : articulo.getFotos()) {
                imagen.setArticulo(articulo);
            }
            return repository.save(articulo);
        }catch (Exception e){
            throw new Exception("Error al guardar articulo y susu imagenes"+e.getMessage());
        }
    }

}
