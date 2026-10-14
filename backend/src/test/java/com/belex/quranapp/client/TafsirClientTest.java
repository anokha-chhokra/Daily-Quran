package com.belex.quranapp.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class TafsirClientTest {

    private MockRestServiceServer mockServer;
    private TafsirClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://api.quran.com/api/v4");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new TafsirClient(builder.build(), 169L);
    }

    @Test
    void fetchTafsirTextStripsHtmlTags() {
        String json = """
            {"tafsir": {"id": 1, "text": "<p>This opening chapter <b>sets the tone</b>.</p>", "verseKey": "1:1"}}
            """;

        mockServer.expect(requestTo("https://api.quran.com/api/v4/tafsirs/169/by_ayah/1%3A1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        String result = client.fetchTafsirText(1, 1);

        assertThat(result).isEqualTo("This opening chapter sets the tone.");
        mockServer.verify();
    }
}
