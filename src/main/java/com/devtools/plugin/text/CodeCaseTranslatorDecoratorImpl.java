package com.devtools.plugin.text;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;

public class CodeCaseTranslatorDecoratorImpl implements Translator {

    private final Translator translator;

    public CodeCaseTranslatorDecoratorImpl(Translator translator) {
        this.translator = translator;
    }

    private char[] fromUpperToLower(char[] ch) {
        for (int i = 0; i < ch.length; ++i) {
            if (Character.isUpperCase(ch[i])) {
                ch[i] = Character.toLowerCase(ch[i]);
            }
        }
        return ch;
    }

    private char[] fromLowerToUpper(char[] ch) {
        for (int i = 0; i < ch.length; ++i) {
            if (Character.isLowerCase(ch[i])) {
                ch[i] = Character.toUpperCase(ch[i]);
            }
        }
        return ch;
    }

    private String changeChar(char[] ch, int n, char signFrom, char signTo) {
        for (int i = 0; i < n; ++i) {
            if (ch[i] == signFrom) {
                ch[i] = signTo;
            }
            ch[i] = ch[i];
        }
        return new String(ch);
    }

    private static String fromSnakeAndCamelCase(String str) {
        StringBuilder retVal = new StringBuilder();

        retVal.append(str.charAt(0));
        for (int i = 1; i < str.length(); i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                retVal.append(str.charAt(i));
            } else {
                retVal.append(" ");
                retVal.append(Character.toLowerCase(str.charAt(i)));
            }
        }
        return retVal.toString();
    }

    private String fromSnakeCase(char[] ch, int n) {
        return fromUpperCase(ch, n);
    }

    private String fromKebabCase(char[] ch, int n) {
        return changeChar(ch, n, '-', ' ');
    }

    private String fromUpperCase(char[] ch, int n) {
        return changeChar(fromUpperToLower(ch), n, '_', ' ');
    }

    private String toSnakeCase(String text) {
        return changeChar(text.toCharArray(), text.length(), ' ', '_');
    }

    private String toKebabCase(String text) {
        return changeChar(text.toCharArray(), text.length(), ' ', '-');
    }

    private String toUpperCaseSnakeCase(String text) {
        return changeChar(fromLowerToUpper(text.toCharArray()), text.length(), ' ', '_');
    }

    private String toCamelCase(String text, boolean startsWithUpper) {
        int cnt = 0;
        int n = text.length();
        char[] ch = text.toCharArray();
        int res_ind = 0;

        if (startsWithUpper) {
            ch[0] = Character.toUpperCase(ch[0]);
        }
        else {
            ch[0] = Character.toLowerCase(ch[0]);
        }

        for (int i = 0; i < n; i++) {
            if (ch[i] == ' ') {
                cnt++;
                ch[i + 1] = Character.toUpperCase(ch[i + 1]);
            } else
                ch[res_ind++] = ch[i];
        }
        return String.valueOf(ch, 0, n - cnt);
    }

    private String toPascalCase(String text) {
        return toCamelCase(text, true);
    }

    enum CodeCase {
        SNAKE_CASE,
        KEBAB_CASE,
        UPPER_CASE_SNAKE_CASE,
        PASCAL_CASE,
        CAMEL_CASE,
        UNDEFINED_CASE;
    }

    @Override
    public String translate(String text) throws RequestException {
        if (text == null || text.isEmpty()) {
            throw new RequestException("Input text is null or is empty");
        }

        char[] ch = text.toCharArray();
        String result;
        CodeCase codeCase = CodeCase.UNDEFINED_CASE;

        if (Character.isUpperCase(ch[0]) && Character.isUpperCase(ch[1])) {
            result = fromUpperCase(ch, text.length());
            codeCase = CodeCase.UPPER_CASE_SNAKE_CASE;

        } else if (new String(ch).contains("_")) {
            result = fromSnakeCase(ch, text.length());
            codeCase = CodeCase.SNAKE_CASE;

        } else if (new String(ch).contains("-")) {
            result = fromKebabCase(ch, text.length());
            codeCase = CodeCase.KEBAB_CASE;

        } else if (Character.isUpperCase(ch[0]) && Character.isLowerCase(ch[1])) {
            result = fromSnakeAndCamelCase(text);
            codeCase = CodeCase.PASCAL_CASE;

        } else if (Character.isLowerCase(ch[0]) && Character.isLowerCase(ch[1])) {
            result = fromSnakeAndCamelCase(text);
            codeCase = CodeCase.CAMEL_CASE;

        } else {
            result = text;
        }

        String translatedText = translator.translate(result);

        switch (codeCase) {
            case SNAKE_CASE:
                return toSnakeCase(translatedText);
            case KEBAB_CASE:
                return toKebabCase(translatedText);
            case UPPER_CASE_SNAKE_CASE:
                return toUpperCaseSnakeCase(translatedText);
            case CAMEL_CASE:
                return toCamelCase(translatedText, false);
            case PASCAL_CASE:
                return toPascalCase(translatedText);
            default:
                return translatedText;
        }
    }
}
