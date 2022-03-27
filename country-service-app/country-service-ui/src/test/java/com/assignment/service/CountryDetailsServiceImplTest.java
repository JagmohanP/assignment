package com.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestPropertySource(locations = "classpath:test-application.properties")
@SpringBootTest
public class CountryDetailsServiceImplTest {

    @Value("${countryservice.url}")
    private String countryServiceUrl;

    @Value("${countryservice.url.all-countries}")
    private String allCountriesUrl;

    @Value("${countryservice.url.country-details-by-name}")
    private String countryDetailsByNameUrl;

    @Autowired
    Environment environment;
    // Service under test
    @Autowired
    private CountryDetailsService countryDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
    }

    private static CountryList loadAllCountriesDataFromJson()
            throws Exception {

        final CountryList countries = mapper.readValue(
                new ClassPathResource("all-countries.json").getFile(),
                CountryList.class);
        return countries;
    }

    private static CountryDetails loadCountryDataFromJson(final String countryName)
            throws Exception {
        final CountryDetails countryDetails = mapper.readValue(
                new ClassPathResource(countryName).getFile(),
                CountryDetails.class);
        return countryDetails;
    }

    @Test
    public void testAllSupportedCountries()
            throws Exception {

        final CountryList allCountries = loadAllCountriesDataFromJson();
        final CountryDetails finland = loadCountryDataFromJson("country-finland.json");

        final URI uri = UriComponentsBuilder.fromUriString(this.allCountriesUrl).build().toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(allCountries)));

        final Map<String, String> countries = this.countryDetailsService.getAllCountries();

        assertNotNull(countries);
        assertFalse(countries.isEmpty());

        final Optional<String> finlandDetails = countries.entrySet().stream()
                .filter(e -> "Finland".equals(e.getKey()))
                .map(Map.Entry::getKey)
                .findFirst();

        assertTrue(finlandDetails.isPresent());
        assertEquals(finland.getCountryCode(), countries.get("Finland"));
    }

    @Test
    public void testgetDetailsByCountryName()
            throws Exception {
        final CountryDetails finland = loadCountryDataFromJson("country-finland.json");

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "Finland");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .buildAndExpand(urlParams)
                .toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(finland)));

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("Finland");

        assertNotNull(countryDetails);
        assertEquals(finland.getName(), countryDetails.getName());
        assertEquals(finland.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(finland.getCapital(), countryDetails.getCapital());
        assertEquals(finland.getPopulation(), countryDetails.getPopulation());
        assertEquals(finland.getFlagFileUrl(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_name_with_spaces()
            throws Exception {

        final CountryDetails southAfrica = loadCountryDataFromJson("country-south-africa.json");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .buildAndExpand("South Africa")
                .toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(southAfrica)));

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("South Africa");

        assertNotNull(countryDetails);
        assertEquals(southAfrica.getName(), countryDetails.getName());
        assertEquals(southAfrica.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(southAfrica.getCapital(), countryDetails.getCapital());
        assertEquals(southAfrica.getPopulation(), countryDetails.getPopulation());
        assertEquals(southAfrica.getFlagFileUrl(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_name_with_three_words()
            throws Exception {
        final CountryDetails uae = loadCountryDataFromJson("country-uae.json");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .buildAndExpand("United Arab Emirates")
                .toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(uae)));

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("United Arab Emirates");

        assertNotNull(countryDetails);
        assertEquals(uae.getName(), countryDetails.getName());
        assertEquals(uae.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(uae.getCapital(), countryDetails.getCapital());
        assertEquals(uae.getPopulation(), countryDetails.getPopulation());
        assertEquals(uae.getFlagFileUrl(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_with_multiple_capitals()
            throws Exception {
        final CountryDetails southAfrica = loadCountryDataFromJson("country-south-africa.json");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .buildAndExpand("South Africa")
                .toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(southAfrica)));

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("South Africa");

        assertNotNull(countryDetails);
        assertEquals(southAfrica.getCapital().size(), countryDetails.getCapital().size());
        assertTrue(countryDetails.getCapital().containsAll(southAfrica.getCapital()));
    }

    @Test
    public void testgetDetailsByCountryName_with_no_capital()
            throws Exception {
        final CountryDetails antarctica = loadCountryDataFromJson("country-antarctica.json");
        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .buildAndExpand("Antarctica")
                .toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mapper.writeValueAsString(antarctica)));

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("Antarctica");
        assertNotNull(countryDetails);
        assertEquals(0, countryDetails.getCapital().size());
    }

}
