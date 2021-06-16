package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FreeTranslator implements Translator {
    @Override
    public String translate(String text) throws RequestException {
        TranslateRequest request = new TranslateRequest();

        JSONObject obj = new JSONObject(request.send(text));
        return obj.getString("translatedText");
    }

    private static class TranslateRequest {
        public String send(String textToTranslate) throws RequestException {
            Map<Object, Object> data = new HashMap<>();
            data.put("q", textToTranslate);
            data.put("source", "ru");
            data.put("target", "en");

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(buildFormDataFromMap(data))
                    .uri(URI.create("https://translate.astian.org/translate"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()); // ToDO: add multi-threading
                if (response.statusCode() != 200) {
                    throw new RequestException("Error " + response.statusCode() + " occurred");
                }
                return response.body();
            } catch (IOException | InterruptedException e) {
                throw new RequestException("Can't send request, check internet connection.");
            }
        }

        private final HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
            var builder = new StringBuilder();
            for (Map.Entry<Object, Object> entry : data.entrySet()) {
                if (builder.length() > 0) {
                    builder.append("&");
                }
                builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
            }
            return HttpRequest.BodyPublishers.ofString(builder.toString());
        }
    }

}
