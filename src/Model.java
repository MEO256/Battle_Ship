package cs3500.pa04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Edits the WorldState
 */
public class Model {

  /**
   * Writes the user's height and width in the ArrayList
   */
  public static ArrayList<Integer> boardSize(Scanner input) {
    ArrayList<Integer> array = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      array.add(input.nextInt());
    }
    return array;
  }

  /**
   * Checks if the given width and height are appropriate
   */
  public static boolean boardAppropr(ArrayList<Integer> input) {
    for (int i = 0; i < input.size(); i++) {
      if (!(input.get(i) >= 6 && input.get(i) <= 15)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Adds the number of appearance of a certain ship type
   */
  public static ArrayList<Integer> shipSize(Scanner input) {
    ArrayList<Integer> array = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      array.add(input.nextInt());
    }
    return array;
  }

  /**
   * Checks if the given amount of ships is less or equal to a smaller dimension of our board
   * and that the player has every ship type in game
   */
  public static boolean shipsAppropr(ArrayList<Integer> array, int min) {
    int sum = 0;
    for (Integer ints : array) {
      if (ints == 0) {
        return false;
      }
      sum += ints;
    }
    return sum <= min;
  }

  /**
   * Creates a map with our ship types
   */
  public static Map shipsToName(ArrayList<Integer> ships) {
    Map<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, ships.get(0));
    specifications.put(ShipType.BATTLESHIP, ships.get(1));
    specifications.put(ShipType.DESTROYER, ships.get(2));
    specifications.put(ShipType.SUBMARINE, ships.get(3));
    return specifications;
  }

  /**
   * Constructs a list of ships with their location, specification and a number of appearance
   */
  public static List<Ship> putOnBoard(int height, int width, Map<ShipType, Integer> spec) {
    String[][] shipsOnBoard = new String[height][width];
    for (int i = 0; i < shipsOnBoard.length; i++) {
      for (int j = 0; j < shipsOnBoard[0].length; j++) {
        shipsOnBoard[i][j] = "0";
      }
    }
    List<Ship> ships = new ArrayList<>();
    int numberOfShips = spec.get(ShipType.CARRIER);
    while (numberOfShips > 0) {
      ships.add(Model.generateShipPlacements(shipsOnBoard, ShipType.CARRIER.size,
          "C", ShipType.CARRIER));
      numberOfShips--;
    }
    numberOfShips = spec.get(ShipType.BATTLESHIP);
    while (numberOfShips > 0) {
      ships.add(Model.generateShipPlacements(shipsOnBoard, ShipType.BATTLESHIP.size,
          "B", ShipType.BATTLESHIP));
      numberOfShips--;
    }
    numberOfShips = spec.get(ShipType.DESTROYER);
    while (numberOfShips > 0) {
      ships.add(Model.generateShipPlacements(shipsOnBoard, ShipType.DESTROYER.size,
          "D", ShipType.DESTROYER));
      numberOfShips--;
    }
    numberOfShips = spec.get(ShipType.SUBMARINE);
    while (numberOfShips > 0) {
      ships.add(Model.generateShipPlacements(shipsOnBoard, ShipType.SUBMARINE.size,
          "S", ShipType.SUBMARINE));
      numberOfShips--;
    }
    return ships;
  }

  /**
   * Creates a board
   */
  public static String[][] boardFiller(List<Ship> loShips,
                                       String[][] board) {
    for (Ship ship : loShips) {
      if (ship.direction.equals("VERTICAL")) {
        for (int j = 0; j < ship.size; j++) {
          board[ship.location.getCol() + j][ship.location.getRow()] =
              getName(ship).toString().substring(0, 1);
        }
      } else {
        for (int j = 0; j < ship.size; j++) {
          board[ship.location.getCol()][ship.location.getRow() + j] =
              getName(ship).toString().substring(0, 1);
        }
      }
    }
    return board;
  }

  private static ShipType getName(Ship ship) {
    if (ship.size == 3) {
      return ShipType.SUBMARINE;
    } else if (ship.size == 4) {
      return ShipType.DESTROYER;
    } else if (ship.size == 5) {
      return ShipType.BATTLESHIP;
    } else {
      return ShipType.CARRIER;
    }
  }

  /**
   * Checks if our ship overlaps on other ships on the board
   */
  private static boolean isValidPlacement(String[][] board, int row, int col, int size,
                                          boolean isVertical) {
    if (isVertical && row + size > board.length) {
      return false;
    } else if (!isVertical && col + size > board[0].length) {
      return false;
    }

    for (int i = 0; i < size; i++) {
      if (isVertical && board[row + i][col] != "0") {
        return false;
      } else if (!isVertical && board[row][col + i] != "0") {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the coordinates of our ship location
   */
  private static Coord placeShip(String[][] board, int x, int y, int size,
                                 boolean isVertical,
                                 String name) {
    for (int i = 0; i < size; i++) {
      if (isVertical) {
        board[x + i][y] = name;
      } else {
        board[x][y + i] = name;
      }
    }
    return new Coord(x, y);
  }

  /**
   * Generates a ship randomly on the board
   */
  public static Ship generateShipPlacements(String[][] board, Integer spec,
                                            String name, ShipType ship) {
    Random random = new Random();

    boolean isVertical = random.nextBoolean();

    while (true) {
      int x = random.nextInt(board.length);
      int y = random.nextInt(board[0].length);


      if (isValidPlacement(board, x, y, spec, isVertical)) {
        if (isVertical) {
          return new Ship(placeShip(board, x, y, spec, isVertical, name), ship.size,
              "VERTICAL");
        } else {
          return new Ship(placeShip(board, x, y, spec, isVertical, name), ship.size,
              "HORIZONTAL");
        }
      }
    }
  }


  /**
   * Updates the gaming board by checking the location of the ships
   * and the coordinates of the shots given by the user or created randomly by AI
   */
  public static void updateBoard(List<Coord> loc, String[][] board, List<Coord> locOfShips) {
    for (Coord coord : loc) {
      boolean hit = false;

      for (Coord shipCoord : locOfShips) {
        if (coord.getRow() == shipCoord.getCol() && coord.getCol() == shipCoord.getRow()) {
          hit = true;
          break;
        }
      }

      if (hit) {
        board[coord.getCol()][coord.getRow()] = "H";
      } else {
        board[coord.getCol()][coord.getRow()] = "M";
      }
    }
  }

  /**
   * This method checks if the given list of Coord hit the player's fleet
   * and returns the list of Coord with the Coord that hit the fleet
   */
  public static List<Coord> registerReport(List<Coord> loc, List<Ship> los) {
    List<Coord> locDamage = new ArrayList<>();
    List<Coord> shipsCoord = allTheCoordinates(los);
    for (Coord coord : loc) {
      for (Coord coord1 : shipsCoord) {
        if (coord.getCol() == coord1.getRow()
            && coord.getRow() == coord1.getCol()) {
          Coord coord2 = new Coord(coord.getRow(), coord.getCol());
          locDamage.add(coord2);
        }
      }
    }
    return locDamage;
  }

  /**
   * Updates the number of shots depending on how much ships has left
   */
  public static int updateShips(List<Ship> los, String[][] board) {
    int val = los.size();
    int count = 0;
    for (Ship ship : los) {
      List<Ship> losShips = new ArrayList<>(Arrays.asList(ship));
      List<Coord> coordList = allTheCoordinates(losShips);
      for (Coord coord : coordList) {
        if (board[coord.getRow()][coord.getCol()].equals("H")) {
          count++;
        }
      }
      if (ship.size == count) {
        val--;
      }
      count = 0;
    }
    return val;
  }

  /**
   * Creates an empty board
   */
  public static String[][] emptyBoard(ArrayList<Integer> boardSize) {
    String[][] board = new String[boardSize.get(0)][boardSize.get(1)];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    return board;
  }

  /**
   * Creates a list of Coord with all the ship coordinates
   */
  public static List<Coord> allTheCoordinates(List<Ship> los) {
    List<Coord> loc = new ArrayList<>();
    for (Ship ship : los) {
      for (int i = 0; i < ship.size; i++) {
        if (ship.direction.equals("VERTICAL")) {
          loc.add(new Coord(ship.location.getRow(), ship.location.getCol() + i));
        } else {
          loc.add(new Coord(ship.location.getRow() + i, ship.location.getCol()));

        }
      }
    }
    return loc;
  }

  /**
   * Checks if the given list of Coord has similar coordinates in it
   */
  public static boolean similarCoord(List<Coord> loc) {
    List<Coord> locCopy = loc;
    int count = 0;
    for (Coord coord : loc) {
      for (Coord coordCopy : locCopy) {
        if (coord.getRow() == coordCopy.getRow() && coord.getCol() == coordCopy.getCol()) {
          count++;
        }
      }
      if (count != 1) {
        return true;
      }
      count = 0;
    }
    return false;
  }

  /**
   * Rotates the coordinates
   */
  public static List<Coord> rotateCoords(List<Coord> opponentShotsOnBoard) {
    List<Coord> coordList = new ArrayList<>();
    for (Coord coord : opponentShotsOnBoard) {
      coordList.add(new Coord(coord.getRow(), coord.getCol()));
    }
    return coordList;
  }
}
