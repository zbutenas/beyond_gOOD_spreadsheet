package edu.cs3500.spreadsheets.model.cell;

import java.util.List;

/**
 * Represents a Cell. A Cell has a value which is one of: - Boolean - Double - String - Formula: In
 * the form of an {@link edu.cs3500.spreadsheets.sexp.Sexp} All processing of a Cell is done through
 * the visitor pattern.
 */
public interface Cell {
  /**
   * Evaluates the value of that cell for a String function.
   *
   * @return the String of an {@link Cell}.
   * @throws IllegalArgumentException if a non-{@link CString} is attempted to be evaluate.
   */
  String evaluateAsString() throws IllegalArgumentException;

  /**
   * Evaluates the value of that cell for a boolean function.
   *
   * @return the Boolean of an {@link Cell}.
   * @throws IllegalArgumentException if a non-{@link CBoolean} is attempted to be evaluate.
   */
  boolean evaluateAsBoolean() throws IllegalArgumentException;

  /**
   * Evaluates the value of that cell for a number function.
   *
   * @return the number of an {@link Cell}.
   * @throws IllegalArgumentException if a non-{@link CNumber} is attempted to be evaluate.
   */
  double evaluateAsNumber() throws IllegalArgumentException;

  /**
   * Gets the formula of the formula cell.
   *
   * @return the list of Cell values that comprise the formula.
   * @throws IllegalArgumentException if a formula is tried to be taken from a non formula cell.
   */
  List<Cell> getFormula() throws IllegalArgumentException;

  /**
   * Displays the cells formula.
   * @return the formula.
   */
  String displayAsFormula();
}








