package com.assignment.detailsprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

/**
 * Deals with the external country details provider server to fetch the required
 * country details. <br>
 * Application can choose a different provider in future. So, any such new
 * providers need to stick to this common interface and ensure, all the required
 * data is provided to the system.
 * <br>
 * This specific provider implementation interacts with
 * <a href="URL#https://restcountries.com/">https://restcountries.com/</a>
 * to provide all the country specific details.
 */

@Service
public class CountryDetailsProviderImpl implements CountryDetailsProvider {

    private static final Logger log = LoggerFactory.getLogger(CountryDetailsProviderImpl.class);

    private final RestTemplate restTemplate;

    @Autowired
    public CountryDetailsProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${restcountries.api.url.all-countries}")
    private String allCountriesUrl;

    @Value("${restcountries.api.url.country-details-by-name}")
    private String countryDetailsByNameUrl;

    /**
     * Get name and code for all the countries.
     * <br>
     * Instead of fetching a bulk of
     * data for each country, the request includes only country name and country
     * code fields to be returned as part of response. This helps in reducing
     * the response size and faster processing of the response.
     *
     * @return list of countries having country name and country code
     */
    @Override
    public CountryList getAllCountries() {

        final UriComponents uriComponents = UriComponentsBuilder.fromUriString(this.allCountriesUrl)
                .query("fields={fields}").buildAndExpand("name,cca2");

        final String uri = uriComponents.toUriString();
        log.debug("GetAllCountries uri = {}", uri);

        final Country[] allCountries = this.restTemplate.getForObject(uri, Country[].class);
        final List<CountryDetails> countriesDetailsList = this.toCountryDetailsList(allCountries);
        final CountryList countryList = new CountryList(countriesDetailsList);
        return countryList;
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
        //Fetch only needed field to reduce the response size
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final UriComponents uriComponents = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams);

        final String uri = uriComponents.toUriString();
        log.debug("GetDetailsByCountryName uri = {}", uri);

        final Country[] countryDetails = this.restTemplate.getForObject(uri, Country[].class);
        return this.toCountryDetails(countryDetails[0]);
    }

    private CountryDetails toCountryDetails(final Country country) {

        final CountryDetails countryDetails = new CountryDetails();
        countryDetails.setName(country.getName());
        countryDetails.setCapital(country.getCapital());
        countryDetails.setCountryCode(country.getCountryCode());
        countryDetails.setFlagFileUrl(country.getFlag());
        countryDetails.setPopulation(country.getPopulation());

        return countryDetails;
    }

    private List<CountryDetails> toCountryDetailsList(final Country[] countriesArr) {
        final List<CountryDetails> countries = new ArrayList<>();

        if ((null != countriesArr) && (countriesArr.length > 0)) {
            for (final Country country : countriesArr) {
                countries.add(this.toCountryDetails(country));
            }
        }
        return countries;
    }
}
