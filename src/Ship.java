package cs3500.pa04;

/**
 * Represents the Ship for the BattleSalvo
 */
public class Ship {

  String direction;

  int size;

  Coord location;


  /**
   * Constructs a ship
   */
  public Ship(Coord location, int size, String direction) {
    this.size = size;
    this.location = location;
    this.direction = direction;
  }
}
