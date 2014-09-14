package com.rtg.idea.emacsactions;

/**
 * @author alan
 */
public class CollapseWhitespaceToSingle extends CollapseWhitespaceToNothing {

  public CollapseWhitespaceToSingle() {
    super(new CollapseHandler(" "));
  }
}
