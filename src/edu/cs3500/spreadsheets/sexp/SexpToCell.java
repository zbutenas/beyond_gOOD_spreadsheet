package edu.cs3500.spreadsheets.sexp;

import java.util.List;

import edu.cs3500.spreadsheets.Utils;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * This class creates an {@link Cell} value from an {@link Sexp}. While evaluating an {@link Sexp}
 * it will check for the validity of symbols.
 */
public class SexpToCell implements SexpVisitor<Cell> {
  @Override
  public Cell visitBoolean(boolean b) {
    return new CBoolean(b);
  }

  @Override
  public Cell visitNumber(double d) {
    return new CNumber(d);
  }

  @Override
  public Cell visitSList(List<Sexp> l) {
    return null;
  }

  @Override
  public Cell visitSymbol(String s) {
    Utils u = new Utils();
    if (u.checkSymbol(s)) {
      return new CString(s);
    }
    throw new IllegalArgumentException("Symbol is not recognized.");
  }

  @Override
  public Cell visitString(String s) {
    return new CString(s);
  }

}
