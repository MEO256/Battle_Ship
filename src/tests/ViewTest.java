package cs3500.pa04;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test the View class
 */
public class ViewTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintStream originalOutput = System.out;
  private final InputStream originalInput = System.in;

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void testStartWithValidInput() {
    String input = "8 10";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    try {
      View view = new View();
      view.start(0);

    } catch (Exception e) {
      String expectedOutput = "Please enter your fleet in the order [Carrier, "
          + "Battleship, Destroyer, Submarine].\n"
          + "Remember, your fleet may not exceed size "
          + "8"
          + "\n--------------------------------------------------------------------------------";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @Test
  public void testStartWithInvalidOneInput() {
    String input = "20 20";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    try {
      View view = new View();
      view.start(1);

    } catch (Exception e) {
      String expectedOutput = "------------------------------------------------------\n"
          + "Uh Oh! You've entered invalid dimensions. Please remember that the height and width\n"
          + "of the game must be in the range (6, 15), inclusive. Try again!\n"
          + "------------------------------------------------------";
      Assertions.assertEquals(expectedOutput, outputStream.toString().trim());

    }
  }

  @Test
  public void testShipsWithValidInput() {
    class SubController extends Controller {

      public SubController(Scanner input, ArrayList<Integer> boardSize) {
        super(input);
        this.boardSize = boardSize;
      }

      @Override
      public ArrayList<Integer> getShipsNumber() {
        return new ArrayList<>(Arrays.asList(5, 4, 3, 2));
      }

      @Override
      public boolean getBoardSize() {
        return true;
      }

      @Override
      public List<Ship> giveShips(ArrayList<Integer> ships) {
        List<Ship> loShips = new ArrayList<>();
        Ship ship1 = new Ship(new Coord(0, 0),
            ShipType.SUBMARINE.size, "VERTICAL");
        loShips.add(ship1);
        return loShips;
      }
    }

    String input = "10 10";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    Scanner scanner = new Scanner(inputStream);
    ArrayList<Integer> boardSize = new ArrayList<>(Arrays.asList(10, 10));

    try {
      View view = new View();
      view.ships(new SubController(scanner, boardSize));
    } catch (Exception e) {
      String expectedOutput = "Your Board:";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @Test
  public void testShipsWithInvalidInput() {
    class SubController extends Controller {
      /**
       * Constructs a Controller
       */
      SubController(Scanner input) {
        super(input);
      }

      @Override
      public ArrayList<Integer> getShipsNumber() {
        return new ArrayList<>();
      }

      @Override
      public boolean getBoardSize() {
        return true;
      }
    }

    String input = "1 0 2 0";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    try {
      View view = new View();
      view.ships(new SubController(new Scanner(inputStream)));

    } catch (StackOverflowError e) {
      Assertions.assertTrue(
          outputStream.toString().contains("Uh Oh! You've entered invalid fleet sizes.\n"
              + "Please enter your fleet in the order [Carrier, Battleship, "
              + "Destroyer, Submarine].\n"
              + "Remember, your fleet may not exceed size "
              + "0"
              + "\n----------------------------------------------------------------------------"
              + "----"));
    }
  }


  @Test
  public void testStartWithInvalidTwoInput() {
    String input = "4 4";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    try {
      View view = new View();
      view.start(1);

    } catch (Exception e) {
      String expectedOutput = "------------------------------------------------------\n"
          + "Uh Oh! You've entered invalid dimensions. Please remember that the height and width\n"
          + "of the game must be in the range (6, 15), inclusive. Try again!\n"
          + "------------------------------------------------------";
      Assertions.assertEquals(expectedOutput, outputStream.toString().trim());
    }
  }

  @Test
  public void testAttack() {

    String input = "4 4 1 1 0 0 1 2 2 1 1 3 3 1";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    // Create test data
    String[][] botBoard = new String[][] {
        {"B", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };
    String[][] playerBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };

    List<Ship> loShips = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL"), new Ship(new Coord(2, 2),
        ShipType.SUBMARINE.size, "VERTICAL")));
    List<Ship> loShipsPlayer = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    Controller player = new Controller(new Scanner(inputStream));
    AiController bot = new AiController(4, 4);

    try {
      View view = new View();
      view.attack(botBoard, loShips, player, bot, playerBoard, loShipsPlayer);

    } catch (Exception e) {

      String expectedOutput = "Please Enter 1 Shots:\n"
          + "------------------------------------------------------------------";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @Test
  public void testAttackDraw() {

    String input = "4 4 1 1 0 0 1 2 2 1 1 3 3 1";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    // Create test data
    String[][] botBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };
    String[][] playerBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };

    List<Ship> loShips = new ArrayList<>();
    List<Ship> loShipsPlayer = new ArrayList<>();
    Controller player = new Controller(new Scanner(inputStream));
    AiController bot = new AiController(4, 4);

    try {
      View view = new View();
      view.attack(botBoard, loShips, player, bot, playerBoard, loShipsPlayer);

    } catch (Exception e) {

      String expectedOutput = "Draw";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @Test
  public void testAttackWin() {

    String input = "4 4 1 1 0 0 1 2 2 1 1 3 3 1";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    // Create test data
    String[][] botBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };
    String[][] playerBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };

    List<Ship> loShips = new ArrayList<>();
    List<Ship> loShipsPlayer = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    Controller player = new Controller(new Scanner(inputStream));
    AiController bot = new AiController(4, 4);

    try {
      View view = new View();
      view.attack(botBoard, loShips, player, bot, playerBoard, loShipsPlayer);

    } catch (Exception e) {

      String expectedOutput = "You Won!!!";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @Test
  public void testAttackLose() {

    String input = "4 4 1 1 0 0 1 2 2 1 1 3 3 1";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);

    // Create test data
    String[][] botBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };
    String[][] playerBoard = new String[][] {
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"},
        {"0", "0", "0", "0"}
    };

    List<Ship> loShips = new ArrayList<>(Arrays.asList(new Ship(new Coord(0, 0),
        ShipType.SUBMARINE.size, "VERTICAL")));
    List<Ship> loShipsPlayer = new ArrayList<>();
    Controller player = new Controller(new Scanner(inputStream));
    AiController bot = new AiController(4, 4);

    try {
      View view = new View();
      view.attack(botBoard, loShips, player, bot, playerBoard, loShipsPlayer);

    } catch (Exception e) {

      String expectedOutput = "You lose ;(";
      Assertions.assertTrue(outputStream.toString().contains(expectedOutput));
    }
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOutput);
    System.setIn(originalInput);
  }
}