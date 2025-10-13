package org.example;

import org.example.Entities.*;
import org.example.Entities.Dto.ImagenDTO;
import org.example.Entities.Enum.Rol;
import org.example.Entities.Enum.Tags;
import org.example.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, ProductoRepository productoRepository, ArticuloRepository articuloRepository,
                                ImagenRepository imagenRepository ,DescuentoRepository descuentoRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            //Usuarios
            String adminMail = "admin@tuapp.com";
            String clientMail= "cliente@gmail.com";

            if (usuarioRepository.findByMail(adminMail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setMail(adminMail);
                admin.setRol(Rol.ADMIN);
                admin.setPassword(passwordEncoder.encode("admin123"));
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado con éxito");
            } else {
                System.out.println("Usuario ADMIN ya existe");
            }

            if (usuarioRepository.findByMail(clientMail).isEmpty()) {
                Usuario client = new Usuario();
                client.setNombre("Cliente");
                client.setMail(clientMail);
                client.setDni(12345678);
                client.setRol(Rol.USER);
                client.setPassword(passwordEncoder.encode("cliente123"));
                usuarioRepository.save(client);
                System.out.println("Usuario Cliente creado con éxito");
            } else {
                System.out.println("Usuario Cliente ya existe");
            }

            //Categorías
            Categoria cat1 = new Categoria("Categoría 1", "Descripción para la categoría 1");
            Categoria cat2 = new Categoria("Categoría 2", "Descripción para la categoría 2");
            Categoria cat3 = new Categoria("Categoría 3", "Descripción para la categoría 3");
            categoriaRepository.save(cat1);
            categoriaRepository.save(cat2);
            categoriaRepository.save(cat3);


            //Articulos
            //Artículos/Imagen 1
            Articulo art1 = new Articulo("El Corazón de Nuestra Bodega: Donde Nace Cada Vino","La bodega es el lugar donde todo comienza, el corazón de nuestra pasión por el vino. Cada rincón de nuestra fábrica refleja años de experiencia, tradición y cuidado artesanal. Desde la recepción de las uvas recién cosechadas, se realiza una meticulosa selección para asegurar que solo los frutos más maduros y saludables entren en el proceso de elaboración. La fermentación se lleva a cabo bajo estrictos controles de temperatura y humedad, combinando técnicas modernas con métodos tradicionales que nuestros enólogos han perfeccionado con el tiempo. Cada paso, desde el prensado hasta el embotellado, está diseñado para conservar los aromas y sabores únicos de cada variedad de uva. Visitar nuestra fábrica no solo es observar un proceso industrial, sino sumergirse en la historia, la dedicación y la pasión que se esconden detrás de cada botella. Aquí, la magia del vino cobra vida, y cada detalle cuenta para ofrecer productos de la más alta calidad a nuestros clientes y amantes del vino."
                    ,null,LocalDate.now(), Tags.Bodega);
            articuloRepository.save((art1));
            Imagen imagenArticulo1 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760392798/Imagen_articulo_1_tyhqxt.jpg","Imagen del articulo 1",null,art1);
            imagenRepository.save(imagenArticulo1);

            //Artículos/Imagen 2
            Articulo art2 = new Articulo("La Magia de la Crianza: Vinos en Barrica","El verdadero carácter de un vino se descubre en la crianza, donde la paciencia y la dedicación se transforman en aromas y sabores únicos. Nuestros barriles de roble son cuidadosamente seleccionados para garantizar que cada vino desarrolle su máxima expresión. A medida que el vino reposa en estas barricas, interactúa con la madera, absorbiendo matices complejos y equilibrados que realzan su cuerpo y suavizan sus taninos. Cada barril tiene su propia historia y personalidad, y nuestro equipo de enólogos supervisa periódicamente el proceso, asegurándose de que el vino evolucione de manera óptima. Observar la alineación de los barriles en nuestras bodegas es contemplar un proceso casi poético, donde el tiempo, la tradición y la experiencia se combinan para crear vinos que despiertan emociones y cuentan historias en cada sorbo."
                    ,null,LocalDate.now(), Tags.Vino);
            articuloRepository.save((art2));
            Imagen imagenArticulo2 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760392798/Imagen_articulo_2_e8j4ze.jpg","Imagen del articulo 2",null,art2);
            imagenRepository.save(imagenArticulo2);

            //Artículos/Imagen 3
            Articulo art3 = new Articulo("Entre Viñedos: La Esencia de Nuestras Uvas","Nuestros viñedos son el alma de la bodega y el origen de cada experiencia vinícola que ofrecemos. Cada racimo de uvas es cultivado con esmero, siguiendo prácticas sostenibles que respetan la tierra, el clima y la biodiversidad del entorno. Desde la plantación hasta la cosecha, cada etapa es cuidadosamente planificada para garantizar que las uvas alcancen su punto óptimo de maduración. La calidad del fruto depende del suelo, la exposición al sol y la atención constante de nuestro equipo de viticultores, quienes combinan conocimientos ancestrales con técnicas modernas de cultivo. Pasear por nuestros viñedos es conectarse con la naturaleza, sentir la historia de cada cepa y comprender cómo la pasión y el trabajo constante se reflejan en cada botella. Cada uva contiene la esencia de nuestra tierra, y nuestros vinos son un reflejo fiel de ese origen, ofreciendo aromas, sabores y experiencias auténticas e inolvidables."
                    ,null,LocalDate.now(), Tags.Uva);
            articuloRepository.save((art3));
            Imagen imagenArticulo3 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760392799/Imagen_articulo_3_suwf0s.jpg","Imagen del articulo 3",null,art3);
            imagenRepository.save(imagenArticulo3);


            //Descuentos
            Descuento desc1 = new Descuento(10,LocalDate.now(),LocalDate.now().plusDays(10),null);
            descuentoRepository.save(desc1);
            Descuento desc2 = new Descuento(15,LocalDate.now(),LocalDate.now().plusDays(8),null);
            descuentoRepository.save(desc2);

            //Productos
            //Producto/Imagen 1
            Producto prodPadre1 = new Producto("AAAA","Producto 1",5,"Descripción del producto 1: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",20,cat1,true, LocalDate.now(),null,1,null,desc1);
            productoRepository.save(prodPadre1);
            Imagen imagenProducto1 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390056/Imagen_producto_1_mzp8zt.webp","Imagen del producto 1",prodPadre1,null);
            imagenRepository.save(imagenProducto1);

            //Producto/Imagen 2
            Producto prodPadre2 = new Producto("BBBB","Producto 2",10,"Descripción del producto 2: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",30,cat2,true, LocalDate.now(),null,1,null,desc2);
            productoRepository.save(prodPadre2);
            Imagen imagenProducto2 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390056/Imagen_producto_2_gpwgmx.webp","Imagen del producto 2",prodPadre2,null);
            imagenRepository.save(imagenProducto2);

            //Producto/Imagen 3
            Producto prodPadre3 = new Producto("CCCC","Producto 3",2,"Descripción del producto 3: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",20,cat3,true, LocalDate.now(),null,1,null,null);
            productoRepository.save(prodPadre3);
            Imagen imagenProducto3 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390056/Imagen_producto_3_nt4rxt.webp","Imagen del producto 3",prodPadre3,null);
            imagenRepository.save(imagenProducto3);

            //Producto/Imagen 4
            Producto prodPadre4 = new Producto("DDDD","Producto 4",20,"Descripción del producto 4: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",20,cat1,true, LocalDate.now(),null,1,null,null);
            productoRepository.save(prodPadre4);
            Imagen imagenProducto4 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390056/Imagen_producto_4_zfwp8u.jpg","Imagen del producto 4",prodPadre4,null);
            imagenRepository.save(imagenProducto4);

            //Producto/Imagen 5
            Producto prodHijo1 = new Producto("EEEE","Producto 5",10,"Descripción del producto 5: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",15,cat2,false, LocalDate.now(),prodPadre1,1,null,null);
            productoRepository.save(prodHijo1);
            Imagen imagenProducto5 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390056/Imagen_producto_5_ghhlee.webp","Imagen del producto 5",prodHijo1,null);
            imagenRepository.save(imagenProducto5);


            //Producto/Imagen 6
            Producto prodHijo2 = new Producto("FFFF","Producto 6",15,"Descripción del producto 6: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",20,cat3,false, LocalDate.now(),prodPadre2,1,null,null);
            productoRepository.save(prodHijo2);
            Imagen imagenProducto6 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390060/Imagen_producto_6_tgzf5i.webp","Imagen del producto 6",prodHijo2,null);
            imagenRepository.save(imagenProducto6);

            //Producto/Imagen 7
            Producto prodHijo3 = new Producto("GGGG","Producto 7",25,"Descripción del producto 7: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",35,cat1,false, LocalDate.now(),prodPadre3,1,null,desc1);
            productoRepository.save(prodHijo3);
            Imagen imagenProducto7 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760398497/Imagen_producto_7_wwqeaw.webp","Imagen del producto 7",prodHijo3,null);
            imagenRepository.save(imagenProducto7);

            //Producto/Imagen 8
            Producto prodHijo4 = new Producto("HHHH","Producto 8",15,"Descripción del producto 8: este es un producto de alta calidad, diseñado para ofrecer el mejor rendimiento y durabilidad. Ideal para tus necesidades diarias, tanto para uso personal como profesional. Garantiza satisfacción y confiabilidad en cada uso.",20,cat2,false, LocalDate.now(),prodPadre4,1,null,desc2);
            productoRepository.save(prodHijo4);
            Imagen imagenProducto8 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390060/Imagen_producto_8_snorg6.webp","Imagen del producto 8",prodHijo4,null);
            imagenRepository.save(imagenProducto8);
            Imagen imagenProducto9 = new Imagen("https://res.cloudinary.com/degfkwcbm/image/upload/v1760390060/Imagen_producto_8_2_i7reeb.jpg","Imagen del producto 8_2",prodHijo4,null);
            imagenRepository.save(imagenProducto9);

        };
    }
}

