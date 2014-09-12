package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;

/**
 * Move to first non-whitespace character on current line, emacs-style.
 * @author alan
 */
public class MoveToIndent extends AnAction {
  public void actionPerformed(AnActionEvent e) {
    Editor editor = e.getData(DataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    EditorImpl ed = (EditorImpl) editor;
    CharSequence cs = ed.getDocument().getCharsSequence();

    int line = editor.getCaretModel().getLogicalPosition().line;
    int i = ed.getDocument().getLineStartOffset(line);
    int i2 = cs.length();
    if (line + 1 < ed.getDocument().getLineCount()) {
      i2 = ed.getDocument().getLineStartOffset(line + 1);
    }
    while (i < i2 && Character.isWhitespace(cs.charAt(i))) {
      i++;
    }
    editor.getCaretModel().moveToOffset(i);
  }
}
