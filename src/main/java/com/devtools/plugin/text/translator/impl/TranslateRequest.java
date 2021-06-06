package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Request;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TranslateRequest implements Request {
    final private String token;

    /**
     * Constructor from token for API
     * @param token api token
     */
    TranslateRequest(String token) {
        this.token = token;
    }

    @Override
    public String send(String textToTranslate) throws RequestException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://systran-systran-platform-for-language-processing-v1.p.rapidapi.com/translation/text/translate?source=auto&target=ru&input=" + textToTranslate))
                .header("x-rapidapi-key", token)
                .header("x-rapidapi-host", "systran-systran-platform-for-language-processing-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RequestException("Error with sending the request", e);
        }
    }
}
