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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "usuario_favoritos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Producto> favoritos = new HashSet<>();

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToMany(mappedBy = "usuarios")
    private List<Direccion> direcciones = new ArrayList<>();

    public boolean usuarioNuevo(){
        return fechaRegistro != null && !LocalDate.now().isAfter(fechaRegistro.plusWeeks(1));
    }

    public void agregarFavorito(Producto producto) {
        favoritos.add(producto);
    }

    public void quitarFavorito(Producto producto) {
        favoritos.remove(producto);
    }


}
