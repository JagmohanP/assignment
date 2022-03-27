package com.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.assignment.controller.CountryDetailsController;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.assignment.exception.APIExceptionHandler;
import com.assignment.exception.ErrorCodes;
import com.assignment.exception.NotFoundException;
import com.assignment.service.CountryDetailsService;
import com.assignment.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ImportAutoConfiguration(APIExceptionHandler.class)
public class CountryServiceApplicationTests {

    @LocalServerPort
    private int port;

    private static final String HOST = "http://localhost:";
    private static final String COUNTRIES_URL = "/country-service/countries/";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    CountryDetailsController countryDetailsController;

    @MockBean
    private CountryDetailsService countryDetailsService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGlobalExceptionHandlerError()
            throws Exception {
        Mockito.when(this.countryDetailsService.getAllCountries())
                .thenThrow(new RuntimeException("Unable to process the request. Please try after sometime."));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/countries")).andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetAllCountries()
            throws Exception {
        final String uri = HOST + this.port + COUNTRIES_URL;

        final CountryList allCountries = TestUtils.loadAllCountriesDataFromJson();

        Mockito.when(this.countryDetailsService.getAllCountries())
                .thenReturn(allCountries);

        final String body = this.testRestTemplate.getForObject(uri, String.class);

        final CountryList countriesOutPut = mapper.readValue(body, CountryList.class);

        final Map<String, String> countriesMapOutPut = countriesOutPut.getCountries().stream().collect(
                Collectors.toMap(CountryDetails::getName, CountryDetails::getCountryCode));

        assertNotNull(countriesMapOutPut);
        assertTrue(countriesMapOutPut.containsKey("Finland") && ("FI".equalsIgnoreCase(countriesMapOutPut.get("Finland"))));
        assertTrue(countriesMapOutPut.containsKey("India") && ("IN".equalsIgnoreCase(countriesMapOutPut.get("India"))));
    }

    @Test
    public void testGetDetailsByCountryName()
            throws Exception {
        final String uri = HOST + this.port + COUNTRIES_URL + "Finland";

        final CountryDetails finland = TestUtils.loadCountryDataFromJson(TestUtils.COUNTRY_FINLAND_JSON);

        Mockito.when(this.countryDetailsService.getDetailsByCountryName(anyString()))
                .thenReturn(finland);

        final String body = this.testRestTemplate.getForObject(uri, String.class);

        final CountryDetails countryDetails = mapper.readValue(body, CountryDetails.class);

        assertNotNull(countryDetails);
        assertEquals(finland.getName(), countryDetails.getName());
        assertEquals(finland.getCountryCode(), countryDetails.getCountryCode());
        assertEquals(finland.getCapital(), countryDetails.getCapital());
        assertEquals(finland.getPopulation(), countryDetails.getPopulation());
        assertEquals(finland.getFlagFileUrl(), countryDetails.getFlagFileUrl());
    }

    @Test
    public void testGetDetailsByCountryName_wrong_name()
            throws Exception {
        Mockito.when(this.countryDetailsService.getDetailsByCountryName(anyString()))
                .thenThrow(new NotFoundException(ErrorCodes.NotFound));

        this.mockMvc.perform(MockMvcRequestBuilders.get(COUNTRIES_URL + "SomeCountry"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
