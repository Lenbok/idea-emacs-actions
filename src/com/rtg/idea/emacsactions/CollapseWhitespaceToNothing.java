package com.rtg.idea.emacsactions;

import java.awt.Point;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

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

  static abstract class ReplaceHandler extends EditorWriteActionHandler {
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
      doCollapseAction(document, editor.getCaretModel(), currCaretOffset, start, end);
    }

    abstract void doCollapseAction(Document document, CaretModel caret, int currCaretOffset, int start, int end);
  }

  static class CollapseHandler extends ReplaceHandler {
    final String mReplaceWith;

    CollapseHandler(String replaceWith) {
      mReplaceWith = replaceWith;
    }
    @Override
    protected void doCollapseAction(Document document, CaretModel caret, int currCaretOffset, int start, int end) {
      //balloon("doCollapseAction(" + currCaretOffset + " " + start + " " + end);
      document.replaceString(start, end, mReplaceWith);
    }
  }


  static void balloon(String msg) {
    JBPopupFactory.getInstance()
      .createHtmlTextBalloonBuilder(msg, MessageType.INFO, null)
      .setFadeoutTime(7500)
      .createBalloon()
      .show(RelativePoint.fromScreen(new Point(500, 500)), Balloon.Position.atRight);

  }

}
