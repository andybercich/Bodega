package org.example.Repositories;

import org.example.Entities.Compra;
import org.example.Entities.Enum.EstadoCompra;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends BaseRepository<Compra, Long>, JpaSpecificationExecutor<Compra> {

    List<Compra> findByUsuarioId(Long idUsuario);

    List<Compra> findByEstadoCompraAndFechaCompraBefore(EstadoCompra estadoCompra, LocalDateTime fecha);

    int deleteByEstadoCompraAndFechaCompraBefore(EstadoCompra estadoCompra, LocalDateTime fecha);
}
