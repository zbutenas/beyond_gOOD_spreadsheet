package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.EditableView;
import edu.cs3500.spreadsheets.view.Graphs;
import edu.cs3500.spreadsheets.view.IGraphs;

/**
 * The controller has three functionality's:
 *        - To open a new view, based on a given model.
 *        - To open change the appearance of the view (Whether that be by changing the contents
 *                of a cell, changing the highlighted cell, or scrolling infinitely.
 *        - To mutate the model by changing one of the cells.
 * The way the user tells the controller that it wants a change, is by clicking on a button or
 * the screen. Once that click is registered, the controller method is called. The methods
 * will mutate the view, or the model. The only way something is changed, is to go through
 * the controller.
 */
public class BasicController implements IController {
  private EditableView view;
  private final WorksheetModel<BasicWorksheet> model;

  public BasicController(EditableView v, WorksheetModel<BasicWorksheet> m) {
    this.view = v;
    this.model = m;
  }

  @Override
  public void open() {
    this.view.changeModel(this.model);
    this.view.render();
  }

  /**
   * This method will re-render the given view, based on the given characteristics. This method
   * allows view mutation, without disposing of the previous view.
   *
   * @param view           the given view.
   * @param model          the model to be displayed.
   * @param selectedCell   the current selected cell.
   * @param startRow       the current start row.
   * @param startCol       the current start col.
   * @param textBoxDisplay the current String in the text box.
   */
  public static void change(EditableView view, WorksheetModel<BasicWorksheet> model,
                     Coord selectedCell,
                     int startRow, int startCol, String textBoxDisplay) {
    view.changeModel(model);
    view.changeStartRow(startRow);
    view.changeStartCol(startCol);
    view.changeCurrentSelectedCell(selectedCell);
    view.changeCurrentTextBox(textBoxDisplay);
    view.repaint();
  }

  /**
   * This method will control the editing of cells.
   * @param model the model to be edited.
   * @param changeCell the requested cell to be changed.
   * @return the updated model.
   */
  public static WorksheetModel<BasicWorksheet> edit(WorksheetModel<BasicWorksheet> model,
                                             Coord changeCell, String s) {
    model.changeCell(changeCell, s);
    return model;
  }

  /**
   * Due to the fact that we are mutating the model, in order to add a graph to a model, we
   * rout that functionality through the controller.
   * @param g the graph we are adding.
   * @param m the model we are adding it to.
   */
  public static void addGraph(IGraphs g, WorksheetModel m) {
    m.changeGraph((Graphs) g);
  }

}
