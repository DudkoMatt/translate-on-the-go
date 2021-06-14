package com.devtools.plugin.actions;

import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Translator;
import com.devtools.plugin.text.translator.impl.FreeTranslator;
import com.devtools.plugin.ui.messages.PopupMessage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class HotkeyTriggeredAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            Messages.showMessageDialog("Selection is empty. Select text and trigger action again", "Translate On The Go", Messages.getInformationIcon());
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null) {
            new PopupMessage(event).showErrorMessage("Select text to translate");
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
