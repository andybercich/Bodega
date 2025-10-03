package org.example.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Value("${cloudinary.folder.name}")
    private String cloudFolderName;

    public Map upload(MultipartFile file) throws Exception {
        //MultipartFile: Es el archivo que recibís desde el frontend
        try {
            Map uploadResult = cloudinary.uploader().upload( //metodo que sube el archivo
                    file.getBytes(), // Convierte el archivo en un arreglo de bytes, que es lo que Cloudinary necesita para subirlo
                    ObjectUtils.asMap(
                            "folder", cloudFolderName)); // le dice a Cloudinary en qué carpeta guardar la imagen
            return uploadResult; //Cloudinary devuelve un Map con información de la imagen subida
        }catch (Exception e){
            throw  new Exception("Error al subir la imagen a cloud: "+e.getMessage());
        }
    }

}
