package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

/**
 * Move to first non-whitespace character on current line, emacs-style.
 * @author alan
 */
public class MoveToIndent extends EditorAction {

  public MoveToIndent(EditorActionHandler defaultHandler) {
    super(defaultHandler);
  }

  public MoveToIndent() {
    this(new MoveHandler());
  }

  static class MoveHandler extends EditorWriteActionHandler {
    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
      Document document = editor.getDocument();

      if (editor == null || document == null) {
        return;
      }
      CharSequence cs = document.getCharsSequence();

      int line = editor.getCaretModel().getLogicalPosition().line;
      int i = document.getLineStartOffset(line);
      int i2 = cs.length();
      if (line + 1 < document.getLineCount()) {
        i2 = document.getLineStartOffset(line + 1);
      }
      while (i < i2 && Character.isWhitespace(cs.charAt(i))) {
        i++;
      }
      editor.getCaretModel().moveToOffset(i);
    }
  }
}
