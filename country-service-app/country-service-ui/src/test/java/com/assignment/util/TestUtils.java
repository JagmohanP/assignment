package com.assignment.util;

import org.springframework.core.io.ClassPathResource;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static final String ALL_COUNTRIES_JSON = "all-countries.json";
    public static final String COUNTRY_FINLAND_JSON = "country-finland.json";
    public static final String COUNTRY_ANTARCTICA_JSON = "country-antarctica.json";
    public static final String COUNTRY_UAE_JSON = "country-uae.json";
    public static final String COUNTRY_SOUTH_AFRICA_JSON = "country-south-africa.json";

    private TestUtils() {

    }

    public static final ObjectMapper mapper = new ObjectMapper();

    public static CountryList loadAllCountriesDataFromJson()
            throws Exception {

        final CountryList countries = mapper.readValue(
                new ClassPathResource(ALL_COUNTRIES_JSON).getFile(),
                CountryList.class);
        return countries;
    }

    public static CountryDetails loadCountryDataFromJson(final String countryName)
            throws Exception {
        final CountryDetails countryDetails = mapper.readValue(
                new ClassPathResource(countryName).getFile(),
                CountryDetails.class);
        return countryDetails;
    }
}
