package com.devtools.plugin.text.translator;

import com.devtools.plugin.exceptions.NoTokenException;
import com.devtools.plugin.exceptions.RequestException;

public interface Translator {
    /**
     * Translates text to russian language
     * @param text text, which needs to be translated (any language)
     * @return translated to russian text
     * @throws RequestException if some error with api request happens
     */
    String translate(String text) throws RequestException, NoTokenException;
}
