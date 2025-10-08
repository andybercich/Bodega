package org.example.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Direccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Direccion extends Base{

    private String pais;

    private String provincia;

    private String localidad;

    private String calle;

    private int numero;

    private String codigoPostal;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuarioDireccion",
            joinColumns = @JoinColumn(name = "direccion_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario")
    )
    @JsonIgnore
    private List<Usuario> usuarios = new ArrayList<>();


}
