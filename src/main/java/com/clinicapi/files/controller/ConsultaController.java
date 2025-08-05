package com.clinicapi.files.controller;

import com.clinicapi.files.dto.ConsultaDto;
import com.clinicapi.files.entity.User;
import com.clinicapi.files.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping("/agendar")
    public ResponseEntity<ConsultaDto> agendarConsulta(@Valid @RequestBody ConsultaDto consultaDto, @AuthenticationPrincipal User paciente) {
        ConsultaDto consultaAgendada = consultaService.agendarConsulta(consultaDto, paciente);
        return new ResponseEntity<>(consultaAgendada, HttpStatus.CREATED);
    }

    @GetMapping("/mis-consultas")
    public ResponseEntity<List<ConsultaDto>> getMisConsultas(@AuthenticationPrincipal User paciente) {
        List<ConsultaDto> misConsultas = consultaService.findConsultasByPaciente(paciente);
        return ResponseEntity.ok(misConsultas);
    }
}