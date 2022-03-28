package com.assignment.detailsprovider;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

/**
 * Deals with the external country details provider server to fetch the required
 * country details. <br>
 * Application can choose a different provider in future. So, any such new
 * providers need to stick to this common interface and ensure, all the required
 * data is provided to the system.
 *
 */
public interface CountryDetailsProvider {

    /**
     * Get name and code for all the countries
     *
     * @return list of countries having country name and country code
     */

    CountryList getAllCountries();

    /**
     * Get the country details.
     *
     * @param countryName country name
     * @return Country details like name, country code, capital, population and
     *         flag image url
     */
    CountryDetails getDetailsByCountryName(String countryName);

}
