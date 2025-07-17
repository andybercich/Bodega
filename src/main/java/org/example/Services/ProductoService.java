package org.example.Services;

import org.example.Entities.Imagen;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
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

}
