package edu.cs3500.spreadsheets;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Builder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.view.EditableView;
import edu.cs3500.spreadsheets.view.IView;
import edu.cs3500.spreadsheets.view.TextualView;
import edu.cs3500.spreadsheets.view.VisualView;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {

    Utils u = new Utils();

    // Evaluate command lines with first argument "-in"
    if (args[0].equals("-in")) {

      // Try to read the file and print out the bad cells, if there is any.
      try {

        // Builds the model from the given file name
        Readable readable = new FileReader(args[1]);
        Builder b = new Builder();
        WorksheetModel model = WorksheetReader.read(b, readable);
        ArrayList<String> sl = b.troubleCells;
        for (String s : sl) {
          System.out.println("Error in cell " + s);
        }

        // If -eval is mentioned, evaluate the cell.
        switch (args[2]) {

          case "-eval":
            // Get the requested cell.
            String cell = args[3];
            Cell c = (Cell) model.getCellAt(u.cellToCoord(new CString(cell)));

            // If there were no errors.
            if (sl.size() == 0) {
              try {
                System.out.print(String.format("%f", c.evaluateAsNumber()));
              } catch (Exception e) {
                System.out.print(String.format("%s", c.toString()));
              }
            }
            return;

          case "-save":
            PrintWriter p = new PrintWriter(args[3]);
            IView v = new TextualView(model, p);
            v.render();
            p.close();
            return;

          case "-gui":
            VisualView v2 = new VisualView(model, 1, 1);
            v2.render();
            return;

          case "-edit":
            new BasicController(new EditableView(null, 1, 1,
                    new Coord(1, 1), ""), model).open();
            return;

          default:
        }

      } catch (IOException e) {
        throw new IllegalArgumentException("Error.");
      }
    } else if (args[0].equals("-gui")) {
      Builder b = new Builder();
      WorksheetModel<BasicWorksheet> model = b.createWorksheet();
      VisualView v = new VisualView(model, 1, 1);
      v.render();
    } else if (args[0].equals("-edit")) {
      Builder b = new Builder();
      WorksheetModel<BasicWorksheet> model = b.createWorksheet();
      new BasicController(new EditableView(null, 1, 1, new Coord(1, 1), ""), model).open();
    }
  }
}
