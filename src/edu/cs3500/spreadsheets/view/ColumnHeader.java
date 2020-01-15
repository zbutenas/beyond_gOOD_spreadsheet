package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Class represents the column headers in the view.
 */
public class ColumnHeader extends JPanel {
  private int col;
  private int start;

  ColumnHeader(int c, int s) {
    this.col = c;
    this.start = s;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    if (this.col != this.start - 1) {
      g2d.drawString(Coord.colIndexToName(this.col), 2, 18);
    }
    this.setBackground(Color.lightGray);
  }
}
