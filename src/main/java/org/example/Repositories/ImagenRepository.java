package org.example.Repositories;

import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenRepository extends BaseRepository<Imagen,Long>{

    List<Imagen> findByProducto(Producto producto);

}
