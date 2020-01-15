package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs3500.spreadsheets.Utils;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CFormula;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.view.Graphs;
import edu.cs3500.spreadsheets.view.IGraphs;

/**
 * This class represents a basic worksheet. There are two fields, the spreadsheet field which holds
 * all of the cells. It also has a formula counter field which helps iterate through formulas.
 */
public class BasicWorksheet implements WorksheetModel<Cell> {
  private HashMap<Integer, Cell> spreadsheet;
  private int formulaCounter;
  private ArrayList<IGraphs> graphs;

  BasicWorksheet(HashMap<Integer, Cell> spreadsheet) {
    this.spreadsheet = spreadsheet;
    this.formulaCounter = 0;
    evaluateFormulas();
    this.graphs = new ArrayList<>();
  }

  @Override
  public void changeCell(Coord coordinate, String value) {
    Cell c;
    if (value.substring(0, 1).equals("=")) {
      new Parser();
      c = new CFormula(Parser.parse(value.substring(1)));
    } else {
      try {
        c = new CNumber(Double.parseDouble(value));
      } catch (Exception e) {
        switch (value) {
          case "true":
            c = new CBoolean(true);
            break;
          case "false":
            c = new CBoolean(false);
            break;
          default:
            c = new CString(value);
        }
      }
    }
    this.spreadsheet.put(coordinate.hashCode(), c);
    evaluateFormulas();
  }

  @Override
  public Cell getCellAt(Coord coordinate) {
    return this.spreadsheet.get(coordinate.hashCode());
  }

  @Override
  public int getLastKey() {
    Set<Integer> keys = spreadsheet.keySet();
    ArrayList<Integer> newKeys = new ArrayList<>(keys);
    return newKeys.get(newKeys.size() - 1) + 1;
  }

  @Override
  public CNumber product(List<Cell> contents) {
    double p = 1.;
    boolean n = false;
    for (Cell c : contents) {
      try {
        p *= c.evaluateAsNumber();
        n = true;
      } catch (Exception ignored) {
      }
    }
    if (n) {
      return new CNumber(p);
    }
    return new CNumber(0.);
  }

  @Override
  public CNumber sum(List<Cell> contents) {
    double s = 0.;
    boolean n = false;
    for (Cell c : contents) {
      try {
        s += c.evaluateAsNumber();
        n = true;
      } catch (Exception ignored) {
      }
    }
    if (n) {
      return new CNumber(s);
    }
    return new CNumber(0.);
  }

  @Override
  public CString join(List<Cell> contents) {
    StringBuilder s = new StringBuilder();
    if (contents.size() != 2) {
      throw new IllegalArgumentException("You can not join more or than two things.");
    }
    for (Cell c : contents) {
      s.append(c.evaluateAsString());
    }
    return new CString(s.toString());
  }

  @Override
  public CBoolean lessThan(List<Cell> contents) {
    if (contents.size() != 2) {
      throw new IllegalArgumentException("You can not compare more or less than two things.");
    }
    return new CBoolean(contents.get(0).evaluateAsNumber() < contents.get(1).evaluateAsNumber());
  }

  @Override
  public List<Cell> rectangle(Coord start, Coord end) {
    if (start.col > end.col || start.row > end.row) {
      throw new IllegalArgumentException("Invalid cell block reference.");
    }
    ArrayList<Cell> cList = new ArrayList<>();
    for (int ii = start.row; ii <= end.row; ++ii) {
      for (int jj = start.col; jj <= end.col; ++jj) {
        cList.add(spreadsheet.get(new Coord(jj, ii).hashCode()));
      }
    }
    return cList;
  }

  @Override
  public void changeGraph(Graphs g) {
    this.graphs.add(g);
  }

  @Override
  public ArrayList<IGraphs> getGraphs() {
    return this.graphs;
  }

  /**
   * When called, this function will evaluate all of the formula 'cells' that are currently in the
   * spreadsheet.
   */
  private void evaluateFormulas() {
    for (Map.Entry mapElement : spreadsheet.entrySet()) {
      Cell c = (Cell) mapElement.getValue();

      // If the current cell is a formula
      if (c instanceof CFormula) {

        // Get the formula
        List<Cell> f = ((CFormula) c).getFormula();
        this.formulaCounter = 0;

        // Before evaluating, check for cycles
        ArrayList<Integer> cl = new ArrayList<>();
        try {
          this.willCauseCycle(f, cl, c);
          Cell t = new CFormula(f, evaluate(f, 0));
          spreadsheet.put((int) mapElement.getKey(), t);
        } catch (IllegalArgumentException e) {
          spreadsheet.put((int) mapElement.getKey(), new CFormula(f, new CString("#ERROR")));
        }
      }
    }
  }

  /**
   * This function evaluates a formula. A formula is signified by an '=' and could be followed with
   * a value (number, String, boolean) or a valid symbol (function call, cell reference). When a
   * valid symbol reference is found, I get all the arguments needed for that function by sending
   * the formula into the getArguments function. This in turn, allows me to recursively progress
   * through the formula, which is necessary if there is multiple function calls as arguments to
   * another function.
   *
   * @param f    the 'formula' as a list of cells.
   * @param curr used to keep track of where we are at in the formula.
   * @return the cell 'value' that this formula produces.
   */
  private Cell evaluate(List<Cell> f, int curr) {
    Utils u = new Utils();
    switch (f.get(curr).toString()) {
      case "PRODUCT":
        formulaCounter++;
        return this.product(getArguments(f, curr + 1));
      case "SUM":
        formulaCounter++;
        return this.sum(getArguments(f, curr + 1));
      case "JOIN":
        formulaCounter++;
        return this.join(getArguments(f, curr + 1));
      case "<":
        formulaCounter++;
        return this.lessThan(getArguments(f, curr + 1));
      default:
    }
    if (u.checkCellSymbol(f.get(curr))) {
      return getCellAt(u.cellToCoord(f.get(curr)));
    }
    return f.get(curr);
  }

  /**
   * This function will produce the correct amount of arguments that a function needs. It iterates
   * through the remaining formula until it hits a 'null' value, which is my indicator that that
   * function has all the arguments. 'null' value basically signifies the end parenthesis.
   *
   * @param f    the 'formula' in the form of a list of {@link Cell}.
   * @param curr the current index. Used to progress through the formula.
   * @return the list of arguments needed for the specified function.
   */
  private List<Cell> getArguments(List<Cell> f, int curr) {
    List<Cell> args = new ArrayList<>();
    Utils u = new Utils();
    for (int ii = curr; ii < f.size(); ++ii) {
      if (f.get(ii) == null) {
        formulaCounter++;
        return args;
      }
      if (f.get(ii).toString().equals("PRODUCT") || f.get(ii).toString().equals("SUM") ||
              f.get(ii).toString().equals("JOIN") || f.get(ii).toString().equals("<")) {
        args.add(evaluate(f, ii));
        ii = formulaCounter - 1;
      } else if (u.checkCellSymbol(f.get(ii))) {
        args.add(spreadsheet.get(u.cellToCoord(f.get(ii)).hashCode()));
        formulaCounter++;
      } else if (u.checkCellBlock(f.get(ii))) {
        String[] s = f.get(ii).toString().split(":");
        List<Cell> cl = rectangle(u.cellToCoord(new CString(s[0])),
                u.cellToCoord(new CString(s[1])));
        this.checkCycleReliance(cl);
        args.addAll(cl);
        formulaCounter++;
      } else if (u.checkIfColRef(f.get(ii).toString())) {
        String[] s = f.get(ii).toString().split(":");
        int lowBound = Coord.colNameToIndex(s[0]);
        int highBound = Coord.colNameToIndex(s[1]);
        List<Cell> cl = new ArrayList<>();
        for (int zz = lowBound; zz <= highBound; zz ++) {
          cl.addAll(this.getElementsInCol(zz));
        }
        this.checkCycleReliance(cl);
        args.addAll(cl);
        formulaCounter++;
      } else {
        args.add(f.get(ii));
        formulaCounter++;
      }
    }
    return args;
  }

  /**
   * This function checks to see if a cells formula contains either an indirect or a direct cycle.
   *
   * @param f        the formula of the cell in question.
   * @param cc       the list of cell names used for the indirect check.
   * @param currCell the original cell we are checking.
   */
  private void willCauseCycle(List<Cell> f, List<Integer> cc, Cell currCell) {
    Utils u = new Utils();
    List<Integer> forIndirect = new ArrayList<>();

    // This part of the function will check to see if there is a direct cycle
    // in this formula. It does this by iterating through the formula of the
    // cell and will return true if it sees a cells name twice.
    for (Cell c : f) {

      // If the cell in the formula is a valid cell reference
      if (c != null && u.checkCellSymbol(c)) {

        // Evaluate the cells as keys
        int cKey = this.getKey(spreadsheet.get(u.cellToCoord(c).hashCode()));
        int currKey = this.getKey(currCell);
        if (spreadsheet.get(u.cellToCoord(c).hashCode()) != null &&
                spreadsheet.get(u.cellToCoord(c).hashCode()).toString().equals("#ERROR")) {
          throw new IllegalArgumentException("Current Cell Reliant on Bad Cell.");
        }

        // If the coordinate of the two cells are equal, a direct cycle will form.
        if (cKey == currKey) {
          throw new IllegalArgumentException("Indirect Cycle Caused.");
        }

        // If the cell in the formula, is also a formula cell... that cell
        // must be evaluated for a possible indirect cycle.
        if (spreadsheet.get(u.cellToCoord(c).hashCode()) instanceof CFormula) {
          forIndirect.add(cKey);
        }
      }

      // If the cell in the formula is a valid column reference
      if (c != null && u.checkIfColRef(c.toString())) {
        // Splits it into two columns
        String[] s = c.toString().split(":");
        int lowBound = Coord.colNameToIndex(s[0]);
        int highBound = Coord.colNameToIndex(s[1]);
        List<Cell> cl = new ArrayList<>();
        for (int zz = lowBound; zz <= highBound; zz ++) {
          cl.addAll(this.getElementsInCol(zz));
        }
        // Iterates through the list of cells
        for (Cell w: cl) {
          if (w instanceof CFormula) {
            int cKey = this.getKey(w);
            int currKey = this.getKey(currCell);
            if (cKey == currKey) {
              throw new IllegalArgumentException("Indirect Cycle Caused.");
            }
            forIndirect.add(cKey);
          }
        }
      }
    }

    // Iterates through the current list of formula cells, and the list of formula cells
    // that have been seen. This will check for an indirect cycle.
    for (int c : forIndirect) {
      for (int k : cc) {
        if (c == k) {
          throw new IllegalArgumentException("Indirect Cycle Caused.");
        }
      }
    }

    // Continues to check for possible cycles in the formula cells found in
    // the current formula.
    for (int k : forIndirect) {
      Cell newCurr = spreadsheet.get(k);
      cc.addAll(forIndirect);
      willCauseCycle(newCurr.getFormula(), cc, newCurr);
    }
  }

  /**
   * Returns the key associated with the given value.
   *
   * @param c the given value.
   * @return the key.
   */
  private int getKey(Cell c) {
    int key = -1;
    for (Integer entry : spreadsheet.keySet()) {
      if (spreadsheet.get(entry) != null && c != null &&
              c.equals(spreadsheet.get(entry))) {
        key = entry;
        break;
      }
    }
    return key;
  }

  /**
   * Returns all the {@link Cell} in a given col.
   * @param i the index of the col in question.
   * @return the values in the col.
   */
  private List<Cell> getElementsInCol(int i) {
    int checker = (i - 1) % 1000000;
    ArrayList<Cell> values = new ArrayList<>();
    for (Map.Entry mapElement : spreadsheet.entrySet()) {
      int c = (int) mapElement.getKey();
      if (c % 1000000 == checker) {
        values.add((Cell) mapElement.getValue());
      }
    }
    return values;
  }

  /**
   * Determines if this {@link Cell} formula is reliant on a bad cell.
   * @param cl the {@link Cell}s in question.
   */
  private void checkCycleReliance(List<Cell> cl) {
    for (Cell c : cl) {
      if (c.toString() != null && c.toString().equals("#ERROR")) {
        throw new IllegalArgumentException("Relies on an error'd cell.");
      }
    }
  }
}
