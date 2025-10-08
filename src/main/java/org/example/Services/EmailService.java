package org.example.Services;

import jakarta.mail.internet.MimeMessage;
import org.example.Entities.Compra;
import org.example.Entities.Enum.EstadoCompra;
import org.example.Entities.Usuario;
import org.example.Repositories.CompraRepository;
import org.example.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private final CompraRepository compraRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.to}")
    private String toEmail;

    @Autowired
    private UsuarioRepository userRepository;


    public EmailService(CompraRepository compraRepository, UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Async
    public void sendEmailAsync(String subject, String body, String replyTo) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setText(body, false);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom(fromEmail);
        helper.setReplyTo(replyTo);

        mailSender.send(message);
    }

    @Async
    public void sendMailProblem(Usuario usuarioDTO, String message, Compra compra) throws Exception {

        MimeMessage messageSend = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messageSend, "utf-8");


        helper.setText(message + ".  Codigo de seguimiento compra: "+ compra.getCodigoSeguimiento()
                +" Id compra: " + compra.getId()  + " Fecha compra: "+ compra.getFechaCompra() + " Contacto cliente " +
                "nombre "+usuarioDTO.getNombre()+ ": "+ usuarioDTO.getMail(), false);
        helper.setTo(toEmail);
        helper.setSubject(usuarioDTO.getNombre());
        helper.setFrom(fromEmail);
        helper.setReplyTo(usuarioDTO.getMail());
        mailSender.send(messageSend);


    }

    @Async
    public void sendEmailChangeState(Long pedidoId, Long idUser, EstadoCompra newState) throws Exception {
        Compra pedido = compraRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("No se encontró el pedido: " + pedidoId));

        Usuario usuario = usuarioRepository.findById(idUser)
                .orElseThrow(()-> new RuntimeException("No se encontro el ususario: "+idUser));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(usuario.getMail());
        message.setSubject("Actualización de tu pedido - Bodega Tierra Noble");

        StringBuilder texto = new StringBuilder();
        texto.append("Hola ").append(usuario.getNombre()).append(",\n\n");
        texto.append("Te informamos que tu pedido ID: " + pedido.getId() +" ha avanzado.\n\n");

        texto.append("Estado actual: ").append(newState).append("\n");

        if (newState == EstadoCompra.ENVIANDO || newState == EstadoCompra.COMPLETADO) {
            texto.append("Código de seguimiento: ").append(pedido.getCodigoSeguimiento()).append("\n");
        }

        texto.append("\nSi tenés alguna consulta, podés contactarnos respondiendo a este correo.\n\n");
        texto.append("¡Gracias por confiar en Bodega Tierra Noble!");

        message.setText(texto.toString());

        mailSender.send(message);
    }

}
