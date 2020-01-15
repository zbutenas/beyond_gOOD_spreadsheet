package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * Class represents the drawing of the physical cells in a view.
 */
public class DrawCells extends JPanel {
  protected WorksheetModel model;
  private Coord targetCell;
  private Coord current;

  /**
   * Constructs the visual spreadsheet representation given some {@link Cell}.
   * @param m the current model.
   * @param c the current highlighted cell.
   * @param b the current cell being made.
   */
  DrawCells(WorksheetModel m, Coord c, Coord b) {
    this.model = m;
    this.targetCell = c;
    this.current = b;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if (current.equals(targetCell)) {
      this.setBorder(BorderFactory.createLineBorder(Color.red, 2));
      try {
        Cell q = (Cell) this.model.getCellAt(current);
        g2d.drawString(q.toString(), 2, 18);
      } catch (Exception ignored) { }
    }
    else {
      this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
      try {
        Cell q = (Cell) this.model.getCellAt(current);
        g2d.drawString(q.toString(), 2, 18);
      } catch (Exception ignored) { }
    }
    this.revalidate();
  }
}
