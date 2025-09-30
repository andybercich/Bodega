package org.example.Entities.EspecificationsSearch;

import jakarta.persistence.criteria.Predicate;
import org.example.Entities.Compra;
import org.example.Entities.Enum.EstadoCompra;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class CompraSpecifications {

    public static Specification<Compra> filtrar(
            String codigoSeguimiento,
            String nombreUsuario,
            LocalDateTime fechaDesde,
            LocalDateTime fechaHasta,
            List<EstadoCompra> estados
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (codigoSeguimiento != null && !codigoSeguimiento.trim().isEmpty()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("codigoSeguimiento")),
                                "%" + codigoSeguimiento.toLowerCase() + "%"
                        )
                );
            }

            if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("usuario").get("nombre")),
                                "%" + nombreUsuario.toLowerCase() + "%"
                        )
                );
            }

            if (fechaDesde != null) {
                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(root.get("fechaCompra"), fechaDesde)
                );
            }
            if (fechaHasta != null) {
                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(root.get("fechaCompra"), fechaHasta)
                );
            }

            if (estados != null && !estados.isEmpty()) {
                predicate = cb.and(
                        predicate,
                        root.get("estadoCompra").in(estados)
                );
            }

            return predicate;
        };
    }
}
