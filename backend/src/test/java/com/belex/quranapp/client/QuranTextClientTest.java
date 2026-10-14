package com.belex.quranapp.client;
import com.belex.quranapp.client.dto.AyahEditionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class QuranTextClientTest {

    private MockRestServiceServer mockServer;
    private QuranTextClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.alquran.cloud/v1");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new QuranTextClient(builder.build(), "quran-uthmani", "en.sahih");
    }

    @Test
    void fetchAyahWithTranslationParsesArabicAndTranslation() {
        String json = """
            {
              "code": 200,
              "status": "OK",
              "data": [
                {"number": 1, "text": "Arabic text", "numberInSurah": 1,
                 "edition": {"identifier": "quran-uthmani", "language": "ar", "name": "Uthmani", "englishName": "Uthmani"},
                 "surah": {"number": 1, "name": "Al-Faatiha", "englishName": "Al-Faatiha", "englishNameTranslation": "The Opening", "numberOfAyahs": 7, "revelationType": "Meccan"}},
                {"number": 1, "text": "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "numberInSurah": 1,
                 "edition": {"identifier": "en.sahih", "language": "en", "name": "Sahih International", "englishName": "Sahih International"},
                 "surah": {"number": 1, "name": "Al-Faatiha", "englishName": "Al-Faatiha", "englishNameTranslation": "The Opening", "numberOfAyahs": 7, "revelationType": "Meccan"}}
              ]
            }
            """;

        mockServer.expect(requestTo("https://api.alquran.cloud/v1/ayah/1%3A1/editions/quran-uthmani%2Cen.sahih"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<AyahEditionResponse.AyahEdition> result = client.fetchAyahWithTranslation(1, 1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).text()).isEqualTo("Arabic text");
        assertThat(result.get(1).text()).contains("In the name of Allah");
        mockServer.verify();
    }
}
