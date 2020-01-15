import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CFormula;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Holds the tests for the {@link Cell} values.
 */
public class CellTest {

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsStringNum() {
    Cell c = new CNumber(10);
    c.evaluateAsString();
  }

  @Test
  public void testEvalAsStringString() {
    Cell c = new CString("10");
    assertEquals(c.evaluateAsString(), "10");
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsStringBool() {
    Cell c = new CBoolean(false);
    c.evaluateAsString();
  }

  @Test
  public void testEvalAsNumNum() {
    Cell c = new CNumber(10.);
    assertEquals(c.evaluateAsNumber(), 10., .00001);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsNumString() {
    Cell c = new CString("10");
    c.evaluateAsNumber();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsNumBool() {
    Cell c = new CBoolean(false);
    c.evaluateAsNumber();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsBoolNum() {
    Cell c = new CNumber(10.);
    c.evaluateAsBoolean();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testEvalAsBoolString() {
    Cell c = new CString("10");
    c.evaluateAsBoolean();
  }

  @Test
  public void testEvalAsBoolBool() {
    Cell c = new CBoolean(false);
    assertFalse(c.evaluateAsBoolean());
  }

  @Test
  public void testFormula() {
    List<Cell> cl = new ArrayList<>();
    cl.add(new CNumber(10));
    Cell c = new CFormula(cl, new CNumber(10));
    assertEquals(c.evaluateAsNumber(), 10, .0001);
  }

  @Test (expected = NullPointerException.class)
  public void testBlankCell() {
    Cell c = null;
    c.evaluateAsNumber();
  }

  @Test
  public void numAsString() {
    Cell c = new CNumber(10);
    assertEquals(c.toString(), "10.0");
  }

  @Test
  public void boolAsString() {
    Cell c = new CBoolean(false);
    assertEquals(c.toString(), "false");
  }

  @Test
  public void stringAsString() {
    Cell c = new CString("false");
    assertEquals(c.toString(), "false");
  }
}