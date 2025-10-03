package org.example.Entities.Dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Entities.Direccion;
import org.example.Entities.Enum.Rol;
import org.example.Entities.Producto;
import org.example.Entities.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UsuarioDTO {

    private long id;

    private int dni;

    private String mail;

    private Set<ProductoDTO> favoritos;

    private String nombre;

    private List<DireccionDTO> direcciones = new ArrayList<>();
    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setDni(usuario.getDni());
        dto.setMail(usuario.getMail());
        dto.setNombre(usuario.getNombre());

        if (usuario.getFavoritos() != null) {
            Set<ProductoDTO> favoritosDto = usuario.getFavoritos().stream()
                    .map(ProductoDTO::fromEntity)
                    .collect(Collectors.toSet());
            dto.setFavoritos(favoritosDto);
        }

        if (usuario.getDirecciones() != null) {
            List<DireccionDTO> direccionesDto = usuario.getDirecciones().stream()
                    .map(d -> {
                        DireccionDTO dDto = new DireccionDTO();
                        dDto.setId(d.getId());
                        dDto.setPais(d.getPais());
                        dDto.setProvincia(d.getProvincia());
                        dDto.setLocalidad(d.getLocalidad());
                        dDto.setCalle(d.getCalle());
                        dDto.setNumero(d.getNumero());
                        dDto.setCodigoPostal(d.getCodigoPostal());
                        return dDto;
                    })
                    .collect(Collectors.toList());
            dto.setDirecciones(direccionesDto);
        }

        return dto;
    }
}
