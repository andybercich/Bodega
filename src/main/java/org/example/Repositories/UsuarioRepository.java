package org.example.Repositories;

import org.example.Entities.Usuario;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long>{

    boolean existsByMail(String mail);

    Optional<Usuario> findByMail(String mail);

}
