package cs3500.pa04;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents the user input
 */
public class Controller extends AbstractPlayer {

  ArrayList<Integer> boardSize;

  Scanner input;

  List<Ship> los;

  String[][] board;


  /**
   * Constructs a Controller
   */
  Controller(Scanner input) {
    this.input = input;
    this.boardSize = Model.boardSize(input);
    this.board = Model.emptyBoard(this.boardSize);
  }

  /**
   * Checks of the size of the Board is appropriate
   */
  public boolean getBoardSize() {
    return Model.boardAppropr(this.boardSize);
  }

  /**
   * Return a list of ship appearance
   */
  public ArrayList<Integer> getShipsNumber() {
    ArrayList<Integer> array = Model.shipSize(this.input);
    int min = Math.min(this.boardSize.get(0), this.boardSize.get(1));
    if (Model.shipsAppropr(array, min)) {
      return array;
    } else {
      return new ArrayList<>();
    }
  }

  /**
   * Given the list with number of ships, return a list of ships with their locations
   * on the board.
   */
  public List<Ship> giveShips(ArrayList<Integer> ships) {
    this.los = setup(this.boardSize.get(0), this.boardSize.get(1), Model.shipsToName(ships));
    this.board = boardFiller(this.board, this.los);
    return this.los;
  }

  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    return Model.putOnBoard(height, width, specifications);
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  public List<Coord> takeShots() {
    List<Coord> loc = new ArrayList<>();
    int count = Model.updateShips(this.los, this.board);
    for (int i = 0; i < count; i++) {
      Coord coord = new Coord(input.nextInt(), input.nextInt());
      loc.add(new Coord(coord.getRow(), coord.getCol()));
    }
    return loc;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> loc = Model.allTheCoordinates(this.los);
    List<Coord> rotatedCoords = Model.rotateCoords(opponentShotsOnBoard);
    Model.updateBoard(rotatedCoords, this.board, loc);
    return Model.registerReport(rotatedCoords, this.los);
  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    Model.allTheCoordinates(this.los);
  }
}
