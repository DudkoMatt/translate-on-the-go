package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import org.json.JSONObject;

public class FreeTranslator implements Translator {

    @Override
    public String translate(String text) throws RequestException {
        text = text.replaceAll(" ", "%20");

        String token = "TOKEN";
        TranslateRequest request = new TranslateRequest(token);

        String jsonString = request.send(text);
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONArray("outputs").getJSONObject(0).getString("output");
    }
}
