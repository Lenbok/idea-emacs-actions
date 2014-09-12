package com.rtg.idea.emacsactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;

/**
 * Joins line to previous and fix up whitespace on join, emacs-style.
 * @author len
 */
public class DeleteIndentation extends AnAction {

  public void actionPerformed(AnActionEvent e) {
    Editor editor = e.getData(DataKeys.EDITOR);
    if (editor == null) {
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

    ReadonlyStatusHandler.getInstance(e.getProject()).ensureFilesWritable();
    Runnable runnable = new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          public void run() {
            ed.getCaretModel().moveToOffset(start);
            ed.getDocument().replaceString(start, end, replaceWith);
          }
        });
      }
    };
    CommandProcessor.getInstance().executeCommand(e.getProject(), runnable, "RTG.deleteindentation", null);
  }
}
