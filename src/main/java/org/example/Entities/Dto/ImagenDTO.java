package org.example.Entities.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Entities.Descuento;
import org.example.Entities.Imagen;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImagenDTO {
    private String url;
    private String alt;

    public ImagenDTO (String alt, String url){
        this.alt = alt;
        this.url = url;
    }

    public static ImagenDTO fromEntity(Imagen imagen){
        return new ImagenDTO(imagen.getAlt(), imagen.getUrl());
    }

    public static List<ImagenDTO> fromEntitys(List<Imagen> imagenes){
        List<ImagenDTO> imagenDTOS = new ArrayList<>();
        for (Imagen imagen : imagenes) {
            imagenDTOS.add(fromEntity(imagen));
        }
        return imagenDTOS;
    }
}
