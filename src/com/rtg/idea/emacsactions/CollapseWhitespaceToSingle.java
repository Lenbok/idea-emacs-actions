package com.rtg.idea.emacsactions;

/**
 * @author alan
 */
public class CollapseWhitespaceToSingle extends CollapseWhitespaceToNothing {

  @Override
  String replaceWith() {
    return " ";
  }
}
