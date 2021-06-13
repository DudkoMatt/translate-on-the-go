package com.devtools.plugin.ui.messages;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import java.awt.Point;

public class PopupMessageSelector {
    private static final int LINE_INTERVAL = 1;
    private final Editor editor;
    private final Editor currentComponentEditor;
    private SelectionModel selectionModel;
    private Point point;


    public PopupMessageSelector(AnActionEvent event) {
        this.editor = event.getData(PlatformDataKeys.EDITOR);
        this.currentComponentEditor = PlatformDataKeys.EDITOR.getData(event.getDataContext());

        if (editor != null) {
            this.selectionModel = editor.getSelectionModel();
            this.point = editor.visualPositionToXY(toVisualPosition());
        }
    }

    @Nullable
    public Point extractPoint() {
        return point;
    }

    public JComponent getCurrentComponent() {
        if (currentComponentEditor != null) {
            return currentComponentEditor.getContentComponent();
        }

        return null;
    }

    public String getSelectedText() {
        if (editor != null) {
            String blockSelectedText = selectionModel.getSelectedText();

            return StringUtils.isNotEmpty(blockSelectedText) ? blockSelectedText : getAutoSelectedText(selectionModel);
        }

        return "";
    }

    private String getAutoSelectedText(SelectionModel selectionModel) {
        selectionModel.selectWordAtCaret(false);
        return selectionModel.getSelectedText();
    }

    private VisualPosition toVisualPosition() {
        int line = 0;
        int column = 0;

        if (editor != null) {
            VisualPosition start = selectionModel.getSelectionStartPosition();
            VisualPosition end = selectionModel.getSelectionEndPosition();

            if (end != null && start != null) {
                line = end.getLine() + LINE_INTERVAL;
                column = (start.getColumn() + end.getColumn()) / 2;
            }
        }

        return new VisualPosition(line, column);
    }
}
