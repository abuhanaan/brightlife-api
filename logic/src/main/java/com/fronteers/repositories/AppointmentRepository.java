package com.fronteers.repositories;

import com.fronteers.models.entity.AppointmentEntity;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

  AppointmentEntity findOneById(Long id);

  AppointmentEntity findByEmailAndAppointmentDateTime(String email, OffsetDateTime appointmentDateTime);

  boolean existsByAppointmentDateTime(OffsetDateTime appointmentDateTime);

  List<AppointmentEntity> findByPatient_PatientId(String patientId);
}
