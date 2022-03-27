package com.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.HashMap;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.assignment.controller.AppController;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.assignment.exception.APIExceptionHandler;
import com.assignment.exception.ErrorCodes;
import com.assignment.exception.NotFoundException;
import com.assignment.service.CountryDetailsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ImportAutoConfiguration(APIExceptionHandler.class)
class CountryServiceUIApplicationTests {

    @LocalServerPort
    private int port;

    private static final String HOST = "http://localhost:";
    private static final String ALL_COUNTRIES = "/country-service-ui/countries";
    private static final String COUNTRY_DETAILS_BY_NAME = "/country-service-ui/countries/";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    AppController appController;

    @MockBean
    private CountryDetailsService countryDetailsService;

    private static final ObjectMapper mapper = new ObjectMapper();

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
    public void testIndexPage()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

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
        final String uri = HOST + this.port + ALL_COUNTRIES;

        final CountryList allCountries = loadAllCountriesDataFromJson();

        final Map<String, String> countriesMap = allCountries.getCountries().stream().collect(
                Collectors.toMap(CountryDetails::getName, CountryDetails::getCountryCode));

        Mockito.when(this.countryDetailsService.getAllCountries())
                .thenReturn(countriesMap);

        final String body = this.testRestTemplate.getForObject(uri, String.class);

        final TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };
        final Map<String, String> countriesMapOutPut = mapper.readValue(body, typeRef);

        assertNotNull(countriesMapOutPut);
        assertTrue(countriesMapOutPut.containsKey("Finland") && ("FI".equalsIgnoreCase(countriesMapOutPut.get("Finland"))));
        assertTrue(countriesMapOutPut.containsKey("India") && ("IN".equalsIgnoreCase(countriesMapOutPut.get("India"))));
    }

    @Test
    public void testGetDetailsByCountryName()
            throws Exception {
        final String uri = HOST + this.port + COUNTRY_DETAILS_BY_NAME + "Finland";

        final CountryDetails finland = loadCountryDataFromJson("country-finland.json");

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

        this.mockMvc.perform(MockMvcRequestBuilders.get(COUNTRY_DETAILS_BY_NAME + "SomeCountry"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
