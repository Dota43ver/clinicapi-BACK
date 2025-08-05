package com.clinicapi.files.repository;

import com.clinicapi.files.entity.ConsultaMedica;
import com.clinicapi.files.entity.Doctor;
import com.clinicapi.files.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedica, Long> {
    List<ConsultaMedica> findByPaciente(User paciente);

    @Query("SELECT cm FROM ConsultaMedica cm WHERE cm.doctor = :doctor AND cm.fechaHora >= :start AND cm.fechaHora < :end")
    List<ConsultaMedica> findDoctorAppointmentsInTimeRange(
            @Param("doctor") Doctor doctor,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}