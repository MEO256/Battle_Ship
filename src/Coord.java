package cs3500.pa04;


/**
 * Represents the coordinates for the ship
 */
public class Coord {
  private int row;
  private int col;

  public Coord(int col, int row) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}