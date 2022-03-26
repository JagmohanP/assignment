package com.assignment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper POJO class for the response output for country list related API call.
 */
@Getter
@Setter
@AllArgsConstructor
public class CountryList {

    private List<CountryDetails> countries;
}
