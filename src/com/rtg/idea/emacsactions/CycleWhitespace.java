package com.rtg.idea.emacsactions;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;

/**
 * @author len
 */
public class CycleWhitespace extends CollapseWhitespaceToNothing {

  public CycleWhitespace() {
    super(new CycleCollapseHandler());
  }

  static class CycleCollapseHandler extends ReplaceHandler {
    final CharSequence[] mReplace = new CharSequence[3];
    int mLastStartPos = -1;
    int mCaretOffset = -1;
    int mCycleLength = 2;
    int mCyclePos = 0;

    CycleCollapseHandler() {
      mReplace[0] = " ";
      mReplace[1] = "";
      mReplace[2] = "nothing-yet";
    }

    @Override
    void doCollapseAction(Document document, CaretModel caret, int currCaretOffset, int start, int end) {
      //balloon("doCollapseAction(" + currCaretOffset + " " + start + " " + end + ") " + mLastStartPos + " " + mCyclePos + " last '" + mReplace[2] + "'");

      if (mLastStartPos != start) { // Should also check if the document has changed
        // Stash the current string and set up the initial cycle state
        mReplace[2] = document.getCharsSequence().subSequence(start, end);
        mLastStartPos = start;
        mCaretOffset = currCaretOffset - start;
        mCycleLength = end - start > 1 ? 3 : 2;
        mCyclePos = end - start == 1 ? 1 : 0;
      }
      document.replaceString(start, end, mReplace[mCyclePos]);
      caret.moveToOffset(start + Math.min(mReplace[mCyclePos].length(), mCaretOffset));
      mCyclePos = ++mCyclePos % mCycleLength;
    }
  }
}
