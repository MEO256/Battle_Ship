package cs3500.pa04;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AiControllerTest {

  @Test
  void testJoin() {
    AiController bot = new AiController();
    bot.join();
    Assertions.assertEquals(bot.name, "MEO256");
    Assertions.assertEquals(bot.gameType, "SINGLE");
  }

  @Test
  public void testRegister() {
    String input = "6 6 1 1 1 1 0 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    AiController player = new AiController();
    String[][] board = new String[5][5];
    player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    List<Coord> loc = new ArrayList<>(Arrays.asList(new Coord(0, 0),
        new Coord(1, 2)));
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    player.board = board;
    Assertions.assertEquals(player.reportDamage(loc).size(), 1);
  }

  @Test
  void testTakeShots() {
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
    List<Coord> loc = new ArrayList<>(Arrays.asList(new Coord(0, 0),
        new Coord(1, 2)));
    Assertions.assertEquals(player.takeShots().size(), 1);
    player.sameCoord = loc;
    Assertions.assertEquals(player.takeShots().size(), 1);
  }
}