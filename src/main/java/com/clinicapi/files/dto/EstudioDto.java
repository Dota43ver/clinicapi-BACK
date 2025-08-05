package com.clinicapi.files.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EstudioDto {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private String tipoEstudio;
    private LocalDate fecha;
    private String resultados;
}
