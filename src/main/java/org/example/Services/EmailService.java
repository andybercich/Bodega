package org.example.Services;

import jakarta.mail.internet.MimeMessage;
import org.example.Entities.Compra;
import org.example.Entities.Dto.CompraDTO;
import org.example.Entities.Dto.ProblemFormDTO;
import org.example.Entities.Dto.UsuarioDTO;
import org.example.Entities.Usuario;
import org.example.Repositories.CompraRepository;
import org.example.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.to}")
    private String toEmail;

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private CompraRepository compraRepository;

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
}
