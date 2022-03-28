package com.assignment.service;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

/**
 * Interface defining methods to fetch country details.
 */
public interface CountryDetailsService {

    /**
     * Get name and code for all the countries
     *
     * @return all the countries name and code.
     */

    CountryList getAllCountries();

    /**
     * Get various country details like name, code, capital, flag and population
     * for a given country.
     *
     * @param countryName country name
     * @return Country details
     */
    CountryDetails getDetailsByCountryName(String countryName);
}
