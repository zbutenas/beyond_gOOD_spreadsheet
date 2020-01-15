package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Class creates a bar graph from a list of numbers and a list of labels.
 */
public class BarGraph extends JPanel {
  private ArrayList data;
  private ArrayList names;

  BarGraph(ArrayList data, ArrayList names) {
    this.data = data;
    this.names = names;
  }

  @Override
  protected void paintComponent(Graphics g) {
    int frameWidth = 500;
    int frameHeight = 500;

    // Determine spacing
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int size = this.data.size();
    int spacing = frameWidth / size;

    int index = 0;
    int placement = 50;
    for (int ii = 0; ii < data.size(); ++ii) {

      // Place a rectangle to represent the bar
      g2d.drawRect(placement, 450 - (Integer) data.get(index), 50, (Integer) data.get(index));
      String label = (String) names.get(ii);

      // Snip the label before it gets too long.
      if (label.length() > 7) {
        label = label.substring(0, 7);
        label += "...";
      }

      // Add the label to the bottom of the bar
      g2d.drawString(label, placement + 2, 467);

      // Adds the number to the top of the bar
      g2d.drawString(Integer.toString((Integer) data.get(index)), placement + 2,
              463 - (Integer) data.get(index));

      // Draws the two axis lines
      g2d.drawLine(30, 0, 30, 500);
      g2d.drawLine(0, 450, 500, 450);
      index ++;
      placement += spacing;
    }

    // Adds the numbers to the side
    for (int ii = 0; ii < frameHeight; ii += 20) {
      g2d.drawString(Integer.toString(ii), 3, 450 - ii);
    }

    this.setPreferredSize(new Dimension(frameWidth, frameHeight));
  }
}
