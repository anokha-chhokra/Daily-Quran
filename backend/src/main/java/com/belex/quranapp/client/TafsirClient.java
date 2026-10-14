package com.belex.quranapp.client;

import com.belex.quranapp.client.dto.TafsirResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TafsirClient {

    private final RestClient restClient;
    private final long tafsirResourceId;

    public TafsirClient(@Qualifier("tafsirRestClient") RestClient restClient,
                        @Value("${quran.api.tafsir-resource-id}") long tafsirResourceId) {
        this.restClient = restClient;
        this.tafsirResourceId = tafsirResourceId;
    }

    public String fetchTafsirText(int surahNumber, int ayahNumber) {
        String verseKey = surahNumber+":"+ayahNumber;
        TafsirResponse response = restClient.get()
                .uri("/tafsirs/{resourceId}/by_ayah/{verseKey}", tafsirResourceId, verseKey)
                .retrieve()
                .body(TafsirResponse.class);

        if (response == null || response.tafsir() == null) {
            throw new QuranApiException("No tafsir found for " + verseKey);
        }
        return stripHtml(response.tafsir().text());
    }

    private String stripHtml(String html) {
        return html.replaceAll("<[^>]*>", "").trim();
    }
}