package cs3500.pa04;

/**
 * Represents the size of the certain ship types
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  int size;

  ShipType(int i) {
    this.size = i;
  }
}
