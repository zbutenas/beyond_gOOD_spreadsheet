package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * The button listener for the graphs.
 */
public class GraphButtonListener implements ActionListener {
  private JTextField j;
  private final WorksheetModel m;

  /**
   * Constructs a new instance of the listener based on the given command and the model.
   * @param command the given command.
   * @param m the model.
   */
  GraphButtonListener(JTextField command, WorksheetModel m) {
    this.j = command;
    this.m = m;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(" Bar Graph ")) {
      Graphs barGraph = new Graphs(j.getText(), m);
      barGraph.makeGraph();
      BasicController.addGraph(barGraph, m);
    }
  }
}
