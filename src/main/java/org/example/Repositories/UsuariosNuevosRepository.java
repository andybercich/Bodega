package org.example.Repositories;

import org.example.Entities.Base;
import org.example.Entities.UsuariosNuevos;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UsuariosNuevosRepository extends BaseRepository<UsuariosNuevos, Long> {

    void deleteByFechaRegistroBefore(LocalDate fecha);

    boolean existsByCodigoVerificacion(String codigoVerificacion);

    UsuariosNuevos findByMail(String mail);

}
