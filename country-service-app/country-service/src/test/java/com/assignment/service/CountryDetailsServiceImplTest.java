package com.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.assignment.detailsprovider.CountryDetailsProvider;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;
import com.assignment.util.TestUtils;

@TestPropertySource(locations = "classpath:test-application.properties")
@SpringBootTest
public class CountryDetailsServiceImplTest {

    // Service under test
    private CountryDetailsService countryDetailsService;

    @Mock
    private CountryDetailsProvider countryDetailsProvider;

    @BeforeEach
    void setUp() {
        this.countryDetailsService = new CountryDetailsServiceImpl(this.countryDetailsProvider);
    }

    @Test
    public void testGetAllCountries()
            throws Exception {

        final CountryList allCountries = TestUtils.toCountryList(TestUtils.ALL_COUNTRIES_JSON);
        final CountryDetails finland = TestUtils.toCountryDetails(TestUtils.COUNTRY_FINLAND_JSON);

        when(this.countryDetailsProvider.getAllCountries()).thenReturn(allCountries);

        final CountryList countryList = this.countryDetailsService.getAllCountries();

        assertNotNull(countryList);
        assertNotNull(countryList.getCountries());
        assertFalse(countryList.getCountries().isEmpty());

        final Optional<CountryDetails> finlandDetails = countryList.getCountries().stream()
                .filter(c -> "Finland".equalsIgnoreCase(c.getName()))
                .findFirst();

        assertTrue(finlandDetails.isPresent());

        assertNotNull(finlandDetails.get().getName());
        assertNotNull(finlandDetails.get().getCountryCode());

        assertEquals(finland.getName(), finlandDetails.get().getName());
        assertEquals(finland.getCountryCode(), finlandDetails.get().getCountryCode());

        // Only country name and code is fetched as part of all countries data. Other fields should be null.
        assertNull(finlandDetails.get().getCapital());
        assertNull(finlandDetails.get().getPopulation());
        assertNull(finlandDetails.get().getFlagFileUrl());
    }

    @Test
    public void testgetDetailsByCountryName()
            throws Exception {
        final CountryDetails finland = TestUtils.toCountryDetails(TestUtils.COUNTRY_FINLAND_JSON);
        when(this.countryDetailsProvider.getDetailsByCountryName(anyString())).thenReturn(finland);

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

        final CountryDetails southAfrica = TestUtils.toCountryDetails(TestUtils.COUNTRY_SOUTH_AFRICA_JSON);

        when(this.countryDetailsProvider.getDetailsByCountryName(anyString())).thenReturn(southAfrica);

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
        final CountryDetails uae = TestUtils.toCountryDetails(TestUtils.COUNTRY_UAE_JSON);
        when(this.countryDetailsProvider.getDetailsByCountryName(anyString())).thenReturn(uae);

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
        final CountryDetails southAfrica = TestUtils.toCountryDetails(TestUtils.COUNTRY_SOUTH_AFRICA_JSON);
        when(this.countryDetailsProvider.getDetailsByCountryName(anyString())).thenReturn(southAfrica);

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("South Africa");

        assertNotNull(countryDetails);
        assertEquals(southAfrica.getCapital().size(), countryDetails.getCapital().size());
        assertTrue(countryDetails.getCapital().containsAll(southAfrica.getCapital()));
    }

    @Test
    public void testgetDetailsByCountryName_with_no_capital()
            throws Exception {
        final CountryDetails antarctica = TestUtils.toCountryDetails(TestUtils.COUNTRY_ANTARCTICA_JSON);
        when(this.countryDetailsProvider.getDetailsByCountryName(anyString())).thenReturn(antarctica);

        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("Antarctica");
        assertNotNull(countryDetails);
        assertEquals(0, countryDetails.getCapital().size());
    }

    @Test
    public void testgetDetailsByCountryName_with_invalid_name()
            throws Exception {
        final CountryDetails countryDetails = this.countryDetailsService.getDetailsByCountryName("SomeCountry");
        assertNull(countryDetails);
    }
}
