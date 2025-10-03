package org.example.Repositories;

import org.example.Entities.Direccion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends BaseRepository<Direccion, Long> {

    @Query("SELECT d FROM Direccion d JOIN d.usuarios u WHERE u.id = :usuarioId")
    List<Direccion> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
