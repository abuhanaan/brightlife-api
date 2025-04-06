package com.fronteers.repositories;

import com.fronteers.models.entity.AppointmentEntity;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long>,
    QuerydslPredicateExecutor<AppointmentEntity> {

  AppointmentEntity findOneById(Long id);

  AppointmentEntity findByEmailAndAppointmentDateTime(String email,
      OffsetDateTime appointmentDateTime);

  boolean existsByAppointmentDateTime(OffsetDateTime appointmentDateTime);

  List<AppointmentEntity> findByPatient_PatientId(String patientId);

  @Query("SELECT a FROM AppointmentEntity a WHERE a.appointmentDateTime BETWEEN :start AND :end")
  List<AppointmentEntity> findAppointmentsBetween(@Param("start") OffsetDateTime start,
      @Param("end") OffsetDateTime end);
}
