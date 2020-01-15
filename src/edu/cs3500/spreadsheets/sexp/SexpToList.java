package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.Utils;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * This class will transform an {@link Sexp} to a list of {@link Cell} values. While evaluating an
 * {@link Sexp} it will check for the validity of symbols.
 */
public class SexpToList implements SexpVisitor<List<Cell>> {

  @Override
  public List<Cell> visitBoolean(boolean b) {
    List<Cell> c = new ArrayList<>();
    c.add(new CBoolean(b));
    return c;
  }

  @Override
  public List<Cell> visitNumber(double d) {
    List<Cell> c = new ArrayList<>();
    c.add(new CNumber(d));
    return c;
  }

  @Override
  public List<Cell> visitSList(List<Sexp> l) {
    ArrayList<Cell> c = new ArrayList<>();
    for (Sexp s : l) {
      if (s instanceof SList) {
        c.addAll(s.accept(new SexpToList()));
      } else {
        c.add(s.accept(new SexpToCell()));
      }
    }
    c.add(null);
    return c;
  }

  @Override
  public List<Cell> visitSymbol(String s) {
    List<Cell> c = new ArrayList<>();
    Utils u = new Utils();
    if (u.checkSymbol(s)) {
      c.add(new CString(s));
      return c;
    }
    throw new IllegalArgumentException("Symbol is not recognized.");
  }

  @Override
  public List<Cell> visitString(String s) {
    List<Cell> c = new ArrayList<>();
    c.add(new CString(s));
    return c;
  }
}
