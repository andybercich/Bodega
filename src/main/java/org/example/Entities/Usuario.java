package org.example.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "La contraseña de usuario no puede ser nula")
    private String password;

    private LocalDate fechaRegistro;

    @NotNull(message = "El DNI de usuario no puede ser nulo")
    private int dni;

    @Column(unique = true, nullable = false)
    @NotNull(message = "El mail de usuario no puede ser nulo")
    private String mail;

    @ManyToMany
    @JoinTable(
            name = "usuario_favoritos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Producto> favoritos = new HashSet<>();

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToMany(mappedBy = "usuarios")
    private List<Direccion> direcciones = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }

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
