package com.assignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.assignment.service.CountryDetailsService;

/**
 * Controller class for defining the APIs for country details.
 *
 */
@RestController
public class CountryDetailsController {

    private static final Logger log = LoggerFactory.getLogger(CountryDetailsController.class);

    private final CountryDetailsService countryDetailsService;

    @Autowired
    public CountryDetailsController(final CountryDetailsService countryDetailsService) {
        this.countryDetailsService = countryDetailsService;
    }

    /**
     * Get the country name and country code for all the countries.
     *
     * @param name country name
     * @return Country details. <br>
     *         Example response format : <br>
     *
     *         <pre>
     *         {
     *         "countries": [
     *         ...
     *         {
     *         "name": "Finland",
     *         "country_code": "FI"
     *         },
     *         {
     *         "name": "India",
     *         "country_code": "IN"
     *         },
     *         ...
     *         ]
     *         }
     *
     *         <pre>
     *
     */
    @GetMapping("/countries")
    public CountryList getAllCountries() {
        log.debug("Entered getAllCountries().");
        return countryDetailsService.getAllCountries();
    }

    /**
     * Get the Country details like name, country code, capital, population and
     * flag image url
     *
     * @param name country name
     * @return Country details. <br>
     *         Example response format : <br>
     *
     *         <pre>
     *         {
     *         "name": "Finland",
     *         "country_code": "FI",
     *         "capital": [
     *         "Helsinki"
     *         ],
     *         "population": 5530719,
     *         "flag_file_url": "https://flagcdn.com/w320/fi.png"
     *         }
     *
     *         <pre>
     *
     */
    @GetMapping("/countries/{name}")
    public CountryDetails getDetailsByCountryName(@PathVariable("name") final String name) {
        log.debug("Entered getDetailsByCountryName() : name = {}", name);
        return countryDetailsService.getDetailsByCountryName(name);
    }
}
