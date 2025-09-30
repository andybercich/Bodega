package org.example.Repositories;

import org.example.Entities.Compra;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends BaseRepository<Compra, Long>, JpaSpecificationExecutor<Compra> {

    List<Compra> findByUsuarioId(Long idUsuario);

}
