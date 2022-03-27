package com.assignment.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import com.assignment.detailsprovider.Country;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static final String ALL_COUNTRIES_JSON = "all-countries.json";
    public static final String COUNTRY_FINLAND_JSON = "country-finland.json";

    private TestUtils() {

    }

    public static final ObjectMapper mapper = new ObjectMapper();

    public static CountryList loadAllCountriesDataFromJson()
            throws Exception {

        final List<CountryDetails> countries = parseCountriesDataFromJson(ALL_COUNTRIES_JSON);
        return new CountryList(countries);
    }

    public static CountryDetails loadCountryDataFromJson(final String countryName)
            throws Exception {
        final List<CountryDetails> countries = parseCountriesDataFromJson(countryName);
        return countries.get(0);
    }

    public static List<CountryDetails> parseCountriesDataFromJson(final String countryName)
            throws Exception {

        final Country[] countryArr = mapper.readValue(
                new ClassPathResource(countryName).getFile(),
                Country[].class);

        final List<CountryDetails> countries = Arrays.stream(countryArr)
                .map(c -> new CountryDetails(c.getName(), c.getCountryCode(), c.getCapital(), c.getPopulation(), c.getFlag()))
                .collect(Collectors.toList());

        return countries;
    }
}
