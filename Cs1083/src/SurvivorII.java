import java.util.*;
import java.awt.*;

public class SurvivorII {
  public static final int PLAYER_SIZE = 10;
  public static final Scanner CONSOLE = new Scanner(System.in);
  public static void main(String[] args) {

    System.out.println("Enter the number of zombies, box size, move size, and sleep time: ");

    // User specified values:
    int numberZombies = CONSOLE.nextInt();
    if (numberZombies<10)
      numberZombies = 10;

    int boxSize = CONSOLE.nextInt();
    if (boxSize<0)
      boxSize = 760;

    int moveSize = CONSOLE.nextInt();
    if (moveSize<0)
      moveSize = 10;

    int sleepTime = CONSOLE.nextInt();
    if (sleepTime<0)
      sleepTime = 100;

    // Create DrawingPanel and draw a box in the panel:
    DrawingPanel panel = new DrawingPanel(boxSize + 40, boxSize + 40);
    Graphics g = panel.getGraphics();
    g.fillRect(10, 10, 10, boxSize + 20);
    g.fillRect(10, 10, boxSize + 20, 10);
    g.fillRect(boxSize + 20, 10, 10, boxSize + 20);
    g.fillRect(10, boxSize + 20, boxSize + 20, 10);

    // Initialize positions of runner, chaser, and zombies.
    Point runner = new Point(boxSize/4, boxSize/2);
    Point chaser = new Point ((boxSize/4)*3, boxSize/2);

    Random random = new Random();
    Point[] zombies = new Point[numberZombies];
    boolean[] eliminated = new boolean[numberZombies];
    Arrays.fill(eliminated, Boolean.FALSE);

    for (int i = 0; i < numberZombies; i++) {
       zombies[i] = new Point(random.nextInt(boxSize) + 20, random.nextInt(boxSize) + 20);
    }

    // Display players using Color.GREEN and Color.RED  and Color.BLUE (or whatever colors you want).
    displayPlayers(panel, runner, chaser, zombies, eliminated, numberZombies);

    // Wait one second before start of game.
    panel.sleep(1000);

    // Game settings.
    char keyInput = ' ';
    keyInput = panel.getKeyChar();
    while (keyInput == 0) {
      keyInput = panel.getKeyChar();
    }

    boolean gameOver = false;     // use a boolean to indicate when the game is over
    boolean lost = false;         // use a boolean to indicate when the game is lost/won

    while (!gameOver) {

      // Erase players from the panel using Color.WHITE.
      erasePlayers(panel, runner, chaser, zombies, numberZombies);

      // Get input from user if any.
      char newKeyInput = panel.getKeyChar();
      if (newKeyInput == 'w' || newKeyInput == 'a' || newKeyInput == 's' || newKeyInput == 'd') {
        keyInput = newKeyInput;
      }

      // Move the players according to parameters.
      movePlayers (runner, chaser, zombies, eliminated, keyInput, boxSize, moveSize, numberZombies);

      // Display players using Color.GREEN and Color.RED (or whatever colors you want).
      displayPlayers(panel, runner, chaser, zombies, eliminated, numberZombies);

      if (collision (runner, chaser, zombies, panel, numberZombies, eliminated)) {
        System.out.println("YOU LOST!!!");
        lost = true;
        break;
      }
      // Wait sleepTime ms between moves.
      panel.sleep(sleepTime);

      int numberEliminated = 0;
      boolean eZombies = false;

      for(boolean eValue: eliminated){
        if(eValue) numberEliminated += 1;
        if(numberEliminated == eliminated.length) eZombies = true;
      }

      if (lost || eZombies)
        break;
    }

    if (! lost) {
      System.out.println("YOU WON!!!");
    }
  }

  // The displayPlayers method displays the runner and the chaser and the zombies on the panel.
  public static void displayPlayers(DrawingPanel panel, Point runner, Point chaser, Point[] zombies, boolean[] eliminated, int numberZombies) {
    Graphics g = panel.getGraphics();

    // Player: RUNNER
    g.setColor(Color.GREEN);
    g.fillRect((int)runner.getX() - PLAYER_SIZE / 2, (int)runner.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

    // Player: ZOMBIES
    g.setColor(Color.BLUE);
    for(int i = 0; i<numberZombies; i++) {
        Point tempZombie = zombies[i];
            if (!eliminated[i]) {
            g.fillRect(tempZombie.x - PLAYER_SIZE / 2, tempZombie.y - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
            }
    }

    // Player: CHASER
    g.setColor(Color.RED);
    g.fillRect((int)chaser.getX() - PLAYER_SIZE / 2, (int)chaser.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

  }

  // The erasePlayers method erases the runner and the chaser and the zombies on the panel.
  public static void erasePlayers(DrawingPanel panel, Point runner, Point chaser, Point[] zombies, int numberZombies) {

    Random random = new Random();
    Graphics g = panel.getGraphics();
    g.setColor(Color.WHITE);

    // Erase each zombie
    for(int i = 0; i < numberZombies; i++){
        g.fillRect( zombies[i].x - PLAYER_SIZE / 2, zombies[i].y - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
    }

    // Erase runner
    g.fillRect((int)runner.getX() - PLAYER_SIZE / 2, (int)runner.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

    // Erase chaser
    g.fillRect((int)chaser.getX() - PLAYER_SIZE / 2, (int)chaser.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

  }

  public static void movePlayers(Point runner, Point chaser, Point[] zombies, boolean[] eliminated,
                                char keyInput, int boxSize, int moveSize, int numberZombies) {

    processKeyInput(runner, keyInput, boxSize, moveSize);
    moveZombies(zombies, eliminated, boxSize, moveSize, numberZombies);
    moveChaser(runner, chaser, moveSize);

  }

  public static boolean legal(Point p, int dx, int dy, int boxSize) {
    return p.getX() + dx + PLAYER_SIZE / 2 < boxSize + 10
      && p.getX() + dx - PLAYER_SIZE / 2 > 20
      && p.getY() + dy + PLAYER_SIZE / 2 < boxSize + 10
      && p.getY() + dy - PLAYER_SIZE / 2 > 20;
  }

  public static double distance(double x1, double y1, double x2, double y2){

      double x    = x1 - x2;
      double y    = y1 - y2;
      double sqrX = Math.pow(x,2);
      double sqrY = Math.pow(y,2);

      return Math.sqrt(sqrX + sqrY);
  }

  public static void moveZombies(Point[] zombies, boolean[] eliminated, int boxSize, int moveSize, int numZombies) {
      int[] dxs = { 0, 0, moveSize, -moveSize };
      int[] dys = { moveSize, -moveSize, 0, 0 };
      Random random = new Random();

      for(int i = 0; i < numZombies; i++) {
          Point zombie = zombies[i];

          if (!eliminated[i]) {
              int di = random.nextInt(4);
              int dx = dxs[di];
              int dy = dys[di];
              if (legal(zombie, dx, dy, boxSize - 20)) {
                  zombie.translate(dx, dy);
              }
          }
      }
  }

  public static void moveChaser(Point runner, Point chaser, int moveSize) {

    double tempX = chaser.getX();
    double tempY = chaser.getY();
    double up, down, left, right;

    up    = distance(runner.getX(), runner.getY(), tempX, tempY-1);
    down  = distance(runner.getX(), runner.getY(), tempX, tempY+1);
    left  = distance(runner.getX(), runner.getY(), tempX-1, tempY);
    right = distance(runner.getX(), runner.getY(), tempX+1, tempY);

    if(up < down && up < left && up < right)
        chaser.translate(0,-(moveSize-1));

    else if(down < up && down < left && down < right)
        chaser.translate(0, (moveSize-1));

    else if(left < up && left < down && left < right)
        chaser.translate(-(moveSize-1), 0);

    else if(right < up && right < down && right < left)
        chaser.translate((moveSize-1), 0);
  }

  public static void processKeyInput(Point runner, char keyInput, int boxSize, int moveSize) {
    int[] dxs = { 0, 0, moveSize, -moveSize };
    int[] dys = { moveSize, -moveSize, 0, 0 };
    char[] keys = { 's', 'w', 'd', 'a' };

    for (int i = 0; i < dxs.length; i++) {
      int dx = dxs[i];
      int dy = dys[i];
      char key = keys[i];
      if (keyInput == key && legal(runner, dx, dy, boxSize)) {
        runner.translate(dx, dy);
      }
    }
  }

  public static boolean collision(Point runner, Point chaser, Point[] zombies, DrawingPanel panel, int numberZombies, boolean[] eliminated) {
    Graphics g = panel.getGraphics();

    if(Math.max( Math.abs(runner.getX() - chaser.getX()), Math.abs(runner.getY() - chaser.getY())) <= PLAYER_SIZE)
        return true;


    for(int i = 0; i<zombies.length; i++) {
        if (Math.max(Math.abs(runner.getX() - zombies[i].x), Math.abs(runner.getY() - zombies[i].y)) <= PLAYER_SIZE) {
            g.setColor(Color.WHITE);
            g.fillRect((int)zombies[i].getX() - PLAYER_SIZE / 2, (int)zombies[i].getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
            eliminated[i]= true;
        }
    }
    return false;
  }

  public static int getX(Point p) {
    return (int) p.getX();
  }

  public static int getY(Point p) {
    return (int) p.getY();
  }
}
