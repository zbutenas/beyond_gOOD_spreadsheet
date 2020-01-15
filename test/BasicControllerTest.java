import org.junit.Test;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.controller.IController;
import edu.cs3500.spreadsheets.model.Builder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.view.EditableView;
import edu.cs3500.spreadsheets.view.Graphs;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the controller. The controller has three functionality's:
 *        - To open a new view, based on a given model.
 *        - To open change the appearance of the view (Whether that be by changing the contents
 *                of a cell, changing the highlighted cell, or scrolling infinitely.
 *        - To mutate the model by changing one of the cells.
 * Note that the only method in the controller that can be tested is the editing (mutating)
 * of the model. The other two methods return a rendered Java.Swing graphic which we
 * were told cannot be tested. I found no need to create a mock-model.
 */
public class BasicControllerTest {

  @Test
  public void testEditModelThroughController() {
    EditableView v = new EditableView(null, 1, 1, new Coord(1, 1), "");
    Builder b = new Builder();
    WorksheetModel m = b.createWorksheet();
    BasicController c = new BasicController(v, m);
    BasicController.edit(m, new Coord(1, 1), "Hey");
    Cell x = new CString("Hey");
    assertEquals(x, m.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testEditModelTwoThroughController() {
    EditableView v = new EditableView(null, 1, 1, new Coord(5, 5), "");
    Builder b = new Builder();
    WorksheetModel m = b.createWorksheet();
    BasicController c = new BasicController(v, m);
    BasicController.edit(m, new Coord(5, 5), "10");
    Cell x = new CNumber(10.0);
    assertEquals(x, m.getCellAt(new Coord(5, 5)));
  }

  @Test
  public void testEditModelThreeThroughControllerCellOffScreen() {
    EditableView v = new EditableView(null, 1, 1, new Coord(150, 150), "");
    Builder b = new Builder();
    WorksheetModel m = b.createWorksheet();
    BasicController c = new BasicController(v, m);
    BasicController.edit(m, new Coord(150, 150), "false");
    Cell x = new CBoolean(false);
    assertEquals(x, m.getCellAt(new Coord(150, 150)));
  }

  // Tests the controller functionality for the graphs
  @Test
  public void testGraphController() {
    EditableView v = new EditableView(null, 1, 1, new Coord(150, 150), "");
    Builder b = new Builder();
    WorksheetModel m = b.createWorksheet();
    BasicController c = new BasicController(v, m);
    Graphs g = new Graphs("A1:B2", m);
    BasicController.addGraph(g, m);
    assertEquals(m.getGraphs().get(0), g);
  }

}