package cs3500.pa04;

import java.util.List;
import java.util.Map;

/**
 * Represents the abstract class for the Player
 */
public abstract class AbstractPlayer implements Player {


  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return null;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public abstract List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications);

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  public String[][] boardFiller(String[][] board, List<Ship> loShips) {
    return Model.boardFiller(loShips, board);
  }

  public abstract List<Coord> reportDamage(List<Coord> opponentShotsOnBoard);

  public abstract void successfulHits(List<Coord> shotsThatHitOpponentShips);

  @Override
  public void endGame(GameResult result, String reason) {
    System.out.println(reason);
  }
}
