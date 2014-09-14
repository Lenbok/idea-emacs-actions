package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.editor.impl.EditorImpl;

/**
 * Joins line to previous and fix up whitespace on join, emacs-style.
 * @author len
 */
public class DeleteIndentation extends EditorAction {

  public DeleteIndentation(EditorActionHandler defaultHandler) {
    super(defaultHandler);
  }

  public DeleteIndentation() {
    this(new DeleteHandler());
  }

  static class DeleteHandler extends EditorWriteActionHandler {
    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
      Document document = editor.getDocument();

      if (editor == null || document == null || !document.isWritable()) {
        return;
      }
      int line = editor.getCaretModel().getLogicalPosition().line;
      if (line == 0) {
        return;
      }

      final EditorImpl ed = (EditorImpl) editor;
      int s1 = ed.getDocument().getLineStartOffset(line - 1);
      int e1 = ed.getDocument().getLineEndOffset(line - 1);

      int s2 = ed.getDocument().getLineStartOffset(line);
      int e2 = ed.getDocument().getLineEndOffset(line);

      CharSequence cs = ed.getDocument().getCharsSequence();
      int lowOffset = e1 - 1;
      while (Character.isWhitespace(cs.charAt(lowOffset)) && lowOffset >= s1) {
        lowOffset--;
      }
      int highOffset = s2;
      while (Character.isWhitespace(cs.charAt(highOffset)) && highOffset < e2) {
        highOffset++;
      }

      final int start = lowOffset + 1;
      final int end = highOffset;
      final String replaceWith = start != s1 && end != e2 ? " " : "";

      ed.getCaretModel().moveToOffset(start);
      ed.getDocument().replaceString(start, end, replaceWith);
    }
  }
}
