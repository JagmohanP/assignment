package com.assignment.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import com.assignment.detailsprovider.Country;
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

    public static String loadAllCountriesDataFromJson()
            throws Exception {
        return jsonDataAsString(ALL_COUNTRIES_JSON);
    }

    public static String loadCountryDataFromJson(final String countryName)
            throws Exception {
        return jsonDataAsString(countryName);
    }

    public static String jsonDataAsString(final String countryName) {
        final Resource resource = new ClassPathResource(countryName);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Country[] toCountryArray(final String countryName)
            throws Exception {
        return mapper.readValue(jsonDataAsString(countryName), Country[].class);
    }

    public static Country toCountry(final String countryName)
            throws Exception {
        final Country[] countryArray = toCountryArray(countryName);
        if (countryArray.length > 0) {
            return countryArray[0];
        }
        return null;
    }

    public static List<CountryDetails> toCountryDetailsList(final String countryName)
            throws Exception {
        final Country[] countryArr = toCountryArray(countryName);

        final List<CountryDetails> countries = Arrays.stream(countryArr)
                .map(c -> new CountryDetails(c.getName(), c.getCountryCode(), c.getCapital(), c.getPopulation(), c.getFlag()))
                .collect(Collectors.toList());
        return countries;
    }

    public static CountryDetails toCountryDetails(final String countryName)
            throws Exception {

        final List<CountryDetails> countries = toCountryDetailsList(countryName);
        return countries.size() > 0 ? countries.get(0) : null;
    }

    public static CountryList toCountryList(final String countryName)
            throws Exception {

        final List<CountryDetails> countries = toCountryDetailsList(countryName);
        return new CountryList(countries);
    }
}
