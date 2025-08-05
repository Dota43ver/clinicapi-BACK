package com.clinicapi.files.dto;


import com.clinicapi.files.model.Especialidad;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorDto {
    private Long id;

    @NotEmpty(message = "El nombre del doctor no puede estar vacío.")
    private String nombreCompleto;

    @NotNull(message = "La especialidad no puede ser nula.")
    private Especialidad especialidad;
}
