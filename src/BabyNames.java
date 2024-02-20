/*
Created by Ben Blair on February 9 2024
Used Generative AI: ChatGPT
This code lets the user enter a name and the program outputs the meaning
of the name and shows its popularity throughout the years on a bar graph.
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
    private static String nameFilename = "names2.txt";

    // Other class variables (Change the assigned value only. Do NOT change any of the variable types or names.)
    private static int startingYear = 1890;
    private static int decadeWidth = 60;
    private static int legendHeight = 30;

    public static void main(String[] args) throws IOException {
        getUserInputAndSearch();
    }

    // Function to get user input and start the search process
    public static void getUserInputAndSearch() throws FileNotFoundException {
        printWelcomeMessage();
        Scanner console = new Scanner(System.in);
        String name = getName(console); // Get the name input from the user
        String gender = getGender(console).toLowerCase(); // Get the gender input from the user and convert it to lowercase
        console.close();

        updateSettingsForFilename(); // Update settings based on the filename

        String nameAndNumbers = searchNameFromFile(nameFilename, name, gender); // Search for name and associated numbers
        if (!nameAndNumbers.equals("\"" + name + "\"" + " is not found")) { // If the name is found
            processNameAndNumbers(name, gender, nameAndNumbers); // Process the name and associated numbers
        } else {
            // Assuming 'name' is the string variable holding the name in all uppercase
            String lowercaseName = name.toLowerCase();
            String correctedName = Character.toUpperCase(lowercaseName.charAt(0)) + lowercaseName.substring(1);

            System.out.print("\"" + correctedName + "\"" + " not found"); // Print message if the name is not found
        }
    }

    // Print the welcome message
    private static void printWelcomeMessage() {
        System.out.println(MESSAGE_PREFIX + startingYear);
    }

    // Get the name from the user
    private static String getName(Scanner console) {
        System.out.print("Name: ");
        return console.nextLine().toUpperCase(); // Return the input name in uppercase
    }

    // Get the gender from the user
    private static String getGender(Scanner console) {
        while (true) {
            System.out.print("Gender (M or F): ");
            String gender = console.nextLine().toUpperCase(); // Get the gender input from the user in uppercase
            if (gender.equals("M") || gender.equals("F")) { // Check if the input is valid
                return gender; // Return the gender input
            } else {
                System.out.println(gender + " is NOT a valid gender. Please enter M or F."); // Print error message for invalid input
            }
        }
    }

    // Update settings for the filename if necessary
    private static void updateSettingsForFilename() {
        if (nameFilename.equals("names2.txt")) { // Check if filename is names2.txt
            startingYear = 1863; // Change starting year if filename is names2.txt
            decadeWidth = 50; // Change decade width if filename is names2.txt
            legendHeight = 20; // Change legend height if filename is names2.txt
        }
    }

    // Process the name and associated numbers
    private static void processNameAndNumbers(String name, String gender, String nameAndNumbers) throws FileNotFoundException {
        System.out.println(nameAndNumbers); // Print the name and associated numbers
        String meaning = searchInfo(name, gender); // Search for the meaning of the name
        if (!meaning.equals("\"" + name + "\" not found.")) { // If meaning is found
            System.out.println(name + " " + gender + " " + meaning); // Output the meaning here
        }
        DrawingPanel panel = new DrawingPanel(OPEN_AREA_WIDTH, OPEN_AREA_HEIGHT + 2 * legendHeight); // Create a DrawingPanel object
        drawFixedGraphics(panel, meaning, name, gender); // Draw fixed graphics on the panel
    }

    // Search for the meaning of the name
    private static String searchInfo(String name, String gender) throws FileNotFoundException {
        String meaning = readMeaningFromFile(name, gender); // Read meaning from file
        return !meaning.isEmpty() ? meaning : "\"" + name + "\" not found."; // Return meaning if found, else return message
    }

    // Read the meaning from the meaning file
    private static String readMeaningFromFile(String name, String gender) throws FileNotFoundException {
        try (Scanner meaningScanner = new Scanner(new File(MEANING_FILENAME))) {
            while (meaningScanner.hasNextLine()) {
                String line = meaningScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                String nameToken = lineScanner.next();
                String genderToken = lineScanner.next();
                if (nameToken.equalsIgnoreCase(name) && genderToken.equalsIgnoreCase(gender)) { // Check if name and gender match
                    return lineScanner.nextLine().trim(); // Return the meaning
                }
            }
        }
        return ""; // Return empty string if no meaning found
    }

    // Draw fixed graphics such as legends, decades, and rankings
    private static void drawFixedGraphics(DrawingPanel panel, String meaning, String name, String gender) throws FileNotFoundException {
        Graphics g = panel.getGraphics(); // Get graphics object from panel
        panel.setBackground(Color.WHITE); // Set background color

        drawLegend(g, name, gender, meaning); // Draw legend
        drawDecades(g); // Draw decades
        drawRankings(g, name, gender); // Draw rankings

        g.dispose(); // Release resources
    }

    // Draw legend with name, gender, and meaning
    private static void drawLegend(Graphics g, String name, String gender, String meaning) {
        int yOffset = nameFilename.equals("names2.txt") ? 10 : 0; // Offset for y-axis

        g.setColor(Color.LIGHT_GRAY); // Set color for legend background
        g.fillRect(0, 0, OPEN_AREA_WIDTH, legendHeight); // Draw legend background
        g.fillRect(0, OPEN_AREA_HEIGHT + legendHeight, OPEN_AREA_WIDTH, legendHeight); // Draw bottom legend background

        g.setColor(Color.BLACK); // Set color for legend lines and text
        g.drawLine(0, legendHeight, OPEN_AREA_WIDTH, legendHeight); // Draw horizontal line in legend
        g.drawLine(0, OPEN_AREA_HEIGHT + legendHeight, OPEN_AREA_WIDTH, OPEN_AREA_HEIGHT + legendHeight); // Draw bottom horizontal line in legend

        g.setColor(Color.BLACK); // Set color for legend text
        g.drawString(name + " " + gender.toLowerCase() + " " + meaning, 0, legendHeight - 14 + yOffset); // Draw legend text
    }

    // Draw decades on the graph
    private static void drawDecades(Graphics g) {
        int decadesToShow = 13; // Default to 13 decades
        if (nameFilename.equals("names2.txt")) {
            decadesToShow = 8; // If names2.txt, show only 8 decades
        }
        int decade = startingYear;
        int endYear = startingYear + decadesToShow * 10; // Calculate the end year based on the number of decades to show
        for (int x = 0; x < OPEN_AREA_WIDTH && decade < endYear; x += decadeWidth) {
            g.drawString(Integer.toString(decade), x, OPEN_AREA_HEIGHT + 2 * legendHeight - 8); // Draw decade label
            decade += 10; // Move to next decade
        }
    }

    // Draw rankings of the given name and gender
    private static void drawRankings(Graphics g, String name, String gender) throws FileNotFoundException {
        File file = new File(nameFilename); // Create file object with specified name file
        Scanner scanner = new Scanner(file); // Create scanner object to read from file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String currentName = lineScanner.next();
            String currentGender = lineScanner.next();

            if (currentName.equalsIgnoreCase(name) && currentGender.equalsIgnoreCase(gender)) { // Check if name and gender match
                int decadeX = 0; // Initialize decade X position
                while (lineScanner.hasNextInt()) {
                    int rank = lineScanner.nextInt(); // Get rank from file
                    int y = (rank / 2) + legendHeight; // Calculate Y position based on rank
                    g.setColor(Color.GREEN); // Set color for rankings
                    if (rank != 0) {
                        g.fillRect(decadeX, y, decadeWidth / 2, OPEN_AREA_HEIGHT + legendHeight - y); // Draw ranking bar
                    }
                    g.setColor(Color.BLACK); // Set color for ranking text
                    if (rank != 0) {
                        g.drawString(Integer.toString(rank), decadeX, y); // Draw ranking number
                        decadeX += decadeWidth; // Move to next decade
                    } else {
                        g.drawString(Integer.toString(rank), decadeX, legendHeight + OPEN_AREA_HEIGHT); // Draw ranking number
                        decadeX += decadeWidth; // Move to next decade
                    }
                }
                break;
            }
        }
        scanner.close(); // Close scanner
    }

    // Search for the name in the specified file
    public static String searchNameFromFile(String filename, String name, String gender) throws FileNotFoundException {
        File file = new File(filename); // Create file object with specified name file
        Scanner scanner = new Scanner(file); // Create scanner object to read from file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String currentName = lineScanner.next();
            String currentGender = lineScanner.next();

            if (currentName.equalsIgnoreCase(name) && currentGender.equalsIgnoreCase(gender)) { // Check if name and gender match
                scanner.close(); // Close scanner
                return line; // Return line containing the name
            }
        }
        scanner.close(); // Close scanner
        return "\"" + name + "\"" + " is not found"; // Return a message indicating name is not found
    }
}