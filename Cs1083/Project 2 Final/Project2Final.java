import java.util.*;
import java.awt.*;


public class Project2Final{
  public static final int sleepTime = 100;

  public static void main(String[] args){

    System.out.println("Project 2 written by ERIC ESCALANTE");
    // Create DrawingPanel and draw a box in the panel.
    // The box is a square of this size.
    int boxSize = 760;
    DrawingPanel panel = new DrawingPanel(800, 800);
    Graphics g = panel.getGraphics();
    g.fillRect(10, 10, 10, 780);
    g.fillRect(10, 10, 780, 10);
    g.fillRect(780, 10, 10, 780);
    g.fillRect(10, 780, 780, 10);

    // Initialize positions of runner and chaser.
    Point runner = new Point(200, 400);
    Point chaser = new Point(600, 400);

    // Variable for input from user to move runner.
    char keyInput = ' ';
    // The runner should move moveSize (or zero) pixels each time step.
    // The chaser should move moveSize - 1 pixels each time step.
    int moveSize = 10;

    // Display players using Color.GREEN and Color.RED (or whatever colors you want)
    displayPlayers(panel, runner, chaser);

    // Wait one second before start of game.
    panel.sleep(1000);

    int i = 0;

  for(i = 0; i<=300; i++)
  {
      erasePlayers(panel, runner, chaser);
       // Get input from user if any.
       char newKeyInput = panel.getKeyChar();
       if (newKeyInput == 'w' || newKeyInput == 'a' || newKeyInput == 's' || newKeyInput == 'd') {
         keyInput = newKeyInput;
       }

       // Move the players according to parameters.
       movePlayers(runner, chaser, keyInput, boxSize, moveSize);

       // Display players using Color.GREEN and Color.RED (or whatever colors you want).
       displayPlayers(panel, runner, chaser);

       // Wait sleepTime ms between moves.
       panel.sleep(sleepTime);

  }
}
  // Move the players according to parameters.
  //movePlayers(runner, chaser, keyInput, boxSize, moveSize);
  public static void movePlayers(Point runner, Point chaser, char keyInput, int boxSize, int moveSize){
      if (keyInput == 'w')
        runner.translate(0, -moveSize);

      else if (keyInput == 's')
        runner.translate(0, moveSize);

      else if (keyInput == 'a')
        runner.translate(-moveSize, 0);

      else if (keyInput == 'd')
        runner.translate(moveSize, 0);


      double tempX = chaser.getX();
      double tempY = chaser.getY();

      double up, down, left, right;

      up = distance(runner.getX(),runner.getY(), tempX, tempY-1);
      down = distance(runner.getX(),runner.getY(), tempX, tempY+1);
      left = distance(runner.getX(), runner.getY(), tempX-1, tempY);
      right = distance(runner.getX(), runner.getY(), tempX+1, tempY);

      if(up < down && up < left && up < right)
        chaser.translate(0,-(moveSize-1));

      else if(down < up && down < left && down < right)
        chaser.translate(0, (moveSize-1));

      else if(left < up && left < down && left < right)
        chaser.translate(-(moveSize-1), 0);

      else if(right < up && right < down && right < left)
        chaser.translate(moveSize-1, 0);


  }

  // Display players using Color.GREEN and Color.RED (or whatever colors you want).
  //displayPlayers(panel, runner, chaser);
  public static void displayPlayers(DrawingPanel panel, Point runner, Point chaser){
      Graphics g = panel.getGraphics();

      g.setColor(Color.GREEN);
      g.fillRect(runner.x, runner.y, 10, 10);

      g.setColor(Color.RED);
      g.fillRect(chaser.x, chaser.y, 10, 10);
  }

  //Method that turns the runner and chaser white
  public static void erasePlayers(DrawingPanel panel, Point runner, Point chaser){
      Graphics g = panel.getGraphics();

      g.setColor(Color.WHITE);
      g.fillRect(runner.x, runner.y, 10, 10);

      g.setColor(Color.WHITE);
      g.fillRect(chaser.x, chaser.y, 10, 10);

  }

  public static double distance(double x1, double y1, double x2, double y2){

    double x  = x1 - x2;
    double y = y1 - y2;
    double sqrX = Math.pow(x,2);
    double sqrY = Math.pow(y,2);

    return Math.sqrt(sqrX + sqrY);

  }

}
