package org.example.Repositories;

import org.example.Entities.Categoria;
import org.example.Entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaNombre(String nombreCategoria);

    Page<Producto> findByDestacadoTrueOrderByFechaCreacionDesc(Pageable pageable);

    //Encontrar los prod con descuentop
    List<Producto> findByDescuentoIsNotNull();

    // Productos con descuento activo
    @Query("SELECT p FROM Producto p WHERE p.descuento IS NOT NULL AND CURRENT_DATE BETWEEN p.descuento.fechaInicio AND p.descuento.fechaFin")
    List<Producto> findProductosConDescuentoActivo();

    //Buscar por stock
    List<Producto> findByStockGreaterThan(int cantidad);
    List<Producto> findByStockLessThanEqual(int cantidad);
    List<Producto> findByStockBetween(int min, int max);

    // Buscar por fecha de creacion
    List<Producto> findByFechaCreacion(LocalDate fecha);
    List<Producto> findByFechaCreacionAfter(LocalDate fecha);
    List<Producto> findByFechaCreacionBetween(LocalDate desde, LocalDate hasta);

    // Buscar por producto padre
    List<Producto> findByProductoPadreIsNotNull();
    List<Producto> findByProductoPadre(Producto padre);

    Optional<Producto> findByCodigo(String codigo);
}

