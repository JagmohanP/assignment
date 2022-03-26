package com.assignment.detailsprovider;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

/**
 * Deals with the external country details provider server to fetch the required
 * details. <br>
 * This external server to get country details can be changed any time
 * as it's internal to the service. All such external providers are thus needed
 * to be accessed only via the provided interface.
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
