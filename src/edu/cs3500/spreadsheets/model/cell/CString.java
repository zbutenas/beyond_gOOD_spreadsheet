package edu.cs3500.spreadsheets.model.cell;

import java.util.List;
import java.util.Objects;

/**
 * A string Cell {@link Cell}. Contains the value of this cell.
 */
public class CString implements Cell {
  private String val;

  public CString(String val) {
    this.val = val;
  }

  @Override
  public String evaluateAsString() {
    return this.val;
  }

  @Override
  public double evaluateAsNumber() {
    throw new IllegalArgumentException("Attempted to use a String as a Number.");
  }

  @Override
  public boolean evaluateAsBoolean() {
    throw new IllegalArgumentException("Attempted to use a String as a Boolean.");
  }

  @Override
  public String toString() {
    return this.val;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CString cString = (CString) o;
    return val.equals(cString.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public List<Cell> getFormula() {
    throw new IllegalArgumentException("Cannot take a formula from a String cell");
  }

  @Override
  public String displayAsFormula() {
    return this.toString();
  }

}
