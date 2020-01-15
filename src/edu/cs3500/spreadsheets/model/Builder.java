package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cs3500.spreadsheets.Utils;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CFormula;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.view.Graphs;

/**
 * This class will build a model. It will create a defensive copy of a spreadsheet, for which it
 * sends into the model. It supports the ability to read in text files.
 */
public class Builder implements WorksheetReader.WorksheetBuilder {
  private HashMap<Integer, Cell> spreadsheet;
  public ArrayList<String> troubleCells;
  public ArrayList<String> graphs;

  /**
   * Builds the spreadsheet.
   */
  public Builder() {
    this.spreadsheet = new HashMap<Integer, Cell>();
    this.troubleCells = new ArrayList<>();
    this.graphs = new ArrayList<>();
  }

  @Override
  public Builder createCell(int col, int row, String contents) {
    Utils u = new Utils();
    try {
      if (contents.substring(0, 1).equals("=")) {
        new Parser();
        this.spreadsheet.put(new Coord(col, row).hashCode(),
                new CFormula(Parser.parse(contents.substring(1))));
        return this;
      }
      try {
        this.spreadsheet.put(new Coord(col, row).hashCode(),
                new CNumber(Double.parseDouble(contents)));
      } catch (Exception e) {
        switch (contents) {
          case "true":
            spreadsheet.put(new Coord(col, row).hashCode(),
                    new CBoolean(true));
            break;
          case "false":
            spreadsheet.put(new Coord(col, row).hashCode(),
                    new CBoolean(false));
            break;
          default:
            if (contents.substring(0, 1).equals("\"") &&
                    contents.substring(contents.length() - 1).equals("\"")) {
              this.spreadsheet.put(new Coord(col, row).hashCode(),
                      new CString(contents));
            } else {
              throw new IllegalArgumentException("Symbol not recognized.");
            }
        }
      }
    } catch (Exception ee) {
      troubleCells.add(Coord.colIndexToName(col) + row + ": " + ee.getLocalizedMessage());
    }
    return this;
  }

  public Builder addGraph(String name, String contents) {
    this.graphs.add(contents);
    return this;
  }

  @Override
  public WorksheetModel createWorksheet() {
    BasicWorksheet b = new BasicWorksheet(this.spreadsheet);
    for (String s : this.graphs) {
      Graphs g = new Graphs(s, b);
      b.changeGraph(g);
    }
    return b;
  }
}
