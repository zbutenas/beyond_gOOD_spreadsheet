package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Class represents a visible view, cannot be edited.
 */
public class VisualView extends JFrame implements IView {
  private WorksheetModel model;
  private int startRow;
  private int startCol;

  /**
   * Constructs a spreadsheet based on a given model.
   *
   * @param model the given model.
   */
  public VisualView(WorksheetModel model, int startRow, int startCol) {
    super();
    this.setTitle("Spreadsheet");
    this.setSize(700, 700);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.model = model;
    getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2,
            2, 2, Color.black));
    this.startRow = startRow;
    this.startCol = startCol;
  }

  @Override
  public void render() {
    // Used to get the correct cell
    int col = startCol;
    int row = startRow;

    // Container that holds everything
    JPanel overAll = new JPanel();

    // Create the container for the rows and the cells
    JPanel xContainer = new JPanel();
    xContainer.setPreferredSize(new Dimension(1400, 460));
    xContainer.setLayout(new BorderLayout(0, 0));

    // Create the container for the xContainer and the cols
    JPanel yContainer = new JPanel();
    yContainer.setPreferredSize(new Dimension(600, 600));
    yContainer.setLayout(new BorderLayout(0, 0));

    // Create the headers
    JPanel headerContainer = new JPanel();
    headerContainer.setPreferredSize(new Dimension(1350, 30));
    headerContainer.setLayout(new GridLayout(1, 26));
    for (int ii = col - 1; ii < col + 26; ++ii) {
      if (ii == col - 1) {
        headerContainer.add(new ColumnHeader(this.startCol, ii));
      }
    }

    // Create the rows
    JPanel rowContainer = new JPanel();
    rowContainer.setPreferredSize(new Dimension(45, 2500));
    rowContainer.setLayout(new GridLayout(100, 1));
    for (int ii = row; ii < row + 100; ++ii) {
      rowContainer.add(new RowHeader(ii));
    }

    // Adds all of the cells to a container
    JPanel cellContainer = new JPanel();
    cellContainer.setPreferredSize(new Dimension(1300, 2500));
    cellContainer.setLayout(new GridLayout(100, 26));

    // Creates the correct number of cells with hard-coded dimensions
    for (int ii = row; ii <= row + 99; ++ii) {
      for (int jj = col; jj <= col + 25; ++jj) {

        // Creates the regular cells
        cellContainer.add(new DrawCells(this.model, new Coord(ii, jj), new Coord(1, 1)));
      }
    }

    // Add the scrolling for the cells
    JScrollPane mainScrollPane = new JScrollPane(cellContainer,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // Add scrolling for the cols
    JScrollPane colScrollPane = new JScrollPane(headerContainer);
    colScrollPane.getHorizontalScrollBar().setModel(mainScrollPane.
            getHorizontalScrollBar().getModel());
    colScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    colScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    // Add the scrolling for the rows
    JScrollPane rowScrollPane = new JScrollPane(rowContainer);
    rowScrollPane.getVerticalScrollBar().setModel(mainScrollPane.
            getVerticalScrollBar().getModel());
    rowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    rowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    // Adds the button to progress the rows
    JPanel buttonPanel = new JPanel();
    JButton scrollRowUp = new JButton("Next 100 Rows");
    scrollRowUp.setBorder(new LineBorder(Color.BLACK));
    buttonPanel.add(scrollRowUp);

    // Adds the button to progress the cols
    JButton scrollColUp = new JButton("Next 26 Cols");
    scrollColUp.setBorder(new LineBorder(Color.BLACK));
    buttonPanel.add(scrollColUp);

    // Adds the button to regress the rows
    JButton scrollRowDown = new JButton("Prev 100 Rows");
    scrollRowDown.setBorder(new LineBorder(Color.BLACK));
    buttonPanel.add(scrollRowDown);

    // Adds the button to regress the cols
    JButton scrollColDown = new JButton("Prev 26 Cols");
    scrollColDown.setBorder(new LineBorder(Color.BLACK));
    buttonPanel.add(scrollColDown);
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.PAGE_END);

    // Put everything together
    yContainer.add(rowScrollPane, BorderLayout.WEST);
    yContainer.add(mainScrollPane, BorderLayout.CENTER);
    yContainer.add(colScrollPane, BorderLayout.NORTH);
    this.add(yContainer);
    this.setResizable(false);
    this.pack();
    this.setVisible(true);
  }

}
