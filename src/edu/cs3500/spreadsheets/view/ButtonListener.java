package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * This class holds all of the button listeners.
 */
public class ButtonListener implements ActionListener {
  private JTextField j;
  private EditableView v;

  ButtonListener(JTextField currentTextBox, EditableView view) {
    this.j = currentTextBox;
    this.v = view;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(" Next 100 Rows ")) {
      BasicController.change(v, v.model, v.currentSelectedCell,
              v.startRow + 100, v.startCol, v.currentTextBox);
    }
    if (e.getActionCommand().equals(" Next 26 Cols ")) {
      BasicController.change(v, v.model, v.currentSelectedCell,
              v.startRow, v.startCol + 26, v.currentTextBox);
    }
    if (e.getActionCommand().equals(" Prev 100 Rows ")) {
      if (v.startRow - 100 > 0) {
        BasicController.change(v, v.model, v.currentSelectedCell,
                v.startRow - 100, v.startCol, v.currentTextBox);
      }
    }
    if (e.getActionCommand().equals(" Prev 26 Cols ")) {
      if (v.startCol - 26 > 0) {
        BasicController.change(v, v.model, v.currentSelectedCell,
                v.startRow, v.startCol - 26, v.currentTextBox);
      }
    }
    if (e.getActionCommand().equals(" Yes ")) {
      WorksheetModel editedModel =
              BasicController.edit(v.model, v.currentSelectedCell, j.getText());
      Cell c = (Cell) editedModel.getCellAt(v.currentSelectedCell);
      String s;
      if (c != null) {
        s = c.displayAsFormula();
      }
      else {
        s = j.getText();
      }
      BasicController.change(v, editedModel, v.currentSelectedCell,
             v.startRow, v.startCol, s);
    }
    if (e.getActionCommand().equals(" No ")) {
      BasicController.change(v, v.model, v.currentSelectedCell,
              v.startRow, v.startCol, "");
    }
  }
}
