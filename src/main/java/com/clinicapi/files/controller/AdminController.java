package com.clinicapi.files.controller;

import com.clinicapi.files.dto.DoctorDto;
import com.clinicapi.files.entity.Doctor;
import com.clinicapi.files.model.Role;
import com.clinicapi.files.repository.DoctorRepository;
import com.clinicapi.files.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;


    @PostMapping("/doctores")
    public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setNombreCompleto(doctorDto.getNombreCompleto());
        doctor.setEspecialidad(doctorDto.getEspecialidad());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(mapToDoctorDto(savedDoctor));
    }

    @GetMapping("/doctores")
    public ResponseEntity<List<DoctorDto>> getAllDoctores() {
        List<DoctorDto> doctores = doctorRepository.findAll().stream()
                .map(this::mapToDoctorDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctores);
    }

    @PutMapping("/doctores/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor no encontrado"));
        doctor.setNombreCompleto(doctorDto.getNombreCompleto());
        doctor.setEspecialidad(doctorDto.getEspecialidad());
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(mapToDoctorDto(updatedDoctor));
    }

    @DeleteMapping("/doctores/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor no encontrado");
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/usuarios/{id}/grant-admin")
    public ResponseEntity<?> grantAdminRole(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return ResponseEntity.ok("Rol de administrador otorgado al usuario " + user.getEmail());
    }

    private DoctorDto mapToDoctorDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setNombreCompleto(doctor.getNombreCompleto());
        dto.setEspecialidad(doctor.getEspecialidad());
        return dto;
    }
}
