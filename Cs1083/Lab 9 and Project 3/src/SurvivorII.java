/*This program (game) will have a runner, chaser, and zombies.
The new element in this game is that the game will include zombies.
Each zombie the user inputs moves using a random walk.
The object of the game is for the runner to eliminate all zombies before the chaser catches the runner.
*/

import java.util.*;
import java.awt.*;

public class SurvivorII {
    public static final int PLAYER_SIZE = 10;
    public static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Project 3 written by Eric Escalante");
        System.out.println();

        System.out.print("Enter the number of zombies, box size, move size, and sleep time: ");

        // Parameters entered by the user:
        int numZombies = CONSOLE.nextInt();     //user input number of zombies

        int boxSize = CONSOLE.nextInt();        // user input box size; w/parameters
        if(boxSize<0)
            boxSize = 760;

        int moveSize = CONSOLE.nextInt();       // user input size of moves; w/parameters
        if(moveSize<0)
            moveSize = 10;

        int sleepTime = CONSOLE.nextInt();      // user input amount of sleep time; w/parameters
        if(sleepTime<0)
            sleepTime = 1000;

        // Create DrawingPanel and draw a box in the panel
        DrawingPanel panel = new DrawingPanel(boxSize + 40, boxSize + 40);
        Graphics g = panel.getGraphics();
        g.fillRect(10, 10, 10, boxSize + 20);
        g.fillRect(10, 10, boxSize + 20, 10);
        g.fillRect(boxSize + 20, 10, 10, boxSize + 20);
        g.fillRect(10, boxSize + 20, boxSize + 20, 10);


        // Initialize positions of runner, chaser, and random zombies:
        Random random = new Random();
        Point runner = new Point(boxSize/4, boxSize/2);
        Point chaser = new Point((boxSize/4)*3, boxSize/2);

        Point[] zombies = new Point[numZombies];          // Array of zombies based on number of zombies

        boolean[] eliminated = new boolean[numZombies];   // The program needs to remember whether it is eliminated or not
        Arrays.fill(eliminated, Boolean.FALSE);           // Final Project

        // Game Settings
        char keyInput = ' ';                              // Variable for input from user to move runner.
        boolean lost = false;                             // used to indicate when the game is lost
        boolean gameOver = false;                         // used to keep game in motion

        Point p1 = new Point(0,0);                  // initializing point p1
        Point p2 = new Point(0,0);                  // initializing point p2

        // Loop needed to show how many zombies need to appear
        for(int i=0; i<numZombies; i++){
            Point tempZombie = new Point(random.nextInt(boxSize)+20, random.nextInt(boxSize) +20);
            zombies[i] = tempZombie;
        }

        // Display players using Color.GREEN and Color.RED  and Color.BLUE (or whatever colors you want).
        displayPlayers(panel, runner, chaser, zombies, eliminated, numZombies);

        // Wait one second before start of game.
        panel.sleep(1000);

        // Wait until a character is entered
        keyInput = panel.getKeyChar();
        while (keyInput == 0) {
            keyInput = panel.getKeyChar();
        }

        while (!gameOver) {

            // Erase players from the panel using Color.WHITE
            erasePlayers(panel, runner, chaser, zombies, numZombies);

            // Get input from user using characters w, a, s, d
            char newKeyInput = panel.getKeyChar();
            if (newKeyInput == 'w' || newKeyInput == 'a' || newKeyInput == 's' || newKeyInput == 'd') {
                keyInput = newKeyInput;
            }

            // Move the players according to parameters.
            movePlayers(runner, chaser, eliminated, keyInput, boxSize, moveSize, zombies, p1, p2);

            // Display players using Color.GREEN and Color.RED (or whatever colors you want).
            displayPlayers(panel, runner, chaser, zombies, eliminated, numZombies);

            // Game is over if the runner collides with chaser or zombie.
            if (collision(runner, chaser, zombies, panel, numZombies, eliminated)) {
                g.drawString("YOU LOST!!!", boxSize/2, boxSize/2);
                lost = true;
            }

            // Wait sleepTime ms between moves.
            panel.sleep(sleepTime);

            // Game continues until all zombies are eliminated
            int numEliminated = 0;
            boolean eZombies = false;

            // loop needed to eliminate all zombies on the board
            for (boolean bValue : eliminated) {
                if(bValue) numEliminated+=1;
                if (numEliminated == eliminated.length) eZombies = true;

            }
            // ends the game if chaser meets runner or all zombies are eliminated
            if (lost || eZombies) break;
        }

        if(!lost) {
            g.setColor(Color.BLACK);
            g.drawString("YOU WIN!!!", boxSize/2, boxSize/2);
        }

    }

    // Display players using Color.GREEN (runner) and Color.BLUE (zombies)
    public static void displayPlayers(DrawingPanel panel, Point runner, Point chaser, Point[] zombies, boolean[] eliminated, int numZombies) {
        Graphics g = panel.getGraphics();

        int i = 0;
        Point tempZombie = zombies[i];

        // Player: RUNNER
        g.setColor(Color.GREEN);
        g.fillRect((int)runner.getX() - PLAYER_SIZE / 2, (int)runner.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

        // Player: ZOMBIES
        g.setColor(Color.BLUE);
        for(i = 0; i<numZombies; i++) {
            tempZombie = zombies[i];
                if (! eliminated[i]) {

                g.fillRect(tempZombie.x - PLAYER_SIZE / 2, tempZombie.y - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
                //eliminated[i] = true;
                }
        }

        // Player: CHASER
        g.setColor(Color.RED);
        g.fillRect((int)chaser.getX() - PLAYER_SIZE / 2, (int)chaser.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

    }

    //Method that turns the runner and chaser white after it moves
    public static void erasePlayers(DrawingPanel panel, Point runner, Point chaser, Point[] zombies, int numZombies) {

        Random random = new Random();
        Graphics g = panel.getGraphics();
        g.setColor(Color.WHITE);

        // erase zombie
        for(int i = 0; i < numZombies; i++){
            g.fillRect( zombies[i].x - PLAYER_SIZE / 2, zombies[i].y - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);
        }

        // erase runner
        g.fillRect((int)runner.getX() - PLAYER_SIZE / 2, (int)runner.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

        // erase chaser
        g.fillRect((int)chaser.getX() - PLAYER_SIZE / 2, (int)chaser.getY() - PLAYER_SIZE / 2, PLAYER_SIZE, PLAYER_SIZE);

    }

    // The runner moves moveSize (or zero) pixels each time step.
    // The chaser moves moveSize - 1 pixels each time step.
    // Move the players according to parameters.
    public static void movePlayers(Point runner, Point chaser, boolean[] eliminated,
                                   char keyInput, int boxSize, int moveSize, Point[] zombies, Point p1, Point p2) {

        processKeyInput(runner, keyInput, boxSize, moveSize);
        moveZombie(zombies, eliminated, boxSize, moveSize, zombies.length);
        moveChaser(runner, chaser, moveSize);

    }



    //This method will keep all players within the box size
    public static boolean legal(Point p, int dx, int dy, int boxSize) {
        return p.getX() + dx + PLAYER_SIZE / 2 < boxSize && p.getX() + dx - PLAYER_SIZE / 2 > 20
                && p.getY() + dy + PLAYER_SIZE / 2 < boxSize && p.getY() + dy - PLAYER_SIZE / 2 > 20;
    }


    //This method will user the distance formula to show the distance between two points
    public static double distance(double x1, double y1, double x2, double y2){

        double x  = x1 - x2;
        double y = y1 - y2;
        double sqrX = Math.pow(x,2);
        double sqrY = Math.pow(y,2);

        return Math.sqrt(sqrX + sqrY);

    }

    //This method will move the zombies using a random walk
    public static void moveZombie(Point[] zombies, boolean[] eliminated, int boxSize, int moveSize, int numZombies) {
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

    // The chaser should move moveSize - 1 pixels each time step
    public static void moveChaser(Point runner, Point chaser, int moveSize) {

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
            chaser.translate((moveSize-1), 0);
    }

    // Method allows the runner to move directions using the characters
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

    // A collision between the runner and any zombie should result in setting gameOver to true
    public static boolean collision(Point runner, Point chaser, Point[] zombies, DrawingPanel panel, int numZombies, boolean[] eliminated) {
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
}
