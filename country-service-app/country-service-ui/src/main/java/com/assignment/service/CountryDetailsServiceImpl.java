package com.assignment.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

@Service
public class CountryDetailsServiceImpl implements CountryDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CountryDetailsServiceImpl.class);

    private final RestTemplate restTemplate;

    @Autowired
    public CountryDetailsServiceImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${countryservice.url}")
    private String countryServiceUrl;

    @Value("${countryservice.url.all-countries}")
    private String allCountriesUrl;

    @Value("${countryservice.url.country-details-by-name}")
    private String countryDetailsByNameUrl;

    /**
     * Get name and code for all the countries
     *
     * @return all the countries name and code. Key is the country name, value
     *         is the country code.
     */
    @Override
    public Map<String, String> getAllCountries() {

        log.debug("getAllCountries() : allCountriesUrl = {}" + allCountriesUrl);

        final String uri = UriComponentsBuilder.fromUriString(allCountriesUrl).toUriString();
        log.debug("GetAllCountries uri = {}", uri);

        final CountryList allCountries = restTemplate.getForObject(uri, CountryList.class);

        final Map<String, String> countriesMap = new TreeMap<String, String>();
        if ((null != allCountries) && (null != allCountries.getCountries()) && !allCountries.getCountries().isEmpty()) {

            for (final CountryDetails countryDetails : allCountries.getCountries()) {
                countriesMap.put(countryDetails.getName(), countryDetails.getCountryCode());
            }
        }
        return countriesMap;
    }

    /**
     * Get the country details.
     *
     * @param countryName country name
     * @return Country details like name, country code, capital, population and
     *         flag image url
     */
    @Override
    public CountryDetails getDetailsByCountryName(final String countryName) {

        log.debug("getDetailsByCountryName() : countryDetailsByNameUrl = {}" + countryDetailsByNameUrl);

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", countryName);

        final String uri = UriComponentsBuilder.fromUriString(countryDetailsByNameUrl)
                .buildAndExpand(urlParams)
                .toUriString();

        log.debug("GetDetailsByCountryName uri = {}", uri);

        final CountryDetails countryDetails = restTemplate.getForObject(uri, CountryDetails.class);
        return countryDetails;
    }

}
