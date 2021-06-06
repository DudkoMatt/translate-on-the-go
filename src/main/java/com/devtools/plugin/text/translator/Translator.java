package com.devtools.plugin.text.translator;

import com.devtools.plugin.exceptions.RequestException;

public interface Translator {
    String translate(String text) throws RequestException;
}
