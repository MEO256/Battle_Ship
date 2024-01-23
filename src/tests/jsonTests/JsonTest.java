package cs3500.pa04.json;


import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa04.Coord;
import cs3500.pa04.GameResult;
import cs3500.pa04.Model;
import cs3500.pa04.Ship;
import cs3500.pa04.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonTest {

  @Test
  void testCoordJson() {
    CoordJson coordJson = new CoordJson(0, 0);
    Assertions.assertEquals(coordJson.col(), 0);
    Assertions.assertEquals(coordJson.row(), 0);
  }

  @Test
  void testCoordListJson() {
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    CoordListJson coordListJson = new CoordListJson(list);
    Assertions.assertEquals(coordListJson.los().size(), 1);
  }

  @Test
  void testSuccessJson() {
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    SuccessJson successJson = new SuccessJson(list);
    Assertions.assertEquals(successJson.los().size(), 1);
  }

  @Test
  void testReportDamage() {
    List<CoordJson> list = new ArrayList<>(Arrays.asList(new CoordJson(0, 0)));
    ReportDamageJson reportDamageJson = new ReportDamageJson(list);
    Assertions.assertEquals(reportDamageJson.coords().size(), 1);
  }

  @Test
  void testShipListJson() {
    CoordJson coordJson = new CoordJson(0, 0);
    ShipJson shipJson = new ShipJson(coordJson, 4, "VERTICAL");
    ShipListJson shipListJson = new ShipListJson(new ArrayList<>(Arrays.asList(shipJson)));
    Assertions.assertEquals(shipListJson.los().size(), 1);
  }

  @Test
  void testWinJson() {
    WinJson winJson = new WinJson(GameResult.WIN, "You won!");
    Assertions.assertEquals(winJson.reason(), "You won!");
    Assertions.assertEquals(winJson.win(), GameResult.WIN);
  }

  @Test
  void testShipJson() {
    CoordJson coordJson = new CoordJson(0, 0);
    ShipJson shipJson = new ShipJson(coordJson, 4, "VERTICAL");
    Assertions.assertEquals(shipJson.size(), 4);
    Assertions.assertEquals(shipJson.direction(), "VERTICAL");
    Assertions.assertEquals(shipJson.coord().row(), 0);
  }

  @Test
  void testSetupJson() {
    Map<ShipType, Integer> map = Model.shipsToName(new ArrayList<>(Arrays.asList(1, 1, 1, 1)));
    SetupJson setupJson = new SetupJson(4, 5, map);
    Assertions.assertEquals(setupJson.height(), 4);
    Assertions.assertEquals(setupJson.width(), 5);
    Assertions.assertEquals(setupJson.specifications().size(), 4);
  }

  @Test
  void testJsonUtils() {
    Map<ShipType, Integer> map = Model.shipsToName(new ArrayList<>(Arrays.asList(1, 1, 1, 1)));
    SetupJson setupJson = new SetupJson(4, 5, map);
    JsonNode jsonNode = JsonUtils.serializeRecord(setupJson);
    Assertions.assertTrue(jsonNode.toString().contains("{\"height\":4,\"width\":5"));
    List<Ship> ships = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        4, "VERTICAL")));
  }
}