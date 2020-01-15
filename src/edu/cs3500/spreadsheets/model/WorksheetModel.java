package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;
import edu.cs3500.spreadsheets.view.Graphs;
import edu.cs3500.spreadsheets.view.IGraphs;

/**
 * This interface represents the model for the worksheet application. It maintains the state and
 * updates the worksheet.
 *
 * @param <K> represents a the current type of cell in use.
 */
public interface WorksheetModel<K> {

  /**
   * Updates the current spreadsheet. Replaces the cell specified by a coordinate with the updated
   * value. Or creates a new cell if the specified coordinate does not exist.
   *
   * @param coordinate the location of the cell.
   * @param value      the updated value of this cell.
   */
  void changeCell(Coord coordinate, String value);

  /**
   * Returns the cell specified from a given coordinate.
   *
   * @param coordinate the location of the cell.
   * @return the cell at this coordinate.
   */
  K getCellAt(Coord coordinate);

  /**
   * This function multiplies the values of all of its arguments.
   *
   * @param contents the references to values.
   * @return the final product.
   */
  CNumber product(List<Cell> contents);

  /**
   * This function sums the values of all of its arguments.
   *
   * @param contents the references to values.
   * @return the final sum.
   */
  CNumber sum(List<Cell> contents);

  /**
   * This function joins two String values together.
   *
   * @param contents the reference to the first value.
   * @return the joined String.
   * @throws IllegalArgumentException if either argument is not a String or contents !=2.
   */
  CString join(List<Cell> contents) throws IllegalArgumentException;

  /**
   * This function returns whether the first value is less than the second.
   *
   * @param contents the reference to the first value.
   * @return a boolean value.
   * @throws IllegalArgumentException if either value is not a number or if contents !=2.
   */
  CBoolean lessThan(List<Cell> contents) throws IllegalArgumentException;

  /**
   * Allows us to iterate through the spreadsheet in other classes
   * due to the fact that it returns the last key in use.
   * @return the last key.
   */
  int getLastKey();

  /**
   * This function will evaluate a cell block reference. It will first determine if the cell block
   * is valid. Then, it will return a list of all of the cells located \ in that cell block.
   *
   * @param start the 'upper left' hand side of the cell rectangle.
   * @param end   the 'lower right' hand side of the cell rectangle.
   * @return a list of all of the cells in the cell block.
   * @throws IllegalArgumentException if the first cell is greater than the second cell.
   */
  List<Cell> rectangle(Coord start, Coord end);

  /**
   * This function will add a graph to the model.
   * @param g the graph to be stored.
   */
  void changeGraph(Graphs g);

  /**
   * This function will retrieve the list of graphs from the model.
   * @return the current list of graphs.
   */
  ArrayList<IGraphs> getGraphs();
}
