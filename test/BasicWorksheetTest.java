import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.model.Builder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.cell.CBoolean;
import edu.cs3500.spreadsheets.model.cell.CFormula;
import edu.cs3500.spreadsheets.model.cell.CNumber;
import edu.cs3500.spreadsheets.model.cell.CString;
import edu.cs3500.spreadsheets.model.cell.Cell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * This class holds all the tests for the {@link edu.cs3500.spreadsheets.model.BasicWorksheet}.
 */
public class BasicWorksheetTest {
  private Builder b = new Builder();
  private WorksheetModel model = b.createWorksheet();

  @Test
  public void testModelStartsEmpty() {
    assertNull(model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingNumberCell() {
    Cell c = new CNumber(10);
    model.changeCell(new Coord(1, 2), "10");
    assertEquals(c, model.getCellAt(new Coord(1, 2)));
  }

  @Test
  public void testAddCellsToSpreadsheet() {
    for (int ii = 1; ii < 100; ++ii) {
      model.changeCell(new Coord(1, ii), "10");
    }

    for (int ii = 1; ii < 100; ++ii) {
      assertNotNull(model.getCellAt(new Coord(1, ii)));
    }
  }

  @Test
  public void testMakingStringCell() {
    Cell c = new CString("Hello");
    model.changeCell(new Coord(1, 1), "Hello");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingBooleanCell() {
    Cell c = new CBoolean(false);
    model.changeCell(new Coord(1, 1), "false");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaCell() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CNumber(10);
    Cell s3 = new CNumber(12);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(120));
    model.changeCell(new Coord(1, 1), "=(PRODUCT 10 12)");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaCellProductTwice() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("PRODUCT");
    Cell s3 = new CNumber(5);
    Cell s4 = new CNumber(5);
    Cell s6 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(null);
    l.add(s6);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(125));
    model.changeCell(new Coord(1, 1), "=(PRODUCT(PRODUCT 5 5)5)");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaCellProductSum() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CNumber(5);
    Cell s4 = new CNumber(5);
    Cell s6 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(null);
    l.add(s6);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(50));
    model.changeCell(new Coord(1, 1), "=(PRODUCT(SUM 5 5)5)");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaJoin() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("JOIN");
    Cell s2 = new CString("HELLO");
    Cell s3 = new CString("THERE");
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CString("HELLOTHERE"));
    model.changeCell(new Coord(1, 1), "=(JOIN \"HELLO\" \"THERE\")");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaLessThanTrue() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("<");
    Cell s2 = new CNumber(10);
    Cell s3 = new CNumber(12);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CBoolean(true));
    model.changeCell(new Coord(1, 1), "=(< 10 12)");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaLessThanFalse() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("<");
    Cell s2 = new CNumber(12);
    Cell s3 = new CNumber(10);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CBoolean(false));
    model.changeCell(new Coord(1, 1), "=(< 12 10)");
    assertEquals(c, model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMakingFormulaCellReference() {
    model.changeCell(new Coord(1, 1), "10");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("A1");
    Cell s3 = new CNumber(12);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(120));
    model.changeCell(new Coord(2, 1), "=(PRODUCT A1 12)");
    assertEquals(c, model.getCellAt(new Coord(2, 1)));
  }

  @Test
  public void testMakingFormulaCellReferenceMoreAdvanced() {
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(1, 2), "10");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A2");
    Cell s6 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(null);
    l.add(s6);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(100));
    model.changeCell(new Coord(1, 3), "=(PRODUCT(SUM A1 A2)5)");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testBigWorksheetFormula() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s2 = new CString("A1:A100");
    l.add(s1);
    l.add(s2);
    for (int ii = 1; ii <= 100; ii ++) {
      model.changeCell(new Coord(1, ii), Integer.toString(ii));
    }
    l.add(null);
    Cell c = new CFormula(l, new CNumber(5050));
    model.changeCell(new Coord(2, 1), "=(SUM A1:A100)");
    assertEquals(c, model.getCellAt(new Coord(2, 1)));
  }

  @Test
  public void test10Squared() {
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CNumber(10);
    Cell s3 = new CNumber(10);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(100));
    model.changeCell(new Coord(2, 1), "=(PRODUCT 10 10)");
    assertEquals(c, model.getCellAt(new Coord(2, 1)));
  }

  @Test
  public void testMakingFormulaCellReferenceMoreAdvanced2() {
    model.changeCell(new Coord(27, 45), "10");
    model.changeCell(new Coord(28, 4), "10");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s3 = new CString("AA45");
    Cell s4 = new CString("AB4");
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(100));
    model.changeCell(new Coord(1, 3), "=(PRODUCT AA45 AB4)");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testChangeCell() {
    model.changeCell(new Coord(1, 1), "3");
    model.changeCell(new Coord(1, 1), "10");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s3 = new CString("A1");
    Cell s4 = new CNumber(10);
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(100));
    model.changeCell(new Coord(1, 3), "=(PRODUCT A1 10)");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testFormulaNoFunction() {
    model.changeCell(new Coord(1, 1), "3");
    List<Cell> l = new ArrayList<>();
    Cell s3 = new CString("A1");
    l.add(s3);
    Cell c = new CFormula(l, new CNumber(3));
    model.changeCell(new Coord(1, 3), "= A1");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testFormulaNoFunctionStringGood() {
    List<Cell> l = new ArrayList<>();
    Cell s3 = new CString("Hello");
    l.add(s3);
    Cell c = new CFormula(l, new CString("Hello"));
    model.changeCell(new Coord(1, 3), "= \"Hello\"");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testFormulaNoFunctionSymbolBad() {
    model.changeCell(new Coord(1, 3), "= Hello");
  }

  @Test
  public void testBigFormula() {
    model.changeCell(new Coord(1, 1), "5");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "5");
    model.changeCell(new Coord(1, 4), "5");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A2");
    Cell s8 = new CString("SUM");
    Cell s9 = new CString("A3");
    Cell s10 = new CString("A4");
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(null);
    l.add(s8);
    l.add(s9);
    l.add(s10);
    l.add(null);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(100));
    model.changeCell(new Coord(2, 3), "=(PRODUCT (SUM A1 A2) (SUM A3 A4))");
    assertEquals(c, model.getCellAt(new Coord(2, 3)));
  }

  @Test
  public void testBigFormula2() {
    model.changeCell(new Coord(1, 1), "5");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "5");
    model.changeCell(new Coord(1, 4), "5");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A2");
    Cell s12 = new CString("SUM");
    Cell s13 = new CNumber(0);
    Cell s14 = new CNumber(10);
    Cell s8 = new CString("SUM");
    Cell s9 = new CString("A3");
    Cell s10 = new CString("A4");
    Cell s16 = new CString("SUM");
    Cell s15 = new CNumber(5);
    Cell s17 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(s12);
    l.add(s13);
    l.add(s14);
    l.add(null);
    l.add(null);
    l.add(s8);
    l.add(s9);
    l.add(s10);
    l.add(null);
    l.add(s16);
    l.add(s15);
    l.add(s17);
    l.add(null);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(2000));
    model.changeCell(new Coord(2, 3), "=(PRODUCT (SUM A1 A2 (SUM 0 10)) (SUM A3 A4) (SUM 5 5))");
    assertEquals(c, model.getCellAt(new Coord(2, 3)));
  }

  @Test
  public void testBigFormula3() {
    model.changeCell(new Coord(1, 1), "5");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "5");
    model.changeCell(new Coord(1, 4), "5");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A2");
    Cell s12 = new CString("PRODUCT");
    Cell s13 = new CNumber(0);
    Cell s14 = new CNumber(10);
    Cell s8 = new CString("SUM");
    Cell s9 = new CString("A3");
    Cell s10 = new CString("A4");
    Cell s16 = new CString("PRODUCT");
    Cell s15 = new CNumber(5);
    Cell s17 = new CNumber(5);
    Cell s18 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(s12);
    l.add(s13);
    l.add(s14);
    l.add(null);
    l.add(null);
    l.add(s8);
    l.add(s9);
    l.add(s10);
    l.add(null);
    l.add(s16);
    l.add(s15);
    l.add(s17);
    l.add(null);
    l.add(s18);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(2500 * 5));
    model.changeCell(new Coord(2, 3),
            "=(PRODUCT (SUM A1 A2 (PRODUCT 0 10)) (SUM A3 A4) (PRODUCT 5 5) 5)");
    assertEquals(c, model.getCellAt(new Coord(2, 3)));
  }

  @Test
  public void testCycleDirect() {
    model.changeCell(new Coord(1, 1), "=(SUM A1 1)");
    ArrayList<Cell> cl = new ArrayList<>();
    cl.add(new CString("SUM"));
    cl.add(new CString("A1"));
    cl.add(new CNumber(1));
    cl.add(null);
    Cell c = new CString("#ERROR");
    assertEquals(new CFormula(cl, c), model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testCycleInDirect() {
    model.changeCell(new Coord(1, 1), "= A10");
    model.changeCell(new Coord(1, 10), "= A1");
    ArrayList<Cell> cl = new ArrayList<>();
    cl.add(new CString("A10"));
    Cell c = new CString("#ERROR");
    assertEquals(new CFormula(cl, c), model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testCycleInDirectHard() {
    model.changeCell(new Coord(1, 1), "= A10");
    model.changeCell(new Coord(1, 10), "= A11");
    model.changeCell(new Coord(1, 11), "= A1");
    ArrayList<Cell> cl = new ArrayList<>();
    cl.add(new CString("A10"));
    Cell c = new CString("#ERROR");
    assertEquals(new CFormula(cl, c), model.getCellAt(new Coord(1, 1)));
  }

  @Test
  public void testMultSameCellMultipleTimes() {
    model.changeCell(new Coord(1, 1), "3");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A1");
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(9));
    model.changeCell(new Coord(1, 3), "=(PRODUCT A1 A1)");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testMultArgumentsOfNotSameType() {
    model.changeCell(new Coord(1, 1), "3");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s3 = new CString("A1");
    Cell s4 = new CBoolean(false);
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(3));
    model.changeCell(new Coord(1, 3), "=(PRODUCT A1 false)");
    assertEquals(c, model.getCellAt(new Coord(1, 3)));
  }

  @Test
  public void testBigFormula4() {
    model.changeCell(new Coord(1, 1), "5");
    model.changeCell(new Coord(1, 2), "10");
    model.changeCell(new Coord(1, 3), "15");
    model.changeCell(new Coord(1, 4), "0");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("PRODUCT");
    Cell s2 = new CString("SUM");
    Cell s3 = new CString("A1");
    Cell s4 = new CString("A2");
    Cell s12 = new CString("PRODUCT");
    Cell s13 = new CNumber(5);
    Cell s14 = new CNumber(10);
    Cell s8 = new CString("<");
    Cell s9 = new CString("A3");
    Cell s10 = new CString("A4");
    Cell s16 = new CString("PRODUCT");
    Cell s15 = new CNumber(10);
    Cell s17 = new CNumber(5);
    Cell s18 = new CNumber(5);
    l.add(s1);
    l.add(s2);
    l.add(s3);
    l.add(s4);
    l.add(s12);
    l.add(s13);
    l.add(s14);
    l.add(null);
    l.add(null);
    l.add(s8);
    l.add(s9);
    l.add(s10);
    l.add(null);
    l.add(s16);
    l.add(s15);
    l.add(s17);
    l.add(null);
    l.add(s18);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(16250));
    model.changeCell(new Coord(2, 3),
            "=(PRODUCT (SUM A1 A2 (PRODUCT 5 10)) (< A3 A4) (PRODUCT 10 5) 5)");
    assertEquals(c, model.getCellAt(new Coord(2, 3)));
  }

  @Test
  public void testFormulas() {
    model.changeCell(new Coord(3, 3), "3");
    model.changeCell(new Coord(3, 4), "3");
    model.changeCell(new Coord(3, 5), "= (SUM C3 C4)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("C3");
    Cell s4 = new CString("C4");
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(6));
    assertEquals(c, model.getCellAt(new Coord(3, 5)));
  }

  @Test
  public void testFormulas2() {
    model.changeCell(new Coord(4, 1), "3");
    model.changeCell(new Coord(4, 2), "3");
    model.changeCell(new Coord(4, 3), "= (SUM D1 D2)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("D1");
    Cell s4 = new CString("D2");
    l.add(s1);
    l.add(s3);
    l.add(s4);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(6));
    assertEquals(c, model.getCellAt(new Coord(4, 3)));
  }

  // TESTS PERTAINING TO COLUMN REFERENCE (EXTRA CREDIT)

  @Test
  public void testColRefBasic() {
    model.changeCell(new Coord(1, 1), "3");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "10");
    model.changeCell(new Coord(2, 1), "= (SUM A:A)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("A:A");
    l.add(s1);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(18));
    assertEquals(c, model.getCellAt(new Coord(2, 1)));
  }

  @Test
  public void testColRefMultipleCols() {
    model.changeCell(new Coord(1, 1), "3");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "10");
    model.changeCell(new Coord(2, 1), "3");
    model.changeCell(new Coord(2, 2), "5");
    model.changeCell(new Coord(2, 3), "10");
    model.changeCell(new Coord(3, 1), "= (SUM A:B)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("A:B");
    l.add(s1);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CNumber(36));
    assertEquals(c, model.getCellAt(new Coord(3, 1)));
  }

  @Test
  public void testColRefErrorInCol() {
    model.changeCell(new Coord(1, 1), "3");
    model.changeCell(new Coord(1, 2), "= A2");
    model.changeCell(new Coord(1, 3), "10");
    model.changeCell(new Coord(2, 1), "= (SUM A:A)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("A:A");
    l.add(s1);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CString("#ERROR"));
    assertEquals(c, model.getCellAt(new Coord(2, 1)));
  }

  @Test
  public void testColRefErrorRefToSameCol() {
    model.changeCell(new Coord(1, 1), "3");
    model.changeCell(new Coord(1, 2), "5");
    model.changeCell(new Coord(1, 3), "10");
    model.changeCell(new Coord(1, 4), "= (SUM A:A)");
    List<Cell> l = new ArrayList<>();
    Cell s1 = new CString("SUM");
    Cell s3 = new CString("A:A");
    l.add(s1);
    l.add(s3);
    l.add(null);
    Cell c = new CFormula(l, new CString("#ERROR"));
    assertEquals(c, model.getCellAt(new Coord(1, 4)));
  }

}
