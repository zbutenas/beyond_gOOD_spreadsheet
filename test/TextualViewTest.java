import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.Builder;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.IView;
import edu.cs3500.spreadsheets.view.TextualView;

/**
 * Class represents the tests for the textual views.
 */
public class TextualViewTest {

  @Test
  public void testTextualSimple() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), "10");
    model.changeCell(new Coord(1000, 1), "100");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1 10.0\nALL1 100.0\n", v.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testTextualSimpleError() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(0, 1), "10");
    model.changeCell(new Coord(2, 1), "10");
    model.changeCell(new Coord(1000, 1), "100");
    IView v = new TextualView(model);
    v.render();
  }

  @Test
  public void testTextualEmpty() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), " ");
    model.changeCell(new Coord(1000, 1), "100");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1  \nALL1 100.0\n", v.toString());
  }

  @Test
  public void testTextualBoolean() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1),  "false");
    model.changeCell(new Coord(3, 1), "true");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1 false\nC1 true\n", v.toString());
  }

  @Test
  public void testTextualSum() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), "=(SUM 10 12)");
    model.changeCell(new Coord(3, 1), "=(SUM B1 12)");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1 22.0\nC1 34.0\n", v.toString());
  }

  @Test
  public void testTextualSum0() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "0");
    model.changeCell(new Coord(2, 1), "=(SUM 0 0)");
    model.changeCell(new Coord(3, 1), "=(SUM A1 B1)");
    IView v = new TextualView(model);
    assertEquals("A1 0.0\nB1 0.0\nC1 0.0\n", v.toString());
  }

  @Test
  public void testTextualProduct() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), "=(PRODUCT 1 12)");
    model.changeCell(new Coord(4, 1), "=(PRODUCT B1 3)");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1 12.0\nD1 36.0\n", v.toString());
  }

  @Test
  public void testTextualProduct0() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), "=(PRODUCT 0 12)");
    model.changeCell(new Coord(5, 1), "=(PRODUCT B1 3)");
    IView v = new TextualView(model);
    assertEquals("A1 10.0\nB1 0.0\nE1 0.0\n", v.toString());
  }

  @Test
  public void testTextualLessThanTypeMismatch() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "10");
    model.changeCell(new Coord(2, 1), "=(< 12 10)");
    model.changeCell(new Coord(4, 3), "=(< 10 B1)");
    IView v = new TextualView(model);
  }

  @Test
  public void testTextualLessThanTooManyArgs() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 4), "9");
    model.changeCell(new Coord(2, 1), "=(< 12 23 3 4)");
    IView v = new TextualView(model);
  }

  @Test
  public void testTextualLessThan() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "9");
    model.changeCell(new Coord(2, 1), "=(< 12 23)");
    IView v = new TextualView(model);
    assertEquals("A1 9.0\nB1 true\n", v.toString());
  }

  @Test
  public void testTextualJoin() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "9");
    model.changeCell(new Coord(2, 1), "=(JOIN \"cs\" \"3500\")");
    IView v = new TextualView(model);
    assertEquals("A1 9.0\nB1 cs3500\n", v.toString());
  }

  @Test
  public void testTextualJoinTooManyArgs() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "9");
    model.changeCell(new Coord(2, 1), "=(JOIN \"cs\" \"3500\" \"yo\")");
    IView v = new TextualView(model);
  }

  @Test
  public void testTextualFormulaComplicated() {
    Builder b = new Builder();
    WorksheetModel model = b.createWorksheet();
    model.changeCell(new Coord(1, 1), "9");
    model.changeCell(new Coord(2, 1), "=(PRODUCT A1 3 (SUM 3 2 1))");
    IView v = new TextualView(model);
    assertEquals("A1 9.0\nB1 162.0\n", v.toString());
  }

  @Test
  public void testTextualModelFile() throws FileNotFoundException {
    Builder b = new Builder();
    String filename = "Basic1.txt";
    String filename2 = "BasicRender.txt";
    Readable r = new FileReader(filename);
    PrintWriter pw = new PrintWriter(filename2);
    WorksheetModel model = WorksheetReader.read(b, r);
    IView v = new TextualView(model, pw);
    v.render();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testTextualPrintWriter() {
    Builder b = new Builder();
    String filename = "outputTest.txt";
    try {
      PrintWriter pw = new PrintWriter(filename);
      WorksheetModel model = b.createWorksheet();
      model.changeCell(new Coord(1, 1), "10");
      model.changeCell(new Coord(2, 1), "=e");
      model.changeCell(new Coord(1000, 1), "100");
      IView v = new TextualView(model, pw);
      v.render();
      pw.close();
    }
    catch (FileNotFoundException ignored) {
    }
  }

  @Test
  public void testRoundTrip() throws FileNotFoundException {
    Builder b = new Builder();
    String filename = "ScrollingFile.txt";
    String filenameNew = "ScrollingFileOutput.txt";

    // Creates the model from a file
    Readable r = new FileReader(filename);
    WorksheetModel model = WorksheetReader.read(b, r);

    // Save that model to an output file
    PrintWriter pw = new PrintWriter(filenameNew);
    IView v = new TextualView(model, pw);
    v.render();
    pw.close();

    // Create a model from the output file
    Readable rNew = new FileReader(filenameNew);
    WorksheetModel modelNew = WorksheetReader.read(b, rNew);

    assertEquals(model.getCellAt(new Coord( 1, 1)),
            modelNew.getCellAt(new Coord(1, 1)));
    assertEquals(model.getCellAt(new Coord( 2, 1)),
            modelNew.getCellAt(new Coord(2, 1)));
    assertEquals(model.getCellAt(new Coord( 3, 1)),
            modelNew.getCellAt(new Coord(3, 1)));
  }
}
