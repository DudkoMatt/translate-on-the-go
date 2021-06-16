package com.devtools.plugin.text;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import org.apache.commons.lang.NotImplementedException;

public class CodeCaseTranslatorDecoratorImpl implements Translator {
    private final Translator translator;

    public CodeCaseTranslatorDecoratorImpl(Translator translator) {
        this.translator = translator;
    }

    @Override
    public String translate(String text) throws RequestException {
        // ToDO --> parse input text to words
        // ToDO --> String result = translator.translate(text);
        // ToDO --> parse result and return in correct code case
        throw new NotImplementedException("Method translate in CodeCaseTranslatorDecoratorImpl is not implemented");
    }
}
