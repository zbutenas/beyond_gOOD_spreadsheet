package edu.cs3500.spreadsheets.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

/**
 * Class represents the row headers in the view.
 */
public class RowHeader extends JPanel {
  private int row;

  RowHeader(int r) {
    this.row = r;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    g2d.drawString(Integer.toString(this.row), 2, 18);
    this.setBackground(Color.lightGray);
    this.revalidate();
  }
}
