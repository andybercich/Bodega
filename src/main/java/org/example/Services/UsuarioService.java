package org.example.Services;

import org.example.Entities.Categoria;
import org.example.Entities.Dto.CreateAdminDTO;
import org.example.Entities.Enum.Rol;
import org.example.Entities.Usuario;
import org.example.Entities.UsuariosNuevos;
import org.example.Repositories.UsuarioRepository;
import org.example.Repositories.UsuariosNuevosRepository;
import org.example.Services.DTO.ValidacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;


@Service
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> {

    @Autowired
    private UsuariosNuevosRepository usuariosNuevosRepository;

    @Autowired
    private CodigoService codigoService;

    @Autowired
    private JavaMailSender mailSender;

    public Usuario registrarUsuario(Usuario newUser) throws Exception {
        try {
            String codigoVerificacion = codigoService.generarCodigoUnico();

            UsuariosNuevos usuarioNuevo = new UsuariosNuevos(
                    newUser.getPassword(),
                    newUser.getFechaRegistro(),
                    newUser.getDni(),
                    newUser.getMail(),
                    newUser.getNombre(),
                    Rol.User,
                    codigoVerificacion
            );

            usuariosNuevosRepository.save(usuarioNuevo);

            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(newUser.getMail());
            mensaje.setSubject("Código de verificación");
            mensaje.setText("Hola " + newUser.getNombre() + ", tu código de verificación es: " + codigoVerificacion);

            mailSender.send(mensaje);

            return newUser;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Usuario validarCodigoYRegistrar(ValidacionDTO validacionDTO) throws Exception {
        try {
            UsuariosNuevos usuarioNuevo = usuariosNuevosRepository.findByMail(validacionDTO.getMail());

            if (!usuarioNuevo.getCodigoVerificacion().equals(validacionDTO.getCodVerificacion())) {
                throw new Exception("Código de verificación incorrecto");
            }

            Usuario usuario = Usuario.builder()
                    .password(usuarioNuevo.getPassword())
                    .estado(true)
                    .fechaRegistro(usuarioNuevo.getFechaRegistro())
                    .dni(usuarioNuevo.getDni())
                    .mail(usuarioNuevo.getMail())
                    .nombre(usuarioNuevo.getNombre())
                    .rol(usuarioNuevo.getRol())
                    .direcciones(new ArrayList<>())
                    .favoritos(new HashSet<>())
                    .build();

            repository.save(usuario);

            usuariosNuevosRepository.delete(usuarioNuevo);

            return usuario;

        } catch (Exception e) {
            throw new Exception("Error al validar y registrar usuario: " + e.getMessage());
        }

    }

    public Page<Usuario> getUsuariosPaginados(int page, int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        }catch (Exception e){
            throw new Exception("Error al obtener usuarios paginados: " + e.getMessage());
        }
    }

    public Usuario crearUserAdmin(CreateAdminDTO userAdmin) throws Exception{
            try {
                Usuario admin = Usuario.builder()
                        .nombre(userAdmin.getNombre())
                        .mail(userAdmin.getMail())
                        .password(userAdmin.getPassword())
                        .dni(userAdmin.getDni())
                        .rol(Rol.Admin) // ⚠️ siempre ADMIN
                        .fechaRegistro(LocalDate.now())
                        .estado(true)
                        .build();

                return repository.save(admin);

            } catch (Exception e) {
                throw new Exception("Error al crear usuario admin: " + e.getMessage());
            }
    }

}
