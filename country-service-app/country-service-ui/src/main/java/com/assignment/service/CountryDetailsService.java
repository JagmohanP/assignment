package com.assignment.service;

import java.util.Map;

import com.assignment.dto.CountryDetails;

public interface CountryDetailsService {

    /**
     * Get name and code for all the countries
     *
     * @return all the countries name and code.
     */

    Map<String, String> getAllCountries();

    /**
     * Get the country details.
     *
     * @param countryName country name
     * @return Country details
     */
    CountryDetails getDetailsByCountryName(String countryName);
}
