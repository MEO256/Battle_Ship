package cs3500.pa04;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ControllerTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintStream originalOutput = System.out;
  private final InputStream originalInput = System.in;

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void testGetShipsNumber() {
    String input = "8 10 1 1 1 1 ";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    Controller player = new Controller(new Scanner(inputStream));
    Assertions.assertEquals(new ArrayList<>(Arrays.asList(1, 1, 1, 1)), player.getShipsNumber());
  }

  @Test
  public void testGetShipsNumberInvalid() {
    String input = "8 10 1 4 4 4";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    Controller player = new Controller(new Scanner(inputStream));
    Assertions.assertEquals(new ArrayList<>(), player.getShipsNumber());
  }

  @Test
  public void testSuccess() {
    String input = "6 6 1 1 1 1 0 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    List<Coord> loc = new ArrayList<>(Arrays.asList(new Coord(0, 0),
        new Coord(1, 2)));
    Controller player = new Controller(new Scanner(inputStream));
    player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    player.successfulHits(loc);
    Assertions.assertEquals(player.board[0][0], "0");
  }

  @Test
  public void testRegister() {
    String input = "6 6 1 1 1 1 0 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    Controller player = new Controller(new Scanner(inputStream));
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
  void testSetup() {
    String input = "6 6 1 1 1 1 0 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    Controller player = new Controller(new Scanner(inputStream));
    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 1, 2));
    Map<ShipType, Integer> map = Model.shipsToName(list);
    Assertions.assertEquals(player.setup(8, 8, map).get(0).size, 6);
    Assertions.assertEquals(player.setup(8, 8, map).get(1).size, 5);
  }

  @Test
  void testGiveShips() {
    String input = "8 8";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    Controller player = new Controller(new Scanner(inputStream));
    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 1, 2));
    Assertions.assertEquals(player.giveShips(list).get(0).size, 6);
    Assertions.assertEquals(player.giveShips(list).get(1).size, 5);
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOutput);
    System.setIn(originalInput);
  }

}