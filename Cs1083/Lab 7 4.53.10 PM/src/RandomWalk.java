/*This program will simulate a random walk using a Graphics object.
It will ask the user for the Radius and Color of a Circle to house the random walk.
The random walk will start at the center of a circle and continue until it goes outside the circle.
 */

import java.util.*;
import java.awt.*;

public class RandomWalk {
    public static final Scanner CONSOLE = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("Lab 7 Written By Eric Escalante");
        System.out.println();

        System.out.println("Enter Radius of Circle (50 - 400): ");
        int radius = CONSOLE.nextInt();

        int xCenter=radius+10;
        int yCenter=radius+10;

        //while loop to ensure that the radius of the circle is between 50 and 400.
        while (radius < 50 || radius > 400) {
            System.out.println("Enter Radius of Circle (50 - 400): ");
            radius = CONSOLE.nextInt();
        }

        DrawingPanel panel = new DrawingPanel(radius * 2+20, radius * 2+20);
        Graphics g = panel.getGraphics();

        System.out.println("Enter Color of Circle: ");
        String circleColor = CONSOLE.next();

        //while loop to ensure that the user selects one of the color choices.
        while (true) {
            if (colorChoice(circleColor, "Blue")) {
                g.setColor(Color.BLUE);
                break;
            } else if (colorChoice(circleColor, "Orange")) {
                g.setColor(Color.ORANGE);
                break;
            } else {
                System.out.println("Invalid color, enter a valid color: ");
                circleColor = CONSOLE.nextLine();
            }
        }

        g.drawOval(10, 10, radius * 2, radius * 2);

        //Each step will be randomly chosen from one pixel up, one pixel down, one pixel left, and one pixel right
        Random ran = new Random();
        int x1 =xCenter;
        int y1 =yCenter;
        int x2 =x1;
        int y2 =y1;
        int count = 0;

        //while loop to continue drawing the random walk until the walk leaves the circle.
        while(!intersectPoint(radius, xCenter, x1, yCenter, y1)) {
            int i = ran.nextInt(4);

            if (i == 0) {
                g.drawLine(x1, y1, x2++, y2);
                count++;
            }
            if (i == 1) {
                g.drawLine(x1, y1, x2--, y2);
                count++;
            }
            if (i == 2) {
                g.drawLine(x1, y1, x2, y2++);
                count++;
            }
            if (i == 3) {
                g.drawLine(x1, y1, x2, y2--);
                count++;
            }
            panel.sleep(1);

            x1 = x2;
            y1 = y2;
        }

        System.out.println("Number of Steps Taken: " + count);

    }

    //This method to determine whether the current position of the random walk is outside the circle.
    //x1, y1 is the center of the circle200
    //x2, y2 is the current position of the random walk
    public static boolean intersectPoint (int r1, int x1, int x2, int y1, int y2) {

        double x = Math.pow(x1 - x2, 2);
        double y = Math.pow(y1 - y2, 2);
        double sum = x + y;
        double sqrt = Math.sqrt(sum);

        return sqrt >= r1;

    }

    //This method to determine whether a String answer entered by the user matches a String choice for the question.
    public static boolean colorChoice(String answer, String choice) {
        return answer.equalsIgnoreCase(choice) || answer.equalsIgnoreCase((choice).substring(0, 1));

    }

}
