package org.example.Controllers;

import org.example.Entities.Imagen;
import org.example.Repositories.ImagenRepository;
import org.example.Services.ImagenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagen")
public class ImagenController extends BaseController<Imagen,Long, ImagenRepository, ImagenService> {

    public ImagenController(ImagenService service) {
        super(service);
    }

}
