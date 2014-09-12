package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;

/**
 * @author alan
 */
public class CollapseWhitespaceToNothing extends AnAction {

  String replaceWith() {
    return "";
  }

  public void actionPerformed(AnActionEvent e) {
    Editor editor = e.getData(DataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    final EditorImpl ed = (EditorImpl) editor;
    CharSequence cs = ed.getDocument().getCharsSequence();

    int currCaretOffset = editor.getCaretModel().getOffset();

    int line = editor.getCaretModel().getLogicalPosition().line;
    int i1 = ed.getDocument().getLineStartOffset(line);
    int i2 = ed.getDocument().getLineEndOffset(line);

    if (i1 == i2) {
      return;
    }

    ReadonlyStatusHandler.getInstance(e.getProject()).ensureFilesWritable();

    int lowOffset = currCaretOffset - 1;
    while (Character.isWhitespace(cs.charAt(lowOffset)) && lowOffset >= i1) {
      lowOffset--;
    }
    int highOffset = currCaretOffset;
    while (Character.isWhitespace(cs.charAt(highOffset)) && highOffset < i2) {
      highOffset++;
    }

//    ed.getDocument().replaceString(lowOffset + 1, highOffset, replaceWith());

    final int start = lowOffset + 1;
    final int end = highOffset;

    final Application application = ApplicationManager.getApplication();
    Runnable runnable = new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            ed.getDocument().replaceString(start, end, replaceWith());
          }
        });
      }
    };

    CommandProcessor.getInstance().executeCommand(e.getProject(), runnable, "RTG.collapsewhitespace", null);
  }
}
