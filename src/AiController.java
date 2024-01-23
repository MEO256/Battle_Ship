package cs3500.pa04;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an AI
 */
public class AiController extends AbstractPlayer {

  String name;

  String gameType;
  Integer height;
  Integer width;

  String[][] board;

  List<Coord> sameCoord;

  List<Ship> los;

  /**
   * Constructs an AiController
   */
  public AiController(Integer height, Integer width) {
    this.name = name();
    this.height = height;
    this.width = width;
    this.board = boardMaker(height, width);
    this.sameCoord = new ArrayList<>();
  }

  public AiController() {

  }

  private String[][] boardMaker(int height, int width) {
    String[][] board = new String[height][width];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = "0";
      }
    }
    return board;
  }


  /**
   * Given the list with number of ships, return a list of ships with their locations
   * on the board.
   */
  public List<Ship> giveShips(ArrayList<Integer> ships) {
    this.los = setup(this.height, this.width, Model.shipsToName(ships));
    return this.los;
  }

  @Override
  public String name() {
    return "MEO256";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height the height of the board, range: [6, 15] inclusive
   * @param width the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   *
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.los = Model.putOnBoard(height, width, specifications);
    this.board = boardMaker(height, width);
    this.height = height;
    this.width = width;
    this.board = boardFiller(this.board, this.los);
    this.sameCoord = new ArrayList<>();
    return this.los;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> loc = new ArrayList<>();
    List<Coord> newCoords = new ArrayList<>();
    int count = Model.updateShips(this.los, this.board);
    boolean state = true;
    while (state) {
      loc.clear();
      newCoords.clear();
      for (int i = 0; i < count; i++) {
        int row = new Random().nextInt(height);
        int col = new Random().nextInt(width);
        boolean found = false;
        for (Coord coord : sameCoord) {
          if (coord.getRow() == row && coord.getCol() == col) {
            found = true;
            break;
          }
        }
        if (!found) {
          loc.add(new Coord(col, row));
          newCoords.add(new Coord(col, row));
        } else {
          i--;
        }
      }
      state = Model.similarCoord(loc);
    }
    sameCoord.addAll(newCoords);
    return loc;
  }

  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> loc = Model.allTheCoordinates(this.los);
    Model.updateBoard(opponentShotsOnBoard, this.board, loc);
    return Model.registerReport(opponentShotsOnBoard, this.los);
  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    Model.allTheCoordinates(this.los);
  }

  public void join() {
    this.name = "MEO256";
    this.gameType = "SINGLE";
  }
}
