package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ReportDamageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.SuccessJson;
import cs3500.pa04.json.WinJson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Represents the tests for the ProxyController class
 */
public class ProxyControllerTest {

  private ByteArrayOutputStream testLog;
  private ProxyController dealer;


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Mock a Socket to simulate behaviors of ProxyControllers being connected to a server.
   */
  public class Mocket extends Socket {

    private final InputStream testInputs;
    private final ByteArrayOutputStream testLog;

    /**
     * @param testLog what the server has received from the client
     * @param toSend what the server will send to the client
     */
    public Mocket(ByteArrayOutputStream testLog, List<String> toSend) {
      this.testLog = testLog;

      // Set up the list of inputs as separate messages of JSON for the ProxyDealer to handle
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);
      for (String message : toSend) {
        printWriter.println(message);
      }
      this.testInputs = new ByteArrayInputStream(stringWriter.toString().getBytes());
    }

    @Override
    public InputStream getInputStream() {
      return this.testInputs;
    }

    @Override
    public OutputStream getOutputStream() {
      return this.testLog;
    }
  }

  @Test
  public void testVoidForJoin() {
    // Prepare sample message
    JoinJson joinJsonJson = new JoinJson("MEO256", "SINGLE");
    JsonNode sampleMessage = createSampleMessage("join", joinJsonJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      this.dealer = new ProxyController(socket, new AiController());
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"join\",\"arguments\":{\"name\":\"MEO256\","
        + "\"game-type\":\"SINGLE\"}}";
    assertEquals(expected.trim(), logToString().trim());
  }

  @Test
  public void testVoidForWin() {
    // Prepare sample message
    WinJson joinJsonJson = new WinJson(GameResult.WIN, "You won!");
    JsonNode sampleMessage = createSampleMessage("end-game", joinJsonJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      this.dealer = new ProxyController(socket, new AiController());
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "\"void\"";
    assertEquals(expected.trim(), logToString().trim());
  }

  @Test
  public void testVoidForSetup() {
    // Prepare sample message
    Map<ShipType, Integer> map = Model.shipsToName(new ArrayList<>(Arrays.asList(1, 1, 1, 1)));
    SetupJson joinJsonJson = new SetupJson(7, 7, map);
    JsonNode sampleMessage = createSampleMessage("setup", joinJsonJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      this.dealer = new ProxyController(socket, new AiController());
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":[{\"coord\":";
    assertTrue(logToString().contains(expected));
  }

  @Test
  public void testVoidForTakeShots() {
    // Prepare sample message
    JsonNode sampleMessage = createSampleMessage("take-shots", null);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      AiController player = new AiController();
      String[][] board = new String[5][5];
      player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
          ShipType.SUBMARINE.size, "VERTICAL")));
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          board[i][j] = "0";
        }
      }
      player.sameCoord = new ArrayList<>();
      player.board = board;
      player.height = 5;
      player.width = 5;
      this.dealer = new ProxyController(socket, player);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"take-shots\",\"arguments\"";
    assertTrue(logToString().contains(expected));
  }

  @Test
  public void testVoidForReportDamage() {
    // Prepare sample message
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    ReportDamageJson reportDamageJson = new ReportDamageJson(list);
    JsonNode sampleMessage = createSampleMessage("report-damage", reportDamageJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      AiController player = new AiController();
      String[][] board = new String[5][5];
      player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
          ShipType.SUBMARINE.size, "VERTICAL")));
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          board[i][j] = "0";
        }
      }
      player.sameCoord = new ArrayList<>();
      player.board = board;
      player.height = 5;
      player.width = 5;
      this.dealer = new ProxyController(socket, player);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"report-damage\",\"arguments\"";
    assertTrue(logToString().contains(expected));
  }

  @Test
  public void testVoidForSuccessfulHits() {
    // Prepare sample message
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    SuccessJson reportDamageJson = new SuccessJson(list);
    JsonNode sampleMessage = createSampleMessage("successful-hits", reportDamageJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      AiController player = new AiController();
      String[][] board = new String[5][5];
      player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
          ShipType.SUBMARINE.size, "VERTICAL")));
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          board[i][j] = "0";
        }
      }
      player.sameCoord = new ArrayList<>();
      player.board = board;
      player.height = 5;
      player.width = 5;
      this.dealer = new ProxyController(socket, player);
    } catch (IOException e) {
      throw new RuntimeException();
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"successful-hits\",\"arguments\"";
    assertTrue(logToString().contains(expected));
  }

  @Test
  public void testVoidForClosedServer() {
    // Prepare sample message
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    SuccessJson reportDamageJson = new SuccessJson(list);
    JsonNode sampleMessage = createSampleMessage("hey", reportDamageJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      AiController player = new AiController();
      this.dealer = new ProxyController(socket, player);
    } catch (IOException e) {
      throw new RuntimeException();
    }
    Assertions.assertThrows(IllegalStateException.class, () -> this.dealer.run());
  }

  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}