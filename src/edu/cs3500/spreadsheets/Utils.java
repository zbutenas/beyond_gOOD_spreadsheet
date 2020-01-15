package edu.cs3500.spreadsheets;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

/**
 * This class holds useful functions that I found myself constantly repeating.
 */
public class Utils {

  /**
   * Creates a new instance of a utility.
   */
  public Utils() {
    // Nothing to be constructed.
  }

  /**
   * Determines if this symbol is a valid cell reference (EX: A1, A20, AA40).
   *
   * @param c the given cell.
   * @return whether it matches the pattern of a cell reference.
   */
  public boolean checkCellSymbol(Cell c) {
    return c.toString().matches("([A-Za-z]+)([1-9][0-9]*)");
  }

  /**
   * Produces the coordinate of the cell given a cell name reference.
   *
   * @param c the given cell.
   * @return the coordinate of that cell name.
   */
  public Coord cellToCoord(Cell c) {
    Pattern p = Pattern.compile("[A-Z]+|\\d+");
    Matcher m = p.matcher(c.toString());
    ArrayList<String> coordinates = new ArrayList<>();
    while (m.find()) {
      coordinates.add(m.group());
    }
    return new Coord(Coord.colNameToIndex(coordinates.get(0)),
            Integer.parseInt(coordinates.get(1)));
  }

  /**
   * Checks to see if the given cell is an accurate cell block reference.
   *
   * @param c the given cell.
   * @return whether it is an accurate cell block reference.
   */
  public boolean checkCellBlock(Cell c) {
    String r = "([A-Za-z]+)([1-9][0-9]*)";
    if (c.toString().contains(":")) {
      String[] s1 = c.toString().split(":");
      return s1.length == 2 && s1[0].matches(r) && s1[1].matches(r);
    }
    return false;
  }

  /**
   * This function will determine if the symbol is valid.
   *
   * @param s the given string.
   * @return whether the symbol is valid.
   */
  public boolean checkSymbol(String s) {
    if (s.equals("PRODUCT") || s.equals("SUM") || s.equals("JOIN") || s.equals("<")) {
      return true;
    } else if (checkCellSymbol(new CString(s)) || s.substring(0, 1).equals("\"") &&
            s.substring(s.length() - 1).equals("\"") || checkIfColRef(s)) {
      return true;
    } else {
      return checkCellBlock(new CString(s));
    }
  }

  /**
   * Checks to see if the string is an accurate col reference.
   * @param s the given strong.
   * @return whether the reference is valid.
   */
  public boolean checkIfColRef(String s) {
    String r = "([A-Za-z])";
    if (s.contains(":")) {
      String[] s1 = s.split(":");
      return s1[0].matches(r) && s1[1].matches(r);
    }
    return false;
  }
}
