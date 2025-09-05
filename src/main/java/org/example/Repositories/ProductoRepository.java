package org.example.Repositories;

import org.example.Entities.Categoria;
import org.example.Entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends BaseRepository<Producto, Long> {

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


    //Query que convinga stock, fecha creacion, con padre, nombre de catecoria, destacado y con descuento exisente
    @Query("""
    SELECT p FROM Producto p
    WHERE 
        (:stockMin IS NULL OR p.stock >= :stockMin) AND
        (:stockMax IS NULL OR p.stock <= :stockMax) AND
        (:fechaDesde IS NULL OR p.fechaCreacion >= :fechaDesde) AND
        (:fechaHasta IS NULL OR p.fechaCreacion <= :fechaHasta) AND
        (:conPadre IS NULL OR (:conPadre = true AND p.productoPadre IS NOT NULL) OR (:conPadre = false AND p.productoPadre IS NULL)) AND
        (:categoria IS NULL OR LOWER(p.categoria.nombre) = LOWER(:categoria)) AND
        (:destacado IS NULL OR p.destacado = :destacado) AND
        (:conDescuento IS NULL OR (:conDescuento = true AND p.descuento IS NOT NULL) OR (:conDescuento = false AND p.descuento IS NULL))
    """)
    List<Producto> filtrarProductos(
            @Param("stockMin") Integer stockMin,
            @Param("stockMax") Integer stockMax,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("conPadre") Boolean conPadre,
            @Param("categoria") String categoria,
            @Param("destacado") Boolean destacado,
            @Param("conDescuento") Boolean conDescuento
    );

    Optional<Producto> findByCodigo(String codigo);
}

