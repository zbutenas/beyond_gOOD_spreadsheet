package edu.cs3500.spreadsheets.model.cell;

import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpToList;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a formula which consists of a list of Cells (values) which comprises the formula. It
 * also has another field for a Cell value which will be the outcome of the formula.
 */
public class CFormula implements Cell {
  private List<Cell> c;
  private Cell w;

  public CFormula(Sexp formula) {
    this.c = transform(formula);
    this.w = null;
  }

  public CFormula(List<Cell> formula, Cell w) {
    this.c = formula;
    this.w = w;
  }

  @Override
  public String evaluateAsString() {
    if (w != null) {
      return w.evaluateAsString();
    }
    return "";
  }

  @Override
  public boolean evaluateAsBoolean() {
    if (w != null) {
      return w.evaluateAsBoolean();
    }
    return false;
  }

  @Override
  public double evaluateAsNumber() {
    if (w != null) {
      return w.evaluateAsNumber();
    }
    return 1.;
  }

  @Override
  public List<Cell> getFormula() {
    return this.c;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.w == null || getClass() != o.getClass()) {
      return false;
    }
    CFormula cFormula = (CFormula) o;
    return w.equals(cFormula.w) && c.equals(cFormula.c);
  }

  @Override
  public String toString() {
    if (w != null) {
      return w.toString();
    }
    return "null";
  }

  @Override
  public int hashCode() {
    return Objects.hash(w);
  }

  @Override
  public String displayAsFormula() {
    StringBuilder s = new StringBuilder();
    s.append("=");
    for (Cell c : this.c) {
      if (c == null) {
        s.append(")");
      }
      else {
        if (c.toString().equals("SUM") || c.toString().equals("PRODUCT") ||
                c.toString().equals("JOIN") || c.toString().equals("<")) {
          s.append("(");
          s.append(c.toString());
          s.append(" ");
        }
        else {
          s.append(c.toString());
          s.append(" ");
        }
      }
    }
    return s.toString();
  }

  /**
   * Transforms the given {@link Sexp} into a list of Cell values representing the formula of this
   * cell.
   *
   * @param f the given {@link Sexp}
   * @return the list of Cell values.
   */
  private List<Cell> transform(Sexp f) {
    SexpVisitor<List<Cell>> v = new SexpToList();
    return f.accept(v);
  }
}
