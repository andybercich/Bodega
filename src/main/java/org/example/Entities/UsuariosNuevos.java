package org.example.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.Entities.Enum.Rol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "UsuarioNoRegistrados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UsuariosNuevos extends Base{

    @NotNull(message = "La contraseña de usuario no puede ser nula")
    private String password;

    private LocalDate fechaRegistro;

    @NotNull(message = "El DNI de usuario no puede ser nulo")
    private int dni;

    @NotNull(message = "El mail de usuario no puede ser nulo")
    private String mail;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(length = 4)
    @Size(min = 4, max = 4, message = "El código debe tener 4 caracteres")
    private String codigoVerificacion;


}
