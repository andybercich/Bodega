package org.example.Repositories;

import org.example.Entities.Usuario;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface UsuarioRepository extends BaseRepository<Usuario, Long>{

    boolean existsByMail(String mail);

}
