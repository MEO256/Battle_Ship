package cs3500.pa04;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the display for the game
 */
public class View {


  /**
   * Starts the game by welcoming a user and gives a user opportunity to write the height and
   * width of the board
   */
  public void start(int i) {
    if (i == 0) {
      System.out.println("Hello! Welcome to the OOD BattleSalvo Game! \n"
          + "Please enter a valid height and width below:\n"
          + "------------------------------------------------------");
    }
    Scanner input = new Scanner(System.in);
    Controller player = new Controller(input);
    if (player.getBoardSize()) {
      System.out.println("Please enter your fleet in the order [Carrier, "
          + "Battleship, Destroyer, Submarine].\n"
          + "Remember, your fleet may not exceed size "
          + Math.min(player.boardSize.get(0), player.boardSize.get(1))
          + "\n--------------------------------------------------------------------------------\n");
      ships(player);
    } else {
      System.out.println("------------------------------------------------------\n"
          + "Uh Oh! You've entered invalid dimensions. Please remember that the height and width\n"
          + "of the game must be in the range (6, 15), inclusive. Try again!\n"
          + "------------------------------------------------------");
      start(1);
    }
  }

  /**
   * Shows User's board with all the ships on the board
   */
  public void ships(Controller player) {
    ArrayList<Integer> ships = player.getShipsNumber();
    System.out.println(ships);
    int height = player.boardSize.get(0);
    int width = player.boardSize.get(1);
    if (!ships.isEmpty()) {
      List<Ship> loShips = player.giveShips(ships);
      System.out.println(loShips);
      String[][] board = player.boardFiller(player.board, loShips);
      System.out.println("Your Board:");
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
          System.out.print(board[i][j] + " ");
        }
        System.out.println();
      }
      AiController bot = new AiController(height, width);
      shipsAi(bot, ships, player, board, loShips);
    } else {
      System.out.println("Uh Oh! You've entered invalid fleet sizes.\n"
          + "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].\n"
          + "Remember, your fleet may not exceed size "
          + Math.min(player.boardSize.get(0), player.boardSize.get(1))
          + "\n--------------------------------------------------------------------------------");
      ships(player);
    }
  }

  /**
   * Starts the attack phase where a user can see which shots he/she missed or hit
   * and also can see which cells AI shot.
   */
  void attack(String[][] botBoard, List<Ship> loShips, Controller player,
              AiController bot, String[][] playerBoard, List<Ship> loShipsPlayer) {
    int numBot = loShips.size();
    int numPlayer = loShipsPlayer.size();
    while (numBot > 0 && numPlayer > 0) {
      System.out.println("Please Enter " + numPlayer + " Shots:\n"
          + "------------------------------------------------------------------");
      List<Coord> locBot = bot.takeShots();
      player.successfulHits(locBot);
      player.reportDamage(locBot);
      List<Coord> loc = player.takeShots();
      bot.successfulHits(loc);
      bot.reportDamage(loc);
      System.out.println("Opponent Board Data:");
      for (int i = 0; i < botBoard.length; i++) {
        for (int j = 0; j < botBoard[0].length; j++) {
          System.out.print(botBoard[i][j] + " ");
        }
        System.out.println();
      }
      System.out.println("Your Board:");
      for (int i = 0; i < playerBoard.length; i++) {
        for (int j = 0; j < playerBoard[0].length; j++) {
          System.out.print(playerBoard[i][j] + " ");
        }
        System.out.println();
      }
      numBot = loc.size();
      numPlayer = locBot.size();
    }
    endGame(numPlayer, numBot, player);
  }

  /**
   * Player wins if the AI ships are all sunk and loses when Player's ships are all sunk
   * and there can be a draw if the AI and player's ships are all sunk
   */
  private void endGame(int numBot, int numPlayer, Controller player) {
    if (numPlayer > 0 && numBot == 0) {
      player.endGame(GameResult.WIN, "You win!!!");
    } else if (numPlayer == 0 && numBot > 0) {
      player.endGame(GameResult.LOSE, "You lose :(");
    } else {
      player.endGame(GameResult.DRAW, "Draw");
    }
  }


  /**
   * Shows AI's board with hidden ships
   */
  public void shipsAi(AiController bot, ArrayList<Integer> ships, Controller player,
                      String[][] playerBoard, List<Ship> loShipsPlayer) {
    List<Ship> loShips = bot.giveShips(ships);
    String[][] botBoard = bot.boardFiller(bot.board, loShips);
    for (int i = 0; i < botBoard.length; i++) {
      for (int j = 0; j < botBoard[i].length; j++) {
        botBoard[i][j] = "0";
      }
    }
    System.out.println("Opponent Board Data:");
    for (int i = 0; i < botBoard.length; i++) {
      for (int j = 0; j < botBoard[0].length; j++) {
        System.out.print(botBoard[i][j] + " ");
      }
      System.out.println();
    }
    attack(botBoard, loShips, player, bot, playerBoard, loShipsPlayer);
  }
}
