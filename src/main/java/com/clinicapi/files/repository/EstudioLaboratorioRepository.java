package com.clinicapi.files.repository;

import com.clinicapi.files.entity.EstudioLaboratorio;
import com.clinicapi.files.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstudioLaboratorioRepository extends JpaRepository<EstudioLaboratorio, Long> {
    List<EstudioLaboratorio> findByPaciente(User paciente);
}