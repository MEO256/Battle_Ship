package cs3500.pa04;

import java.io.IOException;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {

  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   */
  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);

    ProxyController proxyController = new ProxyController(server, new AiController());

    proxyController.run();
  }

  /**
   * The main entrypoint into the code as the Client. Given a host and port as parameters, the
   * client is run. If there is an issue with the client or connecting,
   * an error message will be printed.
   *
   * @param args The expected parameters are the server's host and port
   */
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      View view = new View();

      view.start(0);
    } else if (args.length == 2) {
      try {
        runClient(args[0], Integer.parseInt(args[1]));
      } catch (Exception e) {
        throw new IOException();
      }
    } else {
      throw new IOException();
    }
  }
}