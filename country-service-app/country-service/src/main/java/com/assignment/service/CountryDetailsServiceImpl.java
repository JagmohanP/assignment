package com.assignment.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.detailsprovider.CountryDetailsProvider;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

/**
 * This class is responsible for fetching the country specific details through
 * country details providers. Instead of directly interacting with external
 * servers, it goes via providers to ensure minimum or no disruption when the
 * providers are changed.
 *
 */
@Service
public class CountryDetailsServiceImpl implements CountryDetailsService {

    private final CountryDetailsProvider countryDetailsProvider;

    @Autowired
    public CountryDetailsServiceImpl(final CountryDetailsProvider countryDetailsProvider) {
        this.countryDetailsProvider = countryDetailsProvider;
    }

    @Override
    public CountryList getAllCountries() {
        final CountryList countryList = this.countryDetailsProvider.getAllCountries();
        if (null != countryList) {
            // Sort the list by country name.
            Collections.sort(countryList.getCountries(), (c1, c2) -> c1.getName().compareTo(c2.getName()));
        }
        return countryList;
    }

    @Override
    public CountryDetails getDetailsByCountryName(final String countryName) {
        return this.countryDetailsProvider.getDetailsByCountryName(countryName);
    }

}
