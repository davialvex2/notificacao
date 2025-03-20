package com.daviaugusto.notificacao.controller;

import com.daviaugusto.notificacao.business.EmailService;
import com.daviaugusto.notificacao.dto.TarefaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {


    private final EmailService emailService;


    @PostMapping
    public ResponseEntity<Void> enviarEmail(@RequestBody TarefaDTO tarefaDTO){
        emailService.enviaEmail(tarefaDTO);
        return ResponseEntity.ok().build();
    }
}
