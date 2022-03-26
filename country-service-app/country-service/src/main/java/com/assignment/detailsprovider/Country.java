package com.assignment.detailsprovider;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * POJO equivalent class for the json response of the external country details
 * server API calls. This class shouln't be sent back directly as it's very
 * specific to the external provider implementation. <br>
 * <br>
 * Sample output format for reference:<br>
 *
 * <pre>
    [
     {
         "flags": {
             "png": "https://flagcdn.com/w320/fi.png",
             "svg": "https://flagcdn.com/fi.svg"
         },
         "name": {
             "common": "Finland",
             "official": "Republic of Finland",
             "nativeName": {
                 "fin": {
                     "official": "Suomen tasavalta",
                     "common": "Suomi"
                 },
                 "swe": {
                     "official": "Republiken Finland",
                     "common": "Finland"
                 }
             }
         },
         "cca2": "FI",
         "capital": [
             "Helsinki"
         ],
         "population": 5530719
     }
 ]
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class Country {

    private String name;
    private String flag;

    @JsonProperty("cca2")
    private String countryCode;

    @JsonProperty("capital")
    private List<String> capital;

    @JsonProperty("population")
    private Long population;

    /*
     * Country name is present at name.common location of the json object. Need
     * to extract it.
     */
    @JsonProperty("name")
    private void unpackNestedName(final Map<String, Object> nameObject) {
        this.name = (String) nameObject.get("common");
    }

    /*
     * There are two types of flag image url. Need to pick one of the image type
     * url, e.g png type url is returned.
     */
    @JsonProperty("flags")
    private void unpackNestedFlag(final Map<String, Object> flagsObject) {
        this.flag = (String) flagsObject.get("png");
    }

}
