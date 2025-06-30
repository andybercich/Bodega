package org.example.Services;

import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.springframework.stereotype.Service;

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


}
