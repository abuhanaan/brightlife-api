package com.fronteers.services;

import com.fronteers.brightlife.model.Appointment;
import com.fronteers.brightlife.model.AppointmentSearch;
import com.fronteers.brightlife.model.AppointmentStatusEnum;
import com.fronteers.brightlife.model.PaginatedAppointments;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TimeSlot;
import com.fronteers.brightlife.model.TimeSlots;
import com.fronteers.brightlife.model.UpdateAppointmentStatus;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.models.entity.AppointmentEntity;
import com.fronteers.models.entity.QAppointmentEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.mappers.AppointmentMapper;
import com.fronteers.repositories.AppointmentRepository;
import com.fronteers.repositories.PatientRegistrationFormRepository;
import com.fronteers.utils.CopyBeanUtil;
import com.fronteers.utils.PatientUtils;
import com.querydsl.core.BooleanBuilder;
import jakarta.mail.MessagingException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

  // U.S. Federal Holidays (Static for demo, can be dynamically fetched from an API)
  private static final Set<LocalDate> US_HOLIDAYS = Set.of(
      LocalDate.of(2025, 1, 1),  // New Year's Day
      LocalDate.of(2025, 7, 4),  // Independence Day
      LocalDate.of(2025, 12, 25) // Christmas Day
  );
  private final AppointmentRepository appointmentRepository;
  private final PatientService patientService;
  private final PatientUtils patientUtils;
  private final PatientRegistrationFormRepository regRepository;
  private final EmailService emailService;
  @Value("${fe.base.url}")
  private String feBaseUrl;

  public Success updateAppointment(Appointment request) {
    AppointmentEntity existingAppointment = checkIfAppointmentExists(request.getId());
    AppointmentEntity appointmentToUpdate = new AppointmentEntity();
    prepareAppointmentForSave(appointmentToUpdate, request, true);
    appointmentToUpdate.setId(null);
    CopyBeanUtil.copyNonNullProperties(appointmentToUpdate, existingAppointment);
    existingAppointment = appointmentRepository.save(existingAppointment);
    return new Success(true, "Appointment Updated Successfully",
        String.format("Appointment id: %s, Appointment time: %s",
            existingAppointment.getId(), existingAppointment.getAppointmentDateTime()));
  }

  public Success submit(Appointment request) {
    AppointmentEntity appointmentEntity = new AppointmentEntity();
    prepareAppointmentForSave(appointmentEntity, request, false);
    appointmentEntity.setStatus(AppointmentStatusEnum.UPCOMING);
    appointmentRepository.save(appointmentEntity);
    notifyAdminAndPatient(appointmentEntity);
    return new Success(true, "Appointment Submitted Successfully",
        String.format("Appointment id: %s, Appointment time: %s",
            appointmentEntity.getId(), appointmentEntity.getAppointmentDateTime()));
  }

  private void notifyAdminAndPatient(AppointmentEntity appointmentEntity) {
    Map<String, Object> variables = Map.of(
        "name", appointmentEntity.getFirstName() + " " + appointmentEntity.getLastName(),
        "email", appointmentEntity.getEmail(),
        "formType", "Intake Form",
        "appointmentUrl", feBaseUrl + "/appointments/" + appointmentEntity.getPatient().getPatientId());
    try {
      emailService.sendEmail("fronteers.dev@gmail.com", "Intake Form Submission Details",
          "admin-form-submission-notification", variables);
      emailService.sendEmail(appointmentEntity.getEmail(), "Acknowledgement",
          "patient-appointment-template.html", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
  }

  public Success changeAppointmentStatus(Long id, UpdateAppointmentStatus request) {
    AppointmentEntity appointmentEntity = checkIfAppointmentExists(id);
    if (appointmentEntity.getStatus().equals(request.getStatus())) {
      throw new ConflictException("Appointment status is already set to " + request.getStatus());
    }
    appointmentEntity.setStatus(request.getStatus());
    appointmentRepository.save(appointmentEntity);
    return new Success(true, "Appointment Status Updated Successfully",
        String.format("Appointment status has been successfully updated to %s successfully",
            request.getStatus()));
  }

  public TimeSlots getAppointmentTimeSlots() {
    LocalDate today = LocalDate.now();
    LocalDate threeMonthsLater = today.plusMonths(3);

    // Convert LocalDate to OffsetDateTime at start of the day (09:00 AM) and end of the day of the end Date
    OffsetDateTime startDateTime = today.atTime(9, 0).atOffset(ZoneOffset.UTC);
    OffsetDateTime endDateTime = threeMonthsLater.atTime(17, 0).atOffset(ZoneOffset.UTC);

    // Fetch appointments within the next 3 months using OffsetDateTime
    List<AppointmentEntity> appointments = appointmentRepository.findAppointmentsBetween(
        startDateTime, endDateTime);

    // Use a TreeMap to store and group appointments by date in sorted order
    Map<LocalDate, List<String>> slotMap = new TreeMap<>();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    for (AppointmentEntity appointment : appointments) {
      LocalDate date = appointment.getAppointmentDateTime().toLocalDate();
      LocalTime time = appointment.getAppointmentDateTime().toLocalTime();

      slotMap.computeIfAbsent(date, k -> new ArrayList<>()).add(time.format(timeFormatter));
    }

    // Convert slotMap to a List of TimeSlot objects
    List<TimeSlot> timeSlots = slotMap.entrySet().stream()
        .map(entry -> mapToTimeSlot(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    TimeSlots response = new TimeSlots();
    response.setSlots(timeSlots);
    return response;
  }

  public Appointment getAppointment(Long appointmentId) {
    AppointmentEntity appointmentEntity = checkIfAppointmentExists(appointmentId);
    return AppointmentMapper.mapAppointmentEntityToDto(appointmentEntity);
  }

  public PaginatedAppointments searchAppointment(Integer pageNumber, Integer limit,
      AppointmentSearch searchCriteria) {
    int maxLimit = (limit == null || limit > 100) ? 100 : limit;
    int currentPage =
        (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1; // Adjust for 0-based indexing
    int safeLimit = Math.max(1, Math.min(maxLimit, 100));

    BooleanBuilder predicate = new BooleanBuilder();
    QAppointmentEntity qAppointment = QAppointmentEntity.appointmentEntity;

    if (searchCriteria.getPatientId() != null) {
      predicate.or(qAppointment.patient.patientId.eq(searchCriteria.getPatientId().toString()));
    }
    if (searchCriteria.getFirstName() != null) {
      predicate.or(qAppointment.firstName.eq(searchCriteria.getFirstName()));
    }
    if (searchCriteria.getLastName() != null) {
      predicate.or(qAppointment.lastName.eq(searchCriteria.getLastName()));
    }
    if (searchCriteria.getMiddleName() != null) {
      predicate.or(qAppointment.middleName.eq(searchCriteria.getMiddleName()));
    }
    if (searchCriteria.getDob() != null) {
      predicate.or(qAppointment.dob.eq(Date.valueOf(searchCriteria.getDob())));
    }
    if (searchCriteria.getPhone() != null) {
      predicate.or(qAppointment.phone.eq(searchCriteria.getPhone()));
    }
    if (searchCriteria.getEmail() != null) {
      predicate.or(qAppointment.email.eq(searchCriteria.getEmail()));
    }
    if (searchCriteria.getGender() != null) {
      predicate.or(qAppointment.gender.eq(searchCriteria.getGender()));
    }
    if (searchCriteria.getCity() != null) {
      predicate.or(qAppointment.address.city.eq(searchCriteria.getCity()));
    }
    if (searchCriteria.getState() != null) {
      predicate.or(qAppointment.address.state.eq(searchCriteria.getState()));
    }
    if (searchCriteria.getAppointmentDateTime() != null) {
      predicate.or(qAppointment.appointmentDateTime.eq(searchCriteria.getAppointmentDateTime()));
    }

    // Fetch all records if no filters are applied
    if (predicate.getValue() == null) {
      log.info("No search filters applied, fetching all records");
      predicate.and(qAppointment.id.isNotNull());
    }
    Pageable pageable = PageRequest.of(currentPage, safeLimit);
    Page<AppointmentEntity> appointmentPage = appointmentRepository.findAll(predicate, pageable);
    List<AppointmentEntity> entityList = appointmentPage.getContent();
    List<Appointment> appointmentDtos = AppointmentMapper.mapAppointmentEntitiesToDto(entityList);

    PaginatedAppointments response = new PaginatedAppointments();
    response.setAppointments(appointmentDtos);
    response.setCurrentPage(appointmentPage.getNumber() + 1);
    response.setItemsPerPage(appointmentPage.getSize());
    response.setTotalPages(appointmentPage.getTotalPages());
    return response;
  }

  private void prepareAppointmentForSave(AppointmentEntity appointmentEntity, Appointment request,
      boolean isUpdateRequest) {
    OffsetDateTime appointmentDateTime = request.getAppointmentDateTime();
    validateAppointmentDateTime(appointmentDateTime);
    PatientRegistrationFormEntity patientRegistrationFormEntity = null;
    if (request.getPatientId() != null) {
      patientRegistrationFormEntity = regRepository.findByPatientId(
          request.getPatientId().toString());
    }
    if (request.getPatientId() == null && request.getEmail() != null) {
      patientRegistrationFormEntity = regRepository.findOneByEmail(request.getEmail());
    }
    if (patientRegistrationFormEntity != null) {
      mapOldPatientAppointmentDetails(appointmentEntity, request, patientRegistrationFormEntity,
          appointmentDateTime);
    } else {
      mapNewPatientAppointmentDetails(appointmentEntity, request, appointmentDateTime);
    }
    if (isUpdateRequest) {
      if (appointmentEntity.getAppointmentDateTime() != request.getAppointmentDateTime()) {
        validateAppointmentDateTime(appointmentDateTime);
      }
    } else {
      validateAppointmentDateTime(appointmentDateTime);
    }
  }

  private void validateAppointmentDateTime(OffsetDateTime appointmentDateTime) {
    if (appointmentDateTime == null) {
      return;
    }
    LocalDate appointmentDate = appointmentDateTime.toLocalDate();
    int appointmentHour = appointmentDateTime.getHour();
    // 0. Check if the appointment date is in the past
    if (appointmentDate.isBefore(LocalDate.now())) {
      throw new BadRequestException("Appointments cannot be booked for past dates.");
    }
    // 1. Check if the appointment is on a weekend
    if (isWeekend(appointmentDate)) {
      throw new BadRequestException("Appointments cannot be booked on weekends.");
    }
    // 2. Check if the appointment is on a public holiday
    if (US_HOLIDAYS.contains(appointmentDate)) {
      throw new BadRequestException("Appointments cannot be booked on public holidays.");
    }
    // 3. Check if the appointment is within operating hours (9 AM - 5 PM)
    if (appointmentHour < 9 || appointmentHour >= 17) {
      throw new BadRequestException("Appointments must be between 9 AM and 5 PM.");
    }
    // 4. Check if the appointment time slot is already taken
    if (appointmentRepository.existsByAppointmentDateTime(appointmentDateTime)) {
      throw new IllegalArgumentException("This appointment slot is already taken.");
    }
  }

  private void mapOldPatientAppointmentDetails(AppointmentEntity appointmentEntity,
      Appointment request, PatientRegistrationFormEntity patientRegFormEntity,
      OffsetDateTime appointmentDateTime) {
    appointmentEntity.setPatient(patientRegFormEntity.getPatient());
    appointmentEntity.setIsNew(false);
    appointmentEntity.setFirstName(patientRegFormEntity.getFirstName());
    appointmentEntity.setLastName(patientRegFormEntity.getLastName());
    appointmentEntity.setGender(patientRegFormEntity.getGender());
    appointmentEntity.setDob(patientRegFormEntity.getDob());
    appointmentEntity.setPhone(patientRegFormEntity.getCellPhone());
    appointmentEntity.setEmail(patientRegFormEntity.getEmail());
    appointmentEntity.setAddress(patientRegFormEntity.getAddress());
    appointmentEntity.setAppointmentDateTime(appointmentDateTime);
    appointmentEntity.setPaymentMethod(patientRegFormEntity.getPaymentMode());
    setCommonAppointmentProperties(appointmentEntity, request);
  }

  private void setCommonAppointmentProperties(AppointmentEntity appointmentEntity,
      Appointment request) {
    appointmentEntity.setVerificationStatus(request.getVerificationStatus());
    appointmentEntity.setAppointmentType(request.getAppointmentType());
    appointmentEntity.setService(request.getService());
    appointmentEntity.setPurpose(request.getPurpose());
    appointmentEntity.setInsuranceName(request.getInsuranceName());
    appointmentEntity.setInsuranceNumber(request.getInsuranceNumber());
  }

  private void mapNewPatientAppointmentDetails(AppointmentEntity appointmentEntity,
      Appointment request, OffsetDateTime appointmentDateTime) {
    appointmentEntity.setIsNew(true);
    setCommonAppointmentProperties(appointmentEntity, request);
    appointmentEntity.setFirstName(request.getFirstName());
    appointmentEntity.setLastName(request.getLastName());
    appointmentEntity.setGender(request.getGender());
    appointmentEntity.setDob(request.getDob() != null ? Date.valueOf(request.getDob()) : null);
    appointmentEntity.setPhone(request.getPhone());
    appointmentEntity.setEmail(request.getEmail());
    appointmentEntity.setAddress(
        request.getAddress() != null ? patientUtils.mapAddressProperties(request.getAddress())
            : null);
    appointmentEntity.setAppointmentDateTime(appointmentDateTime);
    appointmentEntity.setPaymentMethod(request.getPaymentMethod());
  }

  private boolean isWeekend(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
  }

  private AppointmentEntity checkIfAppointmentExists(Long appointmentId) {
    AppointmentEntity appointment = appointmentRepository.findOneById(appointmentId);
    if (appointment == null) {
      throw new BadRequestException(
          String.format("Appointment with id %s does not exist", appointmentId));
    }
    return appointment;
  }

  private TimeSlot mapToTimeSlot(LocalDate date, List<String> slots) {
    TimeSlot timeSlot = new TimeSlot();
    timeSlot.setDate(date);
    timeSlot.setDaySlots(slots);
    return timeSlot;
  }
}
