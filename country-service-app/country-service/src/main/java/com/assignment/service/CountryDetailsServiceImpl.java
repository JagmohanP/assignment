package com.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.detailsprovider.CountryDetailsProvider;
import com.assignment.dto.CountryDetails;
import com.assignment.dto.CountryList;

@Service
public class CountryDetailsServiceImpl implements CountryDetailsService {

    private final CountryDetailsProvider countryDetailsProvider;

    @Autowired
    public CountryDetailsServiceImpl(final CountryDetailsProvider countryDetailsProvider) {
        this.countryDetailsProvider = countryDetailsProvider;
    }

    @Override
    public CountryList getAllCountries() {
        return countryDetailsProvider.getAllCountries();
    }

    @Override
    public CountryDetails getDetailsByCountryName(final String countryName) {
        return countryDetailsProvider.getDetailsByCountryName(countryName);
    }

}
