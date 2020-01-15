package edu.cs3500.spreadsheets.model.cell;

import java.util.List;
import java.util.Objects;

/**
 * A numeric constant {@link Cell}.
 */
public class CNumber implements Cell {
  private double num;

  public CNumber(double num) {
    this.num = num;
  }

  @Override
  public String evaluateAsString() {
    throw new IllegalArgumentException("Attempted to use a number as a String.");
  }

  @Override
  public double evaluateAsNumber() {
    return this.num;
  }

  @Override
  public boolean evaluateAsBoolean() {
    throw new IllegalArgumentException("Attempted to use a number as a Boolean.");
  }

  @Override
  public String toString() {
    return Double.toString(this.num);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CNumber cNumber = (CNumber) o;
    return Double.compare(cNumber.num, num) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(num);
  }

  @Override
  public List<Cell> getFormula() {
    throw new IllegalArgumentException("Cannot take a formula from a number cell");
  }

  @Override
  public String displayAsFormula() {
    return this.toString();
  }
}
