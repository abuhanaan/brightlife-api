package com.fronteers.services;

import com.fronteers.brightlife.model.Appointment;
import com.fronteers.brightlife.model.AppointmentSearch;
import com.fronteers.brightlife.model.PaginatedAppointments;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TimeSlot;
import com.fronteers.brightlife.model.TimeSlots;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.entity.AppointmentEntity;
import com.fronteers.models.entity.QAppointmentEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.repositories.AppointmentRepository;
import com.fronteers.services.mappers.AppointmentMapper;
import com.querydsl.core.BooleanBuilder;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final PatientService patientService;

  // U.S. Federal Holidays (Static for demo, can be dynamically fetched from an API)
  private static final Set<LocalDate> US_HOLIDAYS = Set.of(
      LocalDate.of(2025, 1, 1),  // New Year's Day
      LocalDate.of(2025, 7, 4),  // Independence Day
      LocalDate.of(2025, 12, 25) // Christmas Day
  );

  public Success submit(Appointment request) {
    OffsetDateTime appointmentDateTime = request.getAppointmentDateTime();
    LocalDate appointmentDate = appointmentDateTime.toLocalDate();
    int appointmentHour = appointmentDateTime.getHour();

    // 1. Check if the appointment is on a weekend
    if (isWeekend(appointmentDate)) {
      throw new IllegalArgumentException("Appointments cannot be booked on weekends.");
    }

    // 2. Check if the appointment is on a public holiday
    if (US_HOLIDAYS.contains(appointmentDate)) {
      throw new IllegalArgumentException("Appointments cannot be booked on public holidays.");
    }

    // 3. Check if the appointment is within operating hours (9 AM - 5 PM)
    if (appointmentHour < 9 || appointmentHour >= 17) {
      throw new IllegalArgumentException("Appointments must be between 9 AM and 5 PM.");
    }

    // 4. Check if the appointment time slot is already taken
    if (appointmentRepository.existsByAppointmentDateTime(appointmentDateTime)) {
      throw new IllegalArgumentException("This appointment slot is already taken.");
    }

    // 5. Create new appointment entity
    AppointmentEntity appointmentEntity = new AppointmentEntity();
    PatientRegistrationFormEntity patientRegistrationFormEntity = null;

    if (request.getPatientId() != null) {
      patientRegistrationFormEntity = patientService.checkIfPatientExist(request.getPatientId().toString());
    }
    if (patientRegistrationFormEntity != null) {
      mapOldPatientAppointmentDetails(appointmentEntity, request, patientRegistrationFormEntity, appointmentDateTime);
    } else {
      mapNewPatientAppointmentDetails(appointmentEntity, request, appointmentDateTime);
    }

    appointmentRepository.save(appointmentEntity);
    return new Success(true, "Appointment Submitted Successfully",
        String.format("Appointment id: %s, Appointment time: %s",
            appointmentEntity.getId(), appointmentEntity.getAppointmentDateTime()));
  }

  public TimeSlots getAppointmentTimeSlots() {
    LocalDate today = LocalDate.now();
    LocalDate threeMonthsLater = today.plusMonths(3);

    // Convert LocalDate to OffsetDateTime at start of the day (09:00 AM as per your conditions)
    OffsetDateTime startDateTime = today.atTime(9, 0).atOffset(ZoneOffset.UTC);
    OffsetDateTime endDateTime = threeMonthsLater.atTime(17, 0).atOffset(ZoneOffset.UTC);

    // Fetch appointments within the next 3 months using OffsetDateTime
    List<AppointmentEntity> appointments = appointmentRepository.findAppointmentsBetween(startDateTime, endDateTime);

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

  private void mapOldPatientAppointmentDetails(AppointmentEntity appointmentEntity, Appointment request, PatientRegistrationFormEntity patientRegFormEntity, OffsetDateTime appointmentDateTime) {
    appointmentEntity.setPatient(patientRegFormEntity.getPatient());
    appointmentEntity.setIsNew(false);
    appointmentEntity.setVerificationStatus(request.getVerificationStatus());
    appointmentEntity.setFirstName(patientRegFormEntity.getFirstName());
    appointmentEntity.setLastName(patientRegFormEntity.getLastName());
    appointmentEntity.setGender(patientRegFormEntity.getGender());
    appointmentEntity.setDob(patientRegFormEntity.getDob());
    appointmentEntity.setPhone(request.getPhone());
    appointmentEntity.setEmail(patientRegFormEntity.getEmail());
    appointmentEntity.setAddress(patientRegFormEntity.getAddress());
    appointmentEntity.setAppointmentType(request.getAppointmentType());
    appointmentEntity.setService(request.getService());
    appointmentEntity.setAppointmentDateTime(appointmentDateTime);
    appointmentEntity.setPurpose(request.getPurpose());
    appointmentEntity.setPaymentMethod(request.getPaymentMethod());
    appointmentEntity.setInsuranceName(request.getInsuranceName());
    appointmentEntity.setInsuranceNumber(request.getInsuranceNumber());
  }

  private void mapNewPatientAppointmentDetails(AppointmentEntity appointmentEntity, Appointment request, OffsetDateTime appointmentDateTime) {
    appointmentEntity.setIsNew(true);
    appointmentEntity.setVerificationStatus(request.getVerificationStatus());
    appointmentEntity.setFirstName(request.getFirstName());
    appointmentEntity.setLastName(request.getLastName());
    appointmentEntity.setGender(request.getGender());
    appointmentEntity.setDob(Date.valueOf(request.getDob()));
    appointmentEntity.setPhone(request.getPhone());
    appointmentEntity.setEmail(request.getEmail());
    appointmentEntity.setAddress(patientService.mapAddressProperties(request.getAddress()));
    appointmentEntity.setAppointmentType(request.getAppointmentType());
    appointmentEntity.setService(request.getService());
    appointmentEntity.setAppointmentDateTime(appointmentDateTime);
    appointmentEntity.setPurpose(request.getPurpose());
    appointmentEntity.setPaymentMethod(request.getPaymentMethod());
    appointmentEntity.setInsuranceName(request.getInsuranceName());
    appointmentEntity.setInsuranceNumber(request.getInsuranceNumber());
  }

  /**
   * Checks if the given date falls on a weekend.
   */
  private boolean isWeekend(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
  }

  private AppointmentEntity checkIfAppointmentExists(Long appointmentId) {
    AppointmentEntity appointment = appointmentRepository.findOneById(appointmentId);
    if (appointment == null){
      throw new BadRequestException(String.format("Appointment with id %s does not exist", appointmentId));
    }
    return appointment;
  }

  private TimeSlot mapToTimeSlot(LocalDate date, List<String> slots){
    TimeSlot timeSlot = new TimeSlot();
    timeSlot.setDate(date);
    timeSlot.setDaySlots(slots);
    return timeSlot;
  }

  public PaginatedAppointments searchAppointment(Integer pageNumber, Integer limit, AppointmentSearch searchCriteria) {
    int maxLimit = (limit == null || limit > 100) ? 100 : limit;
    int currentPage = (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1; // Adjust for 0-based indexing
    int safeLimit = Math.max(1, Math.min(maxLimit, 100));

    BooleanBuilder predicate = new BooleanBuilder();
    QAppointmentEntity qAppointment = QAppointmentEntity.appointmentEntity;

    if (searchCriteria.getFirstName() != null) {
      predicate.and(qAppointment.firstName.eq(searchCriteria.getFirstName()));
    }
    if (searchCriteria.getLastName() != null) {
      predicate.and(qAppointment.lastName.eq(searchCriteria.getLastName()));
    }
    if (searchCriteria.getMiddleName() != null) {
      predicate.and(qAppointment.middleName.eq(searchCriteria.getMiddleName()));
    }
    if (searchCriteria.getDob() != null) {
      predicate.and(qAppointment.dob.eq(Date.valueOf(searchCriteria.getDob())));
    }
    if (searchCriteria.getPhone() != null) {
      predicate.and(qAppointment.phone.eq(searchCriteria.getPhone()));
    }
    if (searchCriteria.getEmail() != null) {
      predicate.and(qAppointment.email.eq(searchCriteria.getEmail()));
    }
    if (searchCriteria.getGender() != null) {
      predicate.and(qAppointment.gender.eq(searchCriteria.getGender()));
    }
    if (searchCriteria.getCity() != null) {
      predicate.and(qAppointment.address.city.eq(searchCriteria.getCity()));
    }
    if (searchCriteria.getState() != null) {
      predicate.and(qAppointment.address.state.eq(searchCriteria.getState()));
    }
    if (searchCriteria.getAppointmentDateTime() != null) {
      predicate.and(qAppointment.appointmentDateTime.eq(searchCriteria.getAppointmentDateTime()));
    }
    if (searchCriteria.getPaymentMode() != null) {
      predicate.and(qAppointment.paymentMethod.eq(searchCriteria.getPaymentMode()));
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
}
