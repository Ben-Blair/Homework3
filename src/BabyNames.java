/*
Created by Ben Blair on February 9 2024
Used Generative AI: ChatGPT
to-do: replace all if-else statements and add comments
 */

import java.awt.*;
import java.io.*;
import java.util.*;
public class BabyNames {

    //class constants for open area in graphics (Do NOT change)
    private static final Integer OPEN_AREA_WIDTH = 780;
    private static final Integer OPEN_AREA_HEIGHT = 500;

    //prompt msg class constant (Do NOT change)
    private static final String MESSAGE_PREFIX = "This program allows you to search through the\n" +
            "data from the Social Security Administration\n" +
            "to see how popular a particular name has been\n" +
            "since ";

    // class constant for meaning file (Do NOT change)
    private static final String MEANING_FILENAME = "meanings.txt";

    // class variable for name file (Change the assigned value only. Do NOT change the type or the variable name)
    // Test with both "names.txt" and "names2.txt" (Before submission, change back to "names.txt")
    private static String nameFilename = "names.txt";

    // Other class variables (Change the assigned value only. Do NOT change any of the variable types or names.)
    private static int startingYear = 1890;
    private static int decadeWidth = 60;
    private static int legendHeight = 30;

    public static void main(String[] args) throws IOException {
        System.out.println(MESSAGE_PREFIX + startingYear);
        Scanner console = new Scanner(System.in);
        String name = getName(console);
        String gender = getGender(console);
        console.close();

        if (nameFilename.equals("names2.txt")) {
            startingYear = 1863;
            decadeWidth = 50;
            legendHeight = 20;
        }

        String result = searchInfo(name, gender);
        System.out.println(result);

        DrawingPanel panel = new DrawingPanel(OPEN_AREA_WIDTH, OPEN_AREA_HEIGHT + 2 * legendHeight);
        drawFixedGraphics(panel, result, name, gender);
    }

    public static String getGender(Scanner console) {
        while (true) {
            System.out.print("Gender (M or F): ");
            String gender = console.nextLine().toUpperCase();
            if (gender.equals("M") || gender.equals("F")) {
                return gender;
            } else {
                System.out.println(gender + " is NOT a valid gender. Please enter M or F.");
            }
        }
    }

    public static String getName(Scanner console) {
        System.out.print("Name: ");
        return console.nextLine().toUpperCase();
    }

    public static String searchInfo(String name, String gender) throws FileNotFoundException {
        String meaning = readMeaningFromFile(name, gender);
        return !meaning.isEmpty() ? meaning : "\"" + name + "\" not found.";
    }

    public static String readMeaningFromFile(String name, String gender) throws FileNotFoundException {
        try (Scanner meaningScanner = new Scanner(new File(MEANING_FILENAME))) {
            while (meaningScanner.hasNextLine()) {
                String line = meaningScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                String nameToken = lineScanner.next();
                String genderToken = lineScanner.next();
                if (nameToken.equalsIgnoreCase(name) && genderToken.equalsIgnoreCase(gender)) {
                    return lineScanner.nextLine().trim();
                }
            }
        }
        return "";
    }

    public static void drawFixedGraphics(DrawingPanel panel, String meaning, String name, String gender) throws FileNotFoundException {
        Graphics g = panel.getGraphics();
        panel.setBackground(Color.WHITE);

        drawLegend(g, name, gender, meaning);
        drawDecades(g);
        drawRankings(g, name, gender);

        g.dispose();
    }

    private static void drawLegend(Graphics g, String name, String gender, String meaning) {
        int yOffset = nameFilename.equals("names2.txt") ? 10 : 0; // Offset for y-axis

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, OPEN_AREA_WIDTH, legendHeight);
        g.fillRect(0, OPEN_AREA_HEIGHT + legendHeight, OPEN_AREA_WIDTH, legendHeight);

        g.setColor(Color.BLACK);
        g.drawLine(0, legendHeight, OPEN_AREA_WIDTH, legendHeight);
        g.drawLine(0, OPEN_AREA_HEIGHT + legendHeight, OPEN_AREA_WIDTH, OPEN_AREA_HEIGHT + legendHeight);

        g.setColor(Color.BLACK);
        g.drawString(name + " " + gender.toLowerCase() + " " + meaning, 0, legendHeight - 14 + yOffset);
    }

    private static void drawDecades(Graphics g) {
        int decadesToShow = 13; // Default to 13 decades
        if (nameFilename.equals("names2.txt")) {
            decadesToShow = 8; // If names2.txt, show only 8 decades
        }
        int decade = startingYear;
        int endYear = startingYear + decadesToShow * 10; // Calculate the end year based on the number of decades to show
        for (int x = 0; x < OPEN_AREA_WIDTH && decade < endYear; x += decadeWidth) {
            g.drawString(Integer.toString(decade), x, OPEN_AREA_HEIGHT + 2 * legendHeight - 8);
            decade += 10;
        }
    }

    private static void drawRankings(Graphics g, String name, String gender) throws FileNotFoundException {
        File file = new File(nameFilename);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String currentName = lineScanner.next();
            String currentGender = lineScanner.next();

            if (currentName.equalsIgnoreCase(name) && currentGender.equalsIgnoreCase(gender)) {
                int decadeX = 0;
                while (lineScanner.hasNextInt()) {
                    int rank = lineScanner.nextInt();
                    int y = (rank / 2) + legendHeight;
                    g.setColor(Color.GREEN);
                    if (rank != 0) {
                        g.fillRect(decadeX, y, decadeWidth / 2, OPEN_AREA_HEIGHT + legendHeight - y);
                    }
                    g.setColor(Color.BLACK);
                    if (rank != 0) {
                        g.drawString(Integer.toString(rank), decadeX, y);
                        decadeX += decadeWidth;
                    } else {
                        g.drawString(Integer.toString(rank), decadeX, legendHeight + OPEN_AREA_HEIGHT);
                        decadeX += decadeWidth;
                    }
                }
                break;
            }
        }
        scanner.close();
    }
}