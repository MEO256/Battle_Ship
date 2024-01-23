package cs3500.pa04;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ModelTest {

  @Test
  void updateBoard() {
    List<Coord> loc = new ArrayList<>(Arrays.asList(new Coord(0, 0),
        new Coord(1, 2)));
    String[][] board = new String[5][5];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    List<Coord> shipsLoc = new ArrayList<>(Arrays.asList(new Coord(0, 0)));
    Model.updateBoard(loc, board, shipsLoc);
    Assertions.assertEquals(board[0][0], "H");
    Assertions.assertEquals(board[1][2], "M");
  }

  @Test
  void registerReport() {
    List<Coord> loc = new ArrayList<>(Arrays.asList(new Coord(0, 0), new Coord(1, 2)));
    List<Ship> los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    String[][] board = new String[5][5];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    List<Coord> shipsLoc = new ArrayList<>(Arrays.asList(new Coord(0, 0)));
    Model.updateBoard(loc, board, shipsLoc);
    Assertions.assertEquals(Model.registerReport(loc, los).get(0).getCol(),
        new Coord(0, 0).getCol());
  }

  @Test
  void updateShips() {
    Model model = new Model();
    String[][] board = new String[5][5];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    List<Ship> los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    Assertions.assertEquals(Model.updateShips(los, board), 1);
    String[][] boardDead = new String[5][5];
    for (int i = 0; i < boardDead.length; i++) {
      for (int j = 0; j < boardDead[i].length; j++) {
        boardDead[i][j] = "0";
        boardDead[0][0] = "H";
      }
    }
    Assertions.assertEquals(Model.updateShips(los, boardDead), 1);
  }

  @Test
  void allTheCoordinates() {
    List<Ship> losVer = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    List<Ship> losHor = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    Assertions.assertEquals(Model.allTheCoordinates(losVer).size(), 3);
    Assertions.assertEquals(Model.allTheCoordinates(losHor).size(), 3);
  }

  @Test
  void testTakeShots() {
    String input = "7 10 1 1 0 1 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    Controller player = new Controller(new Scanner(inputStream));
    player.los = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    player.board = new String[5][5];
    for (int i = 0; i < player.board.length; i++) {
      for (int j = 0; j < player.board[i].length; j++) {
        player.board[i][j] = "0";
      }
    }
    Assertions.assertEquals(player.takeShots().get(0).getCol(), 1);
    Assertions.assertEquals(player.takeShots().get(0).getRow(), 0);
  }

  @Test
  void testShipsAppropr() {
    ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 0, 3));
    Assertions.assertEquals(Model.shipsAppropr(list, 0), false);
  }
}