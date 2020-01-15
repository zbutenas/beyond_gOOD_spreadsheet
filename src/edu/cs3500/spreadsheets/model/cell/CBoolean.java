package edu.cs3500.spreadsheets.model.cell;

import java.util.List;
import java.util.Objects;

/**
 * A boolean constant {@link Cell}.
 */
public class CBoolean implements Cell {
  private boolean val;

  public CBoolean(boolean val) {
    this.val = val;
  }

  @Override
  public String evaluateAsString() {
    throw new IllegalArgumentException("Attempted to use a Boolean as a String.");
  }

  @Override
  public double evaluateAsNumber() {
    throw new IllegalArgumentException("Attempted to use a Boolean as a Number.");
  }

  @Override
  public boolean evaluateAsBoolean() {
    return this.val;
  }

  @Override
  public String toString() {
    return Boolean.toString(this.val);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CBoolean cBoolean = (CBoolean) o;
    return val == cBoolean.val;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public List<Cell> getFormula() {
    throw new IllegalArgumentException("Cannot take a formula from a Boolean cell");
  }

  @Override
  public String displayAsFormula() {
    return this.toString();
  }
}
