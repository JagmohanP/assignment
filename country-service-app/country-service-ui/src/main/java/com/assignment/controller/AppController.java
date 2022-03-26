package com.assignment.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.assignment.dto.CountryDetails;
import com.assignment.service.CountryDetailsService;

/**
 * Controller class for defining the APIs for country details.
 *
 */
@RestController
public class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    private final CountryDetailsService countryDetailsService;

    @Autowired
    public AppController(final CountryDetailsService countryDetailsService) {
        this.countryDetailsService = countryDetailsService;
    }

    /**
     * Default mapping to load the index page.
     *
     * @return the index page with supported currencies populated for
     *         conversion.
     */
    @RequestMapping("/")
    public ModelAndView index() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
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

    /**
     * Get the country name and country code for all the countries.
     *
     * @param name country name
     * @return Country details. <br>
     *         Example response format : <br>
     *
     *         <pre>
     *         {
     *         ...
     *         "Finland": "FI",
     *         "France": "FR",
     *         ...
     *         }
     *
     *         <pre>
     *
     */
    @GetMapping("/countries")
    public Map<String, String> getAllCountries() {
        log.debug("Entered getAllCountries().");
        return countryDetailsService.getAllCountries();
    }
}
