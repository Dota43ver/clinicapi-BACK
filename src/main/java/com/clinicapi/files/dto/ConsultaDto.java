package com.clinicapi.files.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaDto {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;

    @NotNull(message = "El ID del doctor es obligatorio.")
    private Long doctorId;
    private String doctorNombre;
    private String doctorEspecialidad;

    @NotNull(message = "La fecha y hora son obligatorias.")
    @Future(message = "La fecha de la consulta debe ser en el futuro.")
    private LocalDateTime fechaHora;

    @NotEmpty(message = "El motivo de la consulta no puede estar vacío.")
    private String motivo;
}
