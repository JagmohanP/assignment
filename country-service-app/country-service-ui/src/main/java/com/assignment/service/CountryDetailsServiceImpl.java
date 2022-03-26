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
     * @return list of countries having country name and country code
     */
    @Override
    public Map<String, String> getAllCountries() {

        System.out.println("countryServiceUrl = " + countryServiceUrl);
        System.out.println("allCountriesUrl = " + allCountriesUrl);
        System.out.println("countryDetailsByNameUrl = " + countryDetailsByNameUrl);

        final String uri = UriComponentsBuilder.fromUriString(allCountriesUrl).toUriString();
        log.debug("GetAllCountries uri = {}", uri);

        //        final HttpEntity<String> entity = new HttpEntity<>("body", headers);
        //        final CountryList allCountries = restTemplate.exchange(uri, HttpMethod.GET, entity, CountryList.class);

        final CountryList allCountries = restTemplate.getForObject(uri, CountryList.class);

        //        try {
        //
        //            final HttpHeaders headers = new HttpHeaders();
        //            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //
        //            final HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        //            final ResponseEntity<CountryList> response = restTemplate.exchange(uri, HttpMethod.GET, entity, CountryList.class);
        //            System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.hasBody());
        //        }
        //        catch (final Exception eek) {
        //            System.out.println("** Exception: " + eek.getMessage());
        //        }

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
