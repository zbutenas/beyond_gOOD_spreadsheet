package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.cs3500.spreadsheets.Utils;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * This frame class currently holds a bar graph.
 */
public class Graphs extends JFrame implements IGraphs {
  private String s;
  private final WorksheetModel m;

  /**
   * Constructs a new instance of a bar graph.
   * @param s The string command being used for this graph.
   * @param m the model being used for this graph.
   */
  public Graphs(String s, WorksheetModel m) {
    super();
    this.s = s;
    this.m = m;
    this.setTitle("Bar Graph");
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.getContentPane().removeAll();
    this.makeGraph();
  }

  @Override
  public void makeGraph() {
    // Find the coordinates of the rectangle from the given command
    Utils u = new Utils();
    try {
      String[] s1 = s.split(":");
      List values = m.rectangle(u.cellToCoord(new CString(s1[0])),
              u.cellToCoord(new CString(s1[1])));

      // Sort the coordinates based off of some invariants (Less than 10 items) and
      // only rectangle references
      ArrayList<Integer> ints = new ArrayList<>();
      ArrayList<String> labels = new ArrayList<>();
      for (int ii = 0; ii < values.size() / 2; ii++) {
        Cell c = (Cell) values.get(ii);
        String l = c.toString();
        labels.add(l);
      }
      for (int ii = values.size() / 2; ii < values.size(); ii++) {
        Cell c = (Cell) values.get(ii);
        int i = (int) Math.round(c.evaluateAsNumber());
        ints.add(i);
      }

      // Add the bar graph panel to the frame
      this.add(new BarGraph(ints, labels));
      this.setVisible(true);
    } catch (Exception ignored) { }
  }
}
