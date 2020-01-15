package edu.cs3500.spreadsheets.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.cs3500.spreadsheets.controller.BasicController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * Class represents the mouse listener.
 */
public class UserMouseListener extends MouseAdapter {
  private EditableView v;

  UserMouseListener(EditableView view) {
    this.v = view;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    String s;
    Coord c = new Coord((e.getX() / 50 + 1) * (v.startCol) + (v.horScrollLocation / 50),
            (e.getY() / 25 + 1) * (v.startRow) + (v.vertScrollLocation / 25));
    try {
      Cell x = (Cell) v.model.getCellAt(c);
      s = x.displayAsFormula();
    }
    catch (Exception n) {
      s = "";
    }
    BasicController.change(v, v.model, c,
            v.startRow, v.startCol, s);
  }
}