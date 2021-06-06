package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.NoTokenException;
import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import org.json.JSONObject;

public class FreeTranslator implements Translator {

    @Override
    public String translate(String text) throws RequestException, NoTokenException {
        text = text.replaceAll(" ", "%20");

        TranslateRequest request = new TranslateRequest();

        String jsonString = request.send(text);
        JSONObject obj = new JSONObject(jsonString);
        return obj.getJSONArray("outputs").getJSONObject(0).getString("output");
    }
}
