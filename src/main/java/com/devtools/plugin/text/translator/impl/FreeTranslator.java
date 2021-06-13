package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.NoTokenException;
import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import org.json.JSONObject;

public class FreeTranslator implements Translator {

    @Override
    public String translate(String text) throws RequestException, NoTokenException {
        TranslateRequest request = new TranslateRequest();

        JSONObject obj = new JSONObject(request.send(text));
        return obj.getString("translatedText");
    }
}
