package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.Appointment;
import com.fronteers.models.entity.AppointmentEntity;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class AppointmentMapper {

  public static Appointment mapAppointmentEntityToDto(AppointmentEntity appointmentEntity) {
    Appointment appointment = new Appointment();
    appointment.setId(appointmentEntity.getId());
    appointment.patientId(appointmentEntity.getPatient() != null ? UUID.fromString(
        appointmentEntity.getPatient().getPatientId()) : null);
    appointment.setIsNew(appointmentEntity.getPatient() != null);
    appointment.setVerificationStatus(appointmentEntity.getVerificationStatus());
    appointment.setFirstName(appointmentEntity.getFirstName());
    appointment.setMiddleName(appointmentEntity.getMiddleName());
    appointment.setLastName(appointmentEntity.getLastName());
    appointment.setGender(appointmentEntity.getGender());
    appointment.setDob(
        appointmentEntity.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    appointment.setPhone(appointmentEntity.getPhone());
    appointment.setEmail(appointmentEntity.getEmail());
    appointment.setAddress(
        PatientDtoMapper.mapAddressEntityToAddressDto(appointmentEntity.getAddress()));
    appointment.setAppointmentType(appointmentEntity.getAppointmentType());
    appointment.setService(appointmentEntity.getService());
    appointment.setAppointmentDateTime(appointmentEntity.getAppointmentDateTime());
    appointment.setPurpose(appointmentEntity.getPurpose());
    appointment.setPaymentMethod(appointmentEntity.getPaymentMethod());
    appointment.setInsuranceName(appointmentEntity.getInsuranceName());
    appointment.setInsuranceNumber(appointmentEntity.getInsuranceNumber());
    return appointment;
  }

  public static List<Appointment> mapAppointmentEntitiesToDto(
      List<AppointmentEntity> appointmentEntities) {
    return appointmentEntities.stream().map(AppointmentMapper::mapAppointmentEntityToDto).toList();
  }
}
