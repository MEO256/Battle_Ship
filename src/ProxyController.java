package cs3500.pa04;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import cs3500.pa04.json.CoordJson;
import cs3500.pa04.json.CoordListJson;
import cs3500.pa04.json.JoinJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ReportDamageJson;
import cs3500.pa04.json.SetupJson;
import cs3500.pa04.json.ShipJson;
import cs3500.pa04.json.ShipListJson;
import cs3500.pa04.json.SuccessJson;
import cs3500.pa04.json.WinJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents the proxy to interact with the server
 */
public class ProxyController {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private AiController player;
  private final ObjectMapper mapper = new ObjectMapper();


  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Construct an instance of a ProxyPlayer.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   * @throws IOException if
   */
  public ProxyController(Socket server, AiController player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }


  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  /**
   * Get the messageJson and checks which method we should run
   */
  private void delegateMessage(MessageJson message) throws IOException {
    String name = message.messageName();
    JsonNode arguments = message.arguments();


    if ("join".equals(name)) {
      join(message);
    } else if ("take-shots".equals(name)) {
      getTakeShots(message);
    } else if ("setup".equals(name)) {
      getSetup(message);
    } else if ("end-game".equals(name)) {
      handleWin(arguments);
    } else if ("report-damage".equals(name)) {
      getDamage(message);
    } else if ("successful-hits".equals(name)) {
      getHits(message);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * Gets a messageJson from the server and starts the successfulHits method
   * with the given arguments
   */
  private void getHits(MessageJson message) throws JsonProcessingException {
    SuccessJson setupJson = this.mapper.convertValue(message.arguments(),
        SuccessJson.class);
    List<Coord> coords = new ArrayList<>();
    for (CoordJson coordJson : setupJson.los()) {
      Coord coord = new Coord(coordJson.col(), coordJson.row());
      coords.add(coord);
    }
    this.player.successfulHits(coords);
    ArrayNode emptyArrayNode = this.mapper.createArrayNode();
    MessageJson messageJson = new MessageJson(message.messageName(), emptyArrayNode);
    String responseJson = this.mapper.writeValueAsString(messageJson);
    this.out.println(responseJson);
  }

  /**
   * Gets the messageJson from the server and starts the reportDamage
   * with the given arguments
   */
  private void getDamage(MessageJson message) {
    ReportDamageJson setupJson = this.mapper.convertValue(message.arguments(),
        ReportDamageJson.class);
    List<Coord> coords = new ArrayList<>();
    for (CoordJson coordJson : setupJson.coords()) {
      Coord coord = new Coord(coordJson.row(), coordJson.col());
      coords.add(coord);
    }
    List<Coord> coordList = this.player.reportDamage(coords);
    List<CoordJson> losJson = new ArrayList<>();
    for (Coord ship : coordList) {
      CoordJson coordJson = new CoordJson(ship.getCol(), ship.getRow());
      losJson.add(coordJson);
    }
    ReportDamageJson shipJson = new ReportDamageJson(losJson);
    JsonNode response = JsonUtils.serializeRecord(shipJson);
    MessageJson messageJson = new MessageJson(message.messageName(), response);
    JsonNode response2 = JsonUtils.serializeRecord(messageJson);
    this.out.println(response2);
  }

  /**
   * Gets the messageJson from the server and starts the join method
   */
  void join(MessageJson message) {
    this.player.join();
    JoinJson join = new JoinJson(this.player.name, this.player.gameType);
    JsonNode response = JsonUtils.serializeRecord(join);
    MessageJson messageJson = new MessageJson(message.messageName(), response);
    JsonNode response2 = JsonUtils.serializeRecord(messageJson);
    this.out.println(response2);
  }

  /**
   * Gets the messageJson from the server and starts the takeShots method
   */
  private void getTakeShots(MessageJson message) {
    List<Coord> loc = this.player.takeShots();
    List<CoordJson> locJson = new ArrayList<>();
    for (Coord coord : loc) {
      CoordJson coordJson = new CoordJson(coord.getCol(), coord.getRow());
      locJson.add(coordJson);
    }
    CoordListJson coordListJson = new CoordListJson(locJson);
    JsonNode response = JsonUtils.serializeRecord(coordListJson);
    MessageJson messageJson = new MessageJson(message.messageName(), response);
    JsonNode response2 = JsonUtils.serializeRecord(messageJson);
    this.out.println(response2);
  }

  /**
   * Gets the meassageJson from the server and starts the setup method with given arguments
   */
  private void getSetup(MessageJson message) {
    SetupJson setupJson = this.mapper.convertValue(message.arguments(), SetupJson.class);


    List<Ship> los = this.player.setup(setupJson.height(),
        setupJson.width(), setupJson.specifications());
    List<ShipJson> losJson = new ArrayList<>();
    for (Ship ship : los) {
      CoordJson coordJson = new CoordJson(ship.location.getRow(), ship.location.getCol());
      ShipJson shipJson = new ShipJson(coordJson, ship.size, ship.direction);
      losJson.add(shipJson);
    }
    ShipListJson shipJson = new ShipListJson(losJson);
    JsonNode response = JsonUtils.serializeRecord(shipJson);
    MessageJson messageJson = new MessageJson(message.messageName(), response);
    JsonNode response2 = JsonUtils.serializeRecord(messageJson);
    this.player.los = los;
    this.out.println(response2);
  }


  /**
   * Parses the given arguments as a WinJSON, notifies the player whether they won, and provides a
   * void response to the server.
   *
   * @param arguments the Json representation of a WinJSON
   */
  private void handleWin(JsonNode arguments) {
    WinJson winJson = this.mapper.convertValue(arguments, WinJson.class);

    this.player.endGame(winJson.win(), winJson.reason());
    this.out.println(VOID_RESPONSE);
  }

}
