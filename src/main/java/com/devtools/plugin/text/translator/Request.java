package com.devtools.plugin.text.translator;

import com.devtools.plugin.exceptions.RequestException;

public interface Request {
    String send(String textToTranslate) throws RequestException;
}
