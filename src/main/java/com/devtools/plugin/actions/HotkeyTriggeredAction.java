package com.devtools.plugin.actions;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import com.devtools.plugin.text.translator.impl.FreeTranslator;
import com.devtools.plugin.ui.messages.PopupMessage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public class HotkeyTriggeredAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null) {
            return;
        }

        Translator translator = new FreeTranslator();
        try {
            String translatedText = translator.translate(selectedText);
            new PopupMessage(event).showTranslatedText(translatedText);
        } catch (RequestException e) {
            new PopupMessage(event).showErrorMessage(e.getMessage());
        }
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
