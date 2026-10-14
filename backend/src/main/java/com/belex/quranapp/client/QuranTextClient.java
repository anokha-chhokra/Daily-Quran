package com.belex.quranapp.client;

import com.belex.quranapp.client.dto.AyahEditionResponse;
import com.belex.quranapp.client.dto.AyahEditionResponse.AyahEdition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class QuranTextClient {
    private final RestClient restClient;
    private final String arabicEdition;
    private final String translationEdition;

    public QuranTextClient(@Qualifier ("quranTextRestClient") RestClient restClient,
                           @Value("${quran.api.arabic-edition}") String arabicEdition,
                           @Value("${quran.api.text-edition}") String translationEdition) {
        this.restClient = restClient;
        this.arabicEdition = arabicEdition;
        this.translationEdition = translationEdition;
    }

    public List<AyahEdition> fetchAyahWithTranslation(int surahNumber, int ayahNumber) {
        String reference = surahNumber + ":" + ayahNumber;
        AyahEditionResponse response = restClient.get()
                .uri("/ayah/{reference}/editions/{editions}", reference, arabicEdition + "," + translationEdition)
                .retrieve().body(AyahEditionResponse.class);

        if(response == null || response.data() == null || response.data().size() < 2){
            throw new QuranApiException("Unexpected response fetching ayah" + reference);
        }
        return response.data();
    }
}
