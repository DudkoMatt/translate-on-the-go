package com.devtools.plugin.text.translator;

import com.devtools.plugin.exceptions.RequestException;

public interface Request {
    /**
     * Sends request to translate text
     * @param textToTranslate text to be translated
     * @return translated text
     * @throws RequestException if some error happened with request
     */
    String send(String textToTranslate) throws RequestException;
}
