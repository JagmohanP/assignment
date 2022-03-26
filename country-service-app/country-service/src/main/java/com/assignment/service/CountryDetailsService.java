package com.assignment.service;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

public interface CountryDetailsService {

    /**
     * Get name and code for all the countries
     *
     * @return all the countries name and code.
     */

    CountryList getAllCountries();

    /**
     * Get the country details.
     *
     * @param countryName country name
     * @return Country details
     */
    CountryDetails getDetailsByCountryName(String countryName);
}
