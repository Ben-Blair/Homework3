/*
Created by Ben Blair on February 9 2024
Used Generative AI: ChatGPT
What still needs to be improved:
    no if statments
    Use only 2 Scanner objects
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
    private static Integer startingYear = 1863; // change the value according to spec
    private static Integer decadeWidth = 50; // change the value according to spec
    private static Integer legendHeight = 20; // change the value according to spec

    // YOU ARE NOT ALLOWED TO ADD ANY OTHER CLASS VARIABLES

    public static void main(String[] args) throws IOException {
        Scanner nameInfo = new Scanner(new File(nameFilename));
        System.out.println(MESSAGE_PREFIX);

        // Use the same Scanner object for both methods
        String result = searchInfo(nameInfo);
        System.out.println(result);

        // Close the scanner after use
        nameInfo.close();
    }

    public static String searchInfo(Scanner nameInfo) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("\nEnter Name: ");
        String name = console.nextLine().toUpperCase();
        System.out.print("Enter Gender (M or F): ");
        String gender = console.nextLine().toUpperCase();

        // Read meanings from the file
        String meaning = readMeaningFromFile(name, gender);

        if (!meaning.isEmpty()) {
            return meaning;
        } else {
            return name + " is not found.";
        }
    }

    // Read meaning from the file for the given name and gender
    public static String readMeaningFromFile(String name, String gender) throws FileNotFoundException {
        Scanner meaningScanner = new Scanner(new File(MEANING_FILENAME));
        while (meaningScanner.hasNextLine()) {
            String line = meaningScanner.nextLine();
            Scanner lineScanner = new Scanner(line);

            // Read the name, gender, and meaning tokens individually
            String nameToken = lineScanner.next();
            String genderToken = lineScanner.next();
            String meaningToken = lineScanner.nextLine().trim(); // Trim any leading/trailing spaces

            // Check if the name and gender match
            if (nameToken.equalsIgnoreCase(name) && genderToken.equalsIgnoreCase(gender)) {
                meaningScanner.close(); // Close the scanner before returning
                return meaningToken;
            }

            // Close the line scanner
            lineScanner.close();
        }

        // Close the meaning scanner
        meaningScanner.close();

        // Return an empty string if the name/gender combination is not found
        return "";
    }
}