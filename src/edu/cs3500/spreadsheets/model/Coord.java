package edu.cs3500.spreadsheets.model;

/**
 * A value type representing coordinates in a {@link WorksheetModel}.
 */
public class Coord {
  public final int row;
  public final int col;

  /**
   * This constructs a new coordinate, representing a Cell.
   *
   * @param col the col of the cell.
   * @param row the row of the cell.
   */
  public Coord(int col, int row) {
    if (row < 1 || col < 1 || col > 1000000) {
      throw new IllegalArgumentException("Coordinates should be strictly " +
              "positive and less than 100000.");
    }
    this.row = row;
    this.col = col;
  }

  /**
   * Converts from the A-Z column naming system to a 1-indexed numeric value.
   *
   * @param name the column name
   * @return the corresponding column index
   */
  public static int colNameToIndex(String name) {
    name = name.toUpperCase();
    int ans = 0;
    for (int i = 0; i < name.length(); i++) {
      ans *= 26;
      ans += (name.charAt(i) - 'A' + 1);
    }
    return ans;
  }

  /**
   * Converts a 1-based column index into the A-Z column naming system.
   *
   * @param index the column index
   * @return the corresponding column name
   */
  public static String colIndexToName(int index) {
    StringBuilder ans = new StringBuilder();
    while (index > 0) {
      int colNum = (index - 1) % 26;
      ans.insert(0, Character.toChars('A' + colNum));
      index = (index - colNum) / 26;
    }
    return ans.toString();
  }

  @Override
  public String toString() {
    return colIndexToName(this.col) + this.row;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coord coord = (Coord) o;
    return row == coord.row
            && col == coord.col;
  }

  @Override
  public int hashCode() {
    return (col - 1) + ((row - 1) * 1000000);

  }
}
