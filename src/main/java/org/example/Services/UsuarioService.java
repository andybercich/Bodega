package org.example.Services;

import org.example.Entities.AuthResponse;
import org.example.Entities.Dto.*;
import org.example.Entities.Enum.Rol;
import org.example.Entities.Producto;
import org.example.Entities.Usuario;
import org.example.Entities.UsuariosNuevos;
import org.example.JWT.JwtService;
import org.example.Repositories.ProductoRepository;
import org.example.Repositories.UsuarioRepository;
import org.example.Repositories.UsuariosNuevosRepository;
import org.example.Services.DTO.ValidacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> {

    @Autowired
    private UsuariosNuevosRepository usuariosNuevosRepository;

    @Autowired
    private CodigoService codigoService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductoRepository productoRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuariosNuevos registrarUsuario(UsuariosNuevos newUser) throws Exception {
        try {

            String codigoVerificacion = codigoService.generarCodigoUnico();
            newUser.setFechaRegistro(LocalDate.now());

            UsuariosNuevos usuarioNuevo = new UsuariosNuevos(
                    passwordEncoder.encode(newUser.getPassword()),
                    newUser.getFechaRegistro(),
                    newUser.getDni(),
                    newUser.getMail(),
                    newUser.getNombre(),
                    Rol.USER,
                    codigoVerificacion
            );

            usuariosNuevosRepository.deleteByMail(usuarioNuevo.getMail());

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

    public AuthResponse validarCodigoYRegistrar(ValidacionDTO validacionDTO){
            UsuariosNuevos usuarioNuevo = usuariosNuevosRepository.findByMail(validacionDTO.getMail());

            if (!usuarioNuevo.getCodigoVerificacion().equals(validacionDTO.getCodVerificacion())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código de verificación incorrecto");
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

        AuthResponse authResponse = AuthResponse.builder()
                .fechaRegistro(usuarioNuevo.getFechaRegistro())
                .dni(usuarioNuevo.getDni())
                .mail(usuarioNuevo.getMail())
                .nombre(usuarioNuevo.getNombre())
                .rol(usuarioNuevo.getRol())
                .direcciones(new ArrayList<>())
                .favoritos(new HashSet<>())
                .build();

            return authResponse;


    }

    public AuthResponse login(LoginDTO loginDTO) {
        Usuario usuario = repository.findByMail(loginDTO.getMail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o contraseña incorrecta");
        }

        return new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getDni(),
                usuario.getMail(),
                usuario.getRol(),
                DireccionDTO.fromEntities(usuario.getDirecciones()),
                usuario.getFechaRegistro(),
                ProductoDTO.fromEntities(usuario.getFavoritos()),
                jwtService.getToken(usuario)
        );
    }

    public boolean agregarFavorito(Long idProduct) throws Exception {
        try {

            Usuario user = repository.findByMail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Producto newProductoFavorito = productoRepository.getReferenceById(idProduct);

            user.agregarFavorito(newProductoFavorito);

            return true;


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean eliminarFavorito(Long idProduct) throws Exception {
        try {

            Usuario user = repository.findByMail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Producto deleteProductoFavorito = productoRepository.getReferenceById(idProduct);

            user.quitarFavorito(deleteProductoFavorito);

            return true;


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Set<ProductoDTO> obtenerFavoritos() {
        try {

            Usuario user = repository.findByMail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Set<Producto> productos = user.getFavoritos();

            return productos.stream()
                    .map(ProductoDTO::fromEntity)
                    .collect(Collectors.toSet());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
                        .password(passwordEncoder.encode(userAdmin.getPassword())) //FALTA ENCRIPTAR ACA, Listo
                        .rol(Rol.ADMIN)
                        .fechaRegistro(LocalDate.now())
                        .estado(true)
                        .build();

                return repository.save(admin);

            } catch (Exception e) {
                throw new Exception("Error al crear usuario admin: " + e.getMessage());
            }
    }

    public boolean mailExistente(String mail) {
        try {

            return repository.existsByMail(mail);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
        public Usuario updateUserByAdmin (Long id, UpdateUserDTO dto) throws Exception {
                try {
                    Usuario usuario = repository.findById(id)
                            .orElseThrow(() -> new Exception("Usuario no encontrado"));

                    usuario.setNombre(dto.getNombre());
                    usuario.setMail(dto.getMail());

                    return repository.save(usuario);

                } catch (Exception e) {
                    throw new Exception("Error al actualizar usuario desde admin: " + e.getMessage());

        }
    }

    public UsuarioDTO updateDataUser(DataUser dataUser){
        Usuario user = repository.findByMail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado"));

        user.setDni(dataUser.getDni());user.setNombre(dataUser.getNombre());
        return UsuarioDTO.fromEntity(repository.saveAndFlush(user));
    }
}