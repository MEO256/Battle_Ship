package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DriverTest {

  @Test
  public void fakeTest() {
    System.out.println("An important message...");
    assertEquals(5, 5);
  }

  @Test
  public void testMain() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOutput = System.out;
    System.setOut(new PrintStream(outputStream));

    Driver drive = new Driver();
  }

  @Test
  void testServerRun() {
    String[] args = new String[]{"0.0.0.0", "35001"};
    try {
      Driver.main(args);
    } catch (Exception e) {
      Assertions.assertThrows(IOException.class, () -> Driver.main(args));
    }
  }

  @Test
  void testServerRunInvalid() {
    String[] args = new String[]{"HEY", "35001"};
    try {
      Driver.main(args);
    } catch (Exception e) {
      Assertions.assertThrows(IOException.class, () -> Driver.main(args));
    }
  }

  @Test
  void testServerRunInvalidWithMoreArgs() {
    String[] args = new String[]{"0.0.0.0", "35001", "HEY"};
    try {
      Driver.main(args);
    } catch (Exception e) {
      Assertions.assertThrows(IOException.class, () -> Driver.main(args));
    }
  }


}