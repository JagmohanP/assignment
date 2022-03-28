package com.assignment.detailsprovider;

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
import com.assignment.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestPropertySource(locations = "classpath:test-application.properties")
@SpringBootTest
public class CountryDetailsProviderImplTest {

    @Value("${restcountries.api.url.all-countries}")
    private String allCountriesUrl;

    @Value("${restcountries.api.url.country-details-by-name}")
    private String countryDetailsByNameUrl;

    // Service under test
    @Autowired
    private CountryDetailsProvider countryDetailsProvider;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
    }

    @Test
    public void testGetAllCountries()
            throws Exception {

        final URI uri = UriComponentsBuilder.fromUriString(this.allCountriesUrl)
                .query("fields={fields}").buildAndExpand("name,cca2").toUri();

        final String allCountriesJsonData = TestUtils.loadAllCountriesDataFromJson();
        final Country[] finland = TestUtils.toCountryArray(TestUtils.COUNTRY_FINLAND_JSON);

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(allCountriesJsonData));

        final CountryList countryList = this.countryDetailsProvider.getAllCountries();

        assertNotNull(countryList);
        assertNotNull(countryList.getCountries());
        assertFalse(countryList.getCountries().isEmpty());

        final Optional<CountryDetails> finlandDetails = countryList.getCountries().stream()
                .filter(c -> "Finland".equalsIgnoreCase(c.getName()))
                .findFirst();

        assertTrue(finlandDetails.isPresent());

        assertEquals(finland[0].getName(), finlandDetails.get().getName());
        assertEquals(finland[0].getCountryCode(), finlandDetails.get().getCountryCode());
    }

    @Test
    public void testgetDetailsByCountryName()
            throws Exception {
        final Country finland = TestUtils.toCountry(TestUtils.COUNTRY_FINLAND_JSON);

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "Finland");
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams).toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.jsonDataAsString(TestUtils.COUNTRY_FINLAND_JSON)));

        final CountryDetails countryDetails = this.countryDetailsProvider.getDetailsByCountryName("Finland");

        assertNotNull(countryDetails);
        assertEquals(finland.getName(), countryDetails.getName());
        assertEquals(finland.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(finland.getCapital(), countryDetails.getCapital());
        assertEquals(finland.getPopulation(), countryDetails.getPopulation());
        assertEquals(finland.getFlag(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_name_with_spaces()
            throws Exception {

        final Country southAfrica = TestUtils.toCountry(TestUtils.COUNTRY_SOUTH_AFRICA_JSON);

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "South Africa");
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams).toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.jsonDataAsString(TestUtils.COUNTRY_SOUTH_AFRICA_JSON)));

        final CountryDetails countryDetails = this.countryDetailsProvider.getDetailsByCountryName("South Africa");

        assertNotNull(countryDetails);
        assertEquals(southAfrica.getName(), countryDetails.getName());
        assertEquals(southAfrica.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(southAfrica.getCapital(), countryDetails.getCapital());
        assertEquals(southAfrica.getPopulation(), countryDetails.getPopulation());
        assertEquals(southAfrica.getFlag(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_name_with_three_words()
            throws Exception {
        final Country uae = TestUtils.toCountry(TestUtils.COUNTRY_UAE_JSON);

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "United Arab Emirates");
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams).toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.jsonDataAsString(TestUtils.COUNTRY_UAE_JSON)));

        final CountryDetails countryDetails = this.countryDetailsProvider.getDetailsByCountryName("United Arab Emirates");

        assertNotNull(countryDetails);
        assertEquals(uae.getName(), countryDetails.getName());
        assertEquals(uae.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(uae.getCapital(), countryDetails.getCapital());
        assertEquals(uae.getPopulation(), countryDetails.getPopulation());
        assertEquals(uae.getFlag(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName_with_multiple_capitals()
            throws Exception {
        final Country southAfrica = TestUtils.toCountry(TestUtils.COUNTRY_SOUTH_AFRICA_JSON);

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "South Africa");
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams).toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.jsonDataAsString(TestUtils.COUNTRY_SOUTH_AFRICA_JSON)));

        final CountryDetails countryDetails = this.countryDetailsProvider.getDetailsByCountryName("South Africa");

        assertNotNull(countryDetails);
        assertEquals(southAfrica.getCapital().size(), countryDetails.getCapital().size());
        assertTrue(countryDetails.getCapital().containsAll(southAfrica.getCapital()));
    }

    @Test
    public void testgetDetailsByCountryName_with_no_capital()
            throws Exception {

        final Map<String, String> urlParams = new HashMap<>();
        urlParams.put("countryName", "Antarctica");
        urlParams.put("fields", "name,cca2,capital,population,flags");

        final URI uri = UriComponentsBuilder.fromUriString(this.countryDetailsByNameUrl)
                .query("fields={fields}").buildAndExpand(urlParams).toUri();

        this.mockServer.expect(
                MockRestRequestMatchers.requestTo(uri))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators.withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(TestUtils.jsonDataAsString(TestUtils.COUNTRY_ANTARCTICA_JSON)));

        final CountryDetails countryDetails = this.countryDetailsProvider.getDetailsByCountryName("Antarctica");
        assertNotNull(countryDetails);
        assertEquals(0, countryDetails.getCapital().size());
    }

}
