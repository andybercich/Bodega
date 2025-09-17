package org.example.Entities.EspecificationsSearch;

import jakarta.persistence.criteria.Predicate;
import org.example.Entities.Producto;
import org.example.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class ProductoSpecifications {

    public static Specification<Producto> filtrar(
            Integer stockMin,
            Integer stockMax,
            Double precioMin,
            Double precioMax,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Boolean conPadre,
            List<Long> categorias,
            Boolean destacado,
            Boolean conDescuento,
            String keyword
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();


            if (stockMin != null) predicate = cb.and(predicate, cb.ge(root.get("stock"), stockMin));
            if (stockMax != null) predicate = cb.and(predicate, cb.le(root.get("stock"), stockMax));

            if (precioMin != null) predicate = cb.and(predicate, cb.ge(root.get("precio"), precioMin));
            if (precioMax != null) predicate = cb.and(predicate, cb.le(root.get("precio"), precioMax));
            if (fechaDesde != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fechaCreacion"), fechaDesde));
            if (fechaHasta != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fechaCreacion"), fechaHasta));

            if (conPadre != null && conPadre) {
                predicate = cb.and(predicate, cb.isNotNull(root.get("productoPadre")));
            }

            if (categorias != null && !categorias.isEmpty()) {
                predicate = cb.and(predicate, root.get("categoria").get("id").in(categorias));
            }

            if (destacado != null && destacado){
                predicate = cb.and(predicate, cb.equal(root.get("destacado"), destacado));
            }
            if (conDescuento != null && conDescuento) {
                predicate = cb.and(predicate, cb.isNotNull(root.get("descuento")));
            }


            if (keyword != null && !keyword.trim().isEmpty()) {
                String[] palabras = keyword.toLowerCase().split("\\s+");
                for (String palabra : palabras) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get("nombre")), "%" + palabra + "%"));
                }
            }

            return predicate;
        };
    }

    public static Specification<Producto> obtenerRelacionados(Long productoId, Long padreId) {
        return (root, query, cb) -> {
            Predicate pred;
            if (padreId != null) {
                pred = cb.or(
                        cb.equal(root.get("id"), padreId),
                        cb.equal(root.get("productoPadre").get("id"), padreId)
                );
            } else {
                pred = cb.equal(root.get("productoPadre").get("id"), productoId);
            }

            return cb.and(pred, cb.notEqual(root.get("id"), productoId));
        };
    }


}
