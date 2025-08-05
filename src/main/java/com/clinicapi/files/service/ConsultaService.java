package com.clinicapi.files.service;

import com.clinicapi.files.dto.ConsultaDto;
import com.clinicapi.files.entity.ConsultaMedica;
import com.clinicapi.files.entity.Doctor;
import com.clinicapi.files.entity.User;
import com.clinicapi.files.repository.ConsultaMedicaRepository;
import com.clinicapi.files.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaMedicaRepository consultaRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public ConsultaDto agendarConsulta(ConsultaDto consultaDto, User paciente) {
        Doctor doctor = doctorRepository.findById(consultaDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado con ID: " + consultaDto.getDoctorId()));

        LocalDateTime fechaHora = consultaDto.getFechaHora();

        // 1. Validar horario de la clínica
        validarHorarioClinica(fechaHora);

        // 2. Validar límite de agendamiento
        validarLimiteAgendamiento(fechaHora);

        // 3. Validar conflicto de horarios del doctor
        validarConflictoHorarioDoctor(doctor, fechaHora);

        ConsultaMedica nuevaConsulta = new ConsultaMedica();
        nuevaConsulta.setPaciente(paciente);
        nuevaConsulta.setDoctor(doctor);
        nuevaConsulta.setFechaHora(fechaHora);
        nuevaConsulta.setMotivo(consultaDto.getMotivo());

        ConsultaMedica consultaGuardada = consultaRepository.save(nuevaConsulta);
        return mapToDto(consultaGuardada);
    }

    public List<ConsultaDto> findConsultasByPaciente(User paciente) {
        return consultaRepository.findByPaciente(paciente).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private void validarHorarioClinica(LocalDateTime fechaHora) {
        DayOfWeek dia = fechaHora.getDayOfWeek();
        LocalTime hora = fechaHora.toLocalTime();

        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Las consultas solo pueden agendarse de lunes a viernes.");
        }
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(19, 0))) {
            throw new IllegalArgumentException("El horario de la clínica es de 8:00 AM a 7:00 PM.");
        }
    }

    private void validarLimiteAgendamiento(LocalDateTime fechaHora) {
        if (fechaHora.toLocalTime().isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("No se puede agendar una consulta después de las 6:00 PM. La última cita es a las 18:00.");
        }
    }

    private void validarConflictoHorarioDoctor(Doctor doctor, LocalDateTime fechaHora) {
        LocalDateTime unaHoraAntes = fechaHora.minusHours(1);
        LocalDateTime unaHoraDespues = fechaHora.plusHours(1);

        List<ConsultaMedica> conflictos = consultaRepository.findDoctorAppointmentsInTimeRange(doctor, unaHoraAntes, unaHoraDespues);
        if (!conflictos.isEmpty()) {
            throw new IllegalStateException("El doctor ya tiene una consulta programada en un horario conflictivo.");
        }
    }

    private ConsultaDto mapToDto(ConsultaMedica consulta) {
        ConsultaDto dto = new ConsultaDto();
        dto.setId(consulta.getId());
        dto.setPacienteId(consulta.getPaciente().getId());
        dto.setPacienteNombre(consulta.getPaciente().getNombreCompleto());
        dto.setDoctorId(consulta.getDoctor().getId());
        dto.setDoctorNombre(consulta.getDoctor().getNombreCompleto());
        dto.setDoctorEspecialidad(consulta.getDoctor().getEspecialidad().name());
        dto.setFechaHora(consulta.getFechaHora());
        dto.setMotivo(consulta.getMotivo());
        return dto;
    }
}
