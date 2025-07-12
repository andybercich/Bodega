package org.example.Entities;

import jakarta.persistence.*;
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
@Table(name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"mail"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Usuario extends Base{

    private String password;

    private LocalDate fechaRegistro;

    private int dni;

    private String mail;

    //private List<Producto> favoritos;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToMany(mappedBy = "usuarios")
    private List<Direccion> direcciones = new ArrayList<>();

    public boolean usuarioNuevo(){
        return fechaRegistro != null && !LocalDate.now().isAfter(fechaRegistro.plusWeeks(1));
    }


}
