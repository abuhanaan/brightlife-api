package com.fronteers.services;

import com.fronteers.models.Holiday;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HolidayService {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String API_URL = "https://date.nager.at/Api/v2/PublicHolidays/%d/US";

  public List<Holiday> getUSHolidays(int year) {
    String url = String.format(API_URL, year);
    ResponseEntity<Holiday[]> response = restTemplate.getForEntity(url, Holiday[].class);
    return Arrays.asList(Objects.requireNonNull(response.getBody()));
  }
}

