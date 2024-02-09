import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsoleTest {
    private static final InputStream originalIn = System.in;
    private static final PrintStream originalOut = System.out;

    private final String namesFileName;
    private final String name;
    private final String gender;
    private final String expectedOutput;

    private static final String MESSAGE_PREFIX = "This program allows you to search through the\n" +
            "data from the Social Security Administration\n" +
            "to see how popular a particular name has been\n" +
            "since ";

    public ConsoleTest(String namesFileName, String name, String gender, String expectedOutput) {
        this.namesFileName = namesFileName;
        this.name = name;
        this.gender = gender;
        this.expectedOutput = expectedOutput;
    }

    private static String getActualVsExpectedMessage(String actual, String expected) {
        return String.format("Actual Output:\n%s\n\nExpected Output:\n%s\n\n", actual, expected);
    }

    private void setNameFileContext() {
        System.out.println("Entering ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.setNameFileContext()");

        if (namesFileName.equals("names2.txt")) TestUtils.setNames2Variables();
        else TestUtils.setNames1Variables();

        System.out.println("   Name file context set to " + TestUtils.NameFileContext);

        System.out.println("Exiting ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.setNameFileContext()");
    }

    // Helper method to confirm console output for a specific prompt contains the specified String
    public void testConsoleOutput() {
        System.out.println("Entering ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.testConsoleOutput()");

        setNameFileContext();

        String mainOutput = captureMainOutput();
        String message = getActualVsExpectedMessage(mainOutput, expectedOutput);
        assertTrue(mainOutput.contains(expectedOutput), message);

        System.out.println("Exiting ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.testConsoleOutput()");
    }

    // Helper method returned all console output as a String for a specific prompt
    private String captureMainOutput() {
        System.out.println("Entering ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.captureMainOutput()");

        try {
            // %1$ is the first argument in the list (System.lineSeparator())
            // %2$ is the second argument in the list (name)
            // %3$ is the third argument in the list (gender)
            // All three are printed using the "s" format (string)
            String userInput = String.format("%2$s%1$s%3$s%1$s", System.lineSeparator(), name, gender);  // prints out name then line separator then gender then line separator again (all as strings)

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(outputStream);

            System.out.flush(); // force any buffered output to be written immediately (i.e., if a print statement was issued with no println) - failure to do this may mean that some text written to the console is LOST (i.e., NOT captured in the printstream)

            System.setIn(new ByteArrayInputStream(userInput.getBytes()));
            System.setOut(ps);

            try {
                BabyNames.main(null);
                System.out.flush(); // force any buffered output to be written immediately (i.e., if a print statement was issued with no println) - failure to do this may mean that some text written to the console is LOST (i.e., NOT captured in the printstream)
            } catch (Exception ex) {
                // need to reset System.out to get any error information
                System.setOut(originalOut);

                System.out.println("Exception thrown while executing BabyNames\n\n" + ex + "\n" + ex.getStackTrace());
                throw ex;
            }
            finally {
                System.setOut(originalOut);
                System.setIn(originalIn);
            }

            System.out.println("Exiting ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.captureMainOutput()");
            return outputStream.toString();
        } catch (Exception e) {
            // this is just here to catch a FileNotFoundException so test methods do not need the checked exception
            System.out.println("Exiting ConsoleTest<" + this.namesFileName + "/" +this.name + "/" + this.gender + ">.captureMainOutput() after throwing exception: " + e);
            return "";
        }
    }
}
