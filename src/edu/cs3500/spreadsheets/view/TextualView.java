package edu.cs3500.spreadsheets.view;

import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * Class represents the textual view.
 */
public class TextualView implements IView {
  private WorksheetModel model;
  private PrintWriter appendable;

  /**
   * Creates a new textual view.
   * @param model the given model.
   * @param appendable the appendable that it will write to.
   */
  public TextualView(WorksheetModel model, PrintWriter appendable) {
    this.model = model;
    this.appendable = appendable;
  }

  public TextualView(WorksheetModel model) {
    this.model = model;
  }

  @Override
  public String toString() {
    int size = model.getLastKey();
    StringBuilder sb = new StringBuilder();
    for (int ii = 1; ii <= size; ++ii) {
      for (int jj = 1; jj <= size; ++jj) {
        Cell c = (Cell) model.getCellAt(new Coord(ii, jj));
        if (c != null) {
          sb.append(Coord.colIndexToName(ii)).append(jj).append(" ");
          sb.append(c.toString());
          sb.append("\n");
        }
      }
    }

    // Write the graphs back into a file
    for (Object g : model.getGraphs()) {
      sb.append("BAR");
      sb.append("\n");
    }

    return sb.toString();
  }

  @Override
  public void render() {
    appendable.write(toString());
  }
}
