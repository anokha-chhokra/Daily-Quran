package com.belex.quranapp.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TafsirResponse(Tafsir tafsir) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Tafsir(long id, String text, String verseKey) {}
}