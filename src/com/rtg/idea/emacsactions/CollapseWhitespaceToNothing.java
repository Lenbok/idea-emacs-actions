package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

/**
 * @author alan
 */
public class CollapseWhitespaceToNothing extends EditorAction {

  public CollapseWhitespaceToNothing(EditorActionHandler defaultHandler) {
    super(defaultHandler);
  }

  public CollapseWhitespaceToNothing() {
    this(new CollapseHandler(""));
  }

  static class CollapseHandler extends EditorWriteActionHandler {
    final String mReplacewith;
    CollapseHandler(String replaceWith) {
      mReplacewith = replaceWith;
    }

    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
      Document document = editor.getDocument();

      if (editor == null || document == null || !document.isWritable()) {
        return;
      }

      int line = editor.getCaretModel().getLogicalPosition().line;
      int i1 = editor.getDocument().getLineStartOffset(line);
      int i2 = editor.getDocument().getLineEndOffset(line);

      if (i1 == i2) {
        return;
      }

      int currCaretOffset = editor.getCaretModel().getOffset();
      CharSequence cs = document.getCharsSequence();
      int lowOffset = currCaretOffset - 1;
      while (Character.isWhitespace(cs.charAt(lowOffset)) && lowOffset >= i1) {
        lowOffset--;
      }
      int highOffset = currCaretOffset;
      while (Character.isWhitespace(cs.charAt(highOffset)) && highOffset < i2) {
        highOffset++;
      }
      final int start = lowOffset + 1;
      final int end = highOffset;
      document.replaceString(start, end, mReplacewith);
    }
  }
}
