package edu.cs3500.spreadsheets.view;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * This class will listen for key events on the worksheet.
 */
public class GridKeyListener implements KeyEventDispatcher {
  private EditableView view;

  GridKeyListener(EditableView v) {
    this.view = v;
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent e) {
    if (e.getID() == KeyEvent.KEY_RELEASED) {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        BasicController.change(view, view.model, new Coord(view.currentSelectedCell.col + 1,
                        view.currentSelectedCell.row), view.startRow, view.startCol,
                view.currentTextBox);
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        if (view.currentSelectedCell.col > 1) {
          BasicController.change(view, view.model, new Coord(view.currentSelectedCell.col - 1,
                          view.currentSelectedCell.row), view.startRow, view.startCol,
                  view.currentTextBox);
        }
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        BasicController.change(view, view.model, new Coord(view.currentSelectedCell.col,
                        view.currentSelectedCell.row + 1), view.startRow, view.startCol,
                view.currentTextBox);
      } else if (e.getKeyCode() == KeyEvent.VK_UP) {
        if (view.currentSelectedCell.row > 1) {
          BasicController.change(view, view.model, new Coord(view.currentSelectedCell.col,
                          view.currentSelectedCell.row - 1), view.startRow, view.startCol,
                  view.currentTextBox);
        }
      } else if (e.getKeyChar() == 46) {
        BasicController.edit(view.model, view.currentSelectedCell, "");
        BasicController.change(view, view.model, new Coord(view.currentSelectedCell.col,
                          view.currentSelectedCell.row), view.startRow, view.startCol,
                  "");
      }
    }
    return false;
  }
}
