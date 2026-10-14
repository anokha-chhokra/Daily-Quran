package com.belex.quranapp.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AyahEditionResponse(int code, String status, List<AyahEdition> data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AyahEdition(int number, String text, int numberInSurah, Edition edition, SurahInfo surah){ }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Edition(String identifier, String language, String name, String englishName){}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SurahInfo(int number, String name, String englishName, String englishNameTranslation, int numberOfAyahs, String revelationType){}

}
