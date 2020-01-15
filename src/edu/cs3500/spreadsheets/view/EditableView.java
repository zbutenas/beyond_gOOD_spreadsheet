package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Class represents an editable view.
 */
public class EditableView extends JFrame implements IView {
  WorksheetModel model;
  int startRow;
  int startCol;
  Coord currentSelectedCell;
  String currentTextBox;
  private JScrollPane mainScroll;
  int vertScrollLocation;
  int horScrollLocation;

  /**
   * This will construct a new EditableView.
   * @param m the model.
   * @param r the starting row.
   * @param c the starting col.
   * @param s the current cell.
   * @param t the current text box value.
   */
  public EditableView(WorksheetModel m, int r, int c, Coord s, String t) {
    super();
    this.setTitle("Beyond gOOD");
    this.setSize(700, 700);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.model = m;
    this.startRow = r;
    this.startCol = c;
    this.currentSelectedCell = s;
    this.currentTextBox = t;
    this.setVisible(true);
    this.mainScroll = null;
    this.vertScrollLocation = 0;
    this.horScrollLocation = 0;
  }

  /**
   * Allows the controller to mutate the view.
   * @param row the new start row.
   */
  public void changeStartRow(int row) {
    this.startRow = row;
  }

  /**
   * Allows the controller to mutate the view.
   * @param col the new start col.
   */
  public void changeStartCol(int col) {
    this.startCol = col;
  }

  /**
   * Allows the controller to mutate the view.
   * @param c the new selected cell.
   */
  public void changeCurrentSelectedCell(Coord c) {
    this.currentSelectedCell = c;
  }

  /**
   * Allows the controller to mutate the view.
   * @param s the new String in the text box.
   */
  public void changeCurrentTextBox(String s) {
    this.currentTextBox = s;
  }

  /**
   * Allows the controller to mutate the model.
   * @param m the new model.
   */
  public void changeModel(WorksheetModel<BasicWorksheet> m) {
    this.model = m;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (this.mainScroll != null) {
      this.vertScrollLocation = this.mainScroll.getVerticalScrollBar().getValue();
      this.horScrollLocation = this.mainScroll.getHorizontalScrollBar().getValue();
    }
    this.getContentPane().removeAll();
    this.render();
  }

  @Override
  public void render() {
    // Creates the container for the cells
    JScrollPane cellScroll = new JScrollPane(createCells(),
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    cellScroll.addMouseListener(new UserMouseListener(this));
    cellScroll.getVerticalScrollBar().setValue(this.vertScrollLocation);
    cellScroll.getHorizontalScrollBar().setValue(this.horScrollLocation);
    this.mainScroll = cellScroll;

    // Creates the container for the rows
    JScrollPane rowScroll = new JScrollPane(this.createRows());
    rowScroll.getVerticalScrollBar().setModel(cellScroll.
            getVerticalScrollBar().getModel());
    rowScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    rowScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    // Creates the container for the columns
    JScrollPane colScroll = new JScrollPane(this.createColumns());
    colScroll.getHorizontalScrollBar().setModel(cellScroll.
            getHorizontalScrollBar().getModel());
    colScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    colScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    // Creates the buttons and formula bar
    JPanel buttonsFormula = new JPanel();
    buttonsFormula.setPreferredSize(new Dimension(500, 35));
    buttonsFormula.setLayout(new FlowLayout());
    JTextField jt = new JTextField(40);
    JButton accept = new JButton(" Yes ");
    accept.setBorder(new LineBorder(Color.BLACK));
    jt.setText(this.currentTextBox);
    accept.addActionListener(new ButtonListener(jt, this));
    JButton decline = new JButton(" No ");
    decline.setBorder(new LineBorder(Color.BLACK));
    decline.addActionListener(new ButtonListener(jt, this));
    buttonsFormula.add(accept);
    buttonsFormula.add(decline);
    buttonsFormula.add(jt);

    // Adds the buttons for infinite scrolling
    JPanel scrollingButtons = new JPanel();
    scrollingButtons.setLayout(new FlowLayout());
    JButton scrollRowUp = new JButton(" Next 100 Rows ");
    scrollRowUp.setBorder(new LineBorder(Color.BLACK));
    scrollRowUp.addActionListener(new ButtonListener(jt, this));
    scrollingButtons.add(scrollRowUp);
    JButton scrollColUp = new JButton(" Next 26 Cols ");
    scrollColUp.setBorder(new LineBorder(Color.BLACK));
    scrollColUp.addActionListener(new ButtonListener(jt, this));
    scrollingButtons.add(scrollColUp);
    JButton scrollRowDown = new JButton(" Prev 100 Rows ");
    scrollRowDown.setBorder(new LineBorder(Color.BLACK));
    scrollRowDown.addActionListener(new ButtonListener(jt, this));
    scrollingButtons.add(scrollRowDown);
    JButton scrollColDown = new JButton(" Prev 26 Cols ");
    scrollColDown.setBorder(new LineBorder(Color.BLACK));
    scrollColDown.addActionListener(new ButtonListener(jt, this));
    scrollingButtons.add(scrollColDown);

    // Add all of the panels to the frame
    JPanel p = new JPanel();
    p.setPreferredSize(new Dimension(600, 600));
    p.setLayout(new BorderLayout(0, 0));
    p.add(rowScroll, BorderLayout.WEST);
    p.add(cellScroll, BorderLayout.CENTER);
    p.add(colScroll, BorderLayout.NORTH);
    JPanel o = new JPanel();
    o.setLayout(new BorderLayout(0, 0));
    o.add(buttonsFormula, BorderLayout.NORTH);
    o.add(p, BorderLayout.SOUTH);
    JPanel w = new JPanel();
    w.setLayout(new BorderLayout());
    w.add(o, BorderLayout.NORTH);

    // Add a label telling what functions are available
    w.add(new JLabel("\n                          " +
                    "             Available Functions: SUM, PRODUCT, JOIN, <\n"),
            BorderLayout.SOUTH);
    this.add(w, BorderLayout.PAGE_START);

    // Create the text box and button to create a bar graph
    JPanel graphs = new JPanel();
    graphs.setLayout(new FlowLayout());
    JTextField gt = new JTextField(30);
    graphs.add(gt);
    JButton bar = new JButton(" Bar Graph ");
    bar.setBorder(new LineBorder(Color.BLACK));
    bar.addActionListener(new GraphButtonListener(gt, this.model));
    graphs.add(bar);

    // Make more Panels
    JPanel bot = new JPanel();
    bot.setLayout(new BorderLayout());
    bot.add(scrollingButtons, BorderLayout.NORTH);
    bot.add(graphs, BorderLayout.SOUTH);
    this.add(bot, BorderLayout.PAGE_END);
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new GridKeyListener(this));
    this.setResizable(false);
    this.pack();
    this.setVisible(true);

    // Re-paint all the current graphs
    for (Object graph : this.model.getGraphs()) {
      Graphs g = (Graphs) graph;
      g.repaint();
    }
  }

  /**
   * Method creates the column panel.
   * @return the constructed column panel.
   */
  private JPanel createColumns() {
    JPanel cols = new JPanel();
    cols.setPreferredSize(new Dimension(1350, 30));
    cols.setLayout(new GridLayout(1, 26));
    for (int ii = this.startCol - 1; ii < this.startCol + 26; ++ii) {
      cols.add(new ColumnHeader(ii, this.startCol));
    }
    return cols;
  }

  /**
   * Method creates the row panel.
   * @return the constructed row panel.
   */
  private JPanel createRows() {
    JPanel rows = new JPanel();
    rows.setPreferredSize(new Dimension(47, 2500));
    rows.setLayout(new GridLayout(100, 1));
    for (int ii = this.startRow; ii < this.startRow + 100; ++ii) {
      rows.add(new RowHeader(ii));
    }
    return rows;
  }

  /**
   * Method creates the cell panel.
   * @return the constructed cell panel.
   */
  private JPanel createCells() {
    JPanel cells = new JPanel();
    cells.setPreferredSize(new Dimension(1300, 2500));
    cells.setLayout(new GridLayout(100, 26));
    for (int ii = this.startRow; ii <= this.startRow + 99; ++ii) {
      for (int jj = this.startCol; jj <= this.startCol + 25; ++jj) {
        cells.add(new DrawCells(this.model, this.currentSelectedCell, new Coord(jj, ii)));
      }
    }
    return cells;
  }
}
