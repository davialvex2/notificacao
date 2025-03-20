package com.daviaugusto.notificacao.business;

import com.daviaugusto.notificacao.dto.TarefaDTO;
import com.daviaugusto.notificacao.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Value("${envio.email.remetente}")
    public String remetente;

    @Value("${envio.email.nomeRemetente}")
    public String nomeRemetente;

    public void enviaEmail(TarefaDTO tarefaDTO){

        try{
            //Instancionado MineMessage para usar o javaMailSender
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            //Instanciando MimeMessageHelper passando MimeMessage(mensagem)
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            // adicionando ao MimemessageHelper remetente que vai enviar o email
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            // adcionando para qual email será enviado
            mimeMessageHelper.setTo(InternetAddress.parse(tarefaDTO.getEmailUsuario()));
            //adcionando o assunto do email
            mimeMessageHelper.setSubject("Notificação de tarefa");

            //instanciando o Context, ele que usa o templete em htlm para que seja feito o corpo do email com as variáveis
            Context context = new Context();
            //inserindo no email o nome da terefa
            context.setVariable("nomeTarefa", tarefaDTO.getNomeTarefa());
            //inserindo a data do evento
            context.setVariable("dataEvento", tarefaDTO.getDataEvento());
            //inserindo a descrição da tarefa
            context.setVariable("descricao", tarefaDTO.getDescricao());

            //armazenando em uma variável string o email, usando a injeção de dependencia do templeteEngine
            // processsando com o templete em htlm e o context que possui as variáveis
            String templete = templateEngine.process("notificacao", context);
            //inserindo no MimemessageHelper o templete pronto
            mimeMessageHelper.setText(templete, true);
            //enviando o email
            javaMailSender.send(mensagem);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Falha ao enviar email", e.getCause());
        }
    }

}
