import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;
import static java.util.Calendar.SECOND;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GraphicsTest {
    private final InputStream originalIn = System.in;

    private final String OPERATING_SYSTEM = getOperatingSystem();

    private final String namesFileName;
    private final String name;
    private final String gender;
    private final String outputFileName;
    private final String expectedOutputNamesFileFolder;
    private final String expectedOutputOperatingSystemFolder;
    private final String expectedOutputFilePath;

    public GraphicsTest(String namesFileName, String name, String gender) {
        this.namesFileName = namesFileName;
        this.name = name;
        this.gender = gender;
        String namesFilePrefix = namesFileName.split("\\.")[0];
        this.outputFileName = String.format("%s-%s-%s-actual.png", namesFilePrefix, name, gender);
        String expectedOutputFolderRoot = "sample_output";
        this.expectedOutputNamesFileFolder = expectedOutputFolderRoot + "/" + String.format("graphical output for %s",this.namesFileName);
        this.expectedOutputOperatingSystemFolder = String.format("%s Users Only",OPERATING_SYSTEM);
        String expectedOutputDirectoryPath = this.expectedOutputNamesFileFolder + "/" + this.expectedOutputOperatingSystemFolder;
        // need the if statement below because windows sample output have a different naming convention
        if (OPERATING_SYSTEM.toLowerCase().equals("windows"))
            this.expectedOutputFilePath = expectedOutputDirectoryPath +
                    String.format("/%s-%s-%s-%s.png", OPERATING_SYSTEM.toLowerCase(), namesFilePrefix, this.name, this.gender);
        else if (OPERATING_SYSTEM.toLowerCase().equals("linux"))  // Linux file names are CASE SENSITIVE
            this.expectedOutputFilePath = expectedOutputDirectoryPath +
                    String.format("/%s-%s-%s-actual.png", namesFilePrefix.toLowerCase(), this.name.toLowerCase(), this.gender.toLowerCase());
        else    // Mac OS file names are CASE SENSITIVE
            this.expectedOutputFilePath = expectedOutputDirectoryPath +
                    String.format("/%s-%s-%s-%s.png", OPERATING_SYSTEM.toLowerCase(), namesFilePrefix.toLowerCase(), this.name.toLowerCase(), this.gender.toLowerCase());
    }

    private static String getOperatingSystem() {
        String fullName = System.getProperty("os.name");
        if (fullName.contains("Mac")) {
            return "Mac";
        } else if (fullName.contains("Windows")) {
            return "Windows";
        } else if (fullName.contains("Ubuntu")) {
            return "Ubuntu";
        }
        return fullName;
    }

    public void generateOutputFile() {
        // generate output file (run BabyNames with output property pointed to output file)
        setNameFileContext();
        assertTimeoutPreemptively(ofSeconds(SECOND * 20), this::generateOutput);
    }

    public void compareOutputFileToExpected() throws IOException {
        if (!new File(outputFileName).exists()) {
            fail(outputFileName + " not found.");
        }

        // Confirm there is graphical output samples for the Operating System
        // Failed assumptions do not result in a test failure; rather, a failed assumption results in a test being aborted
        assumeTrue(getContentsOfDir(this.expectedOutputNamesFileFolder).contains(this.expectedOutputOperatingSystemFolder),
                "The is no sample output for the current machine OS (" + OPERATING_SYSTEM + ")."
        );

        // compare pixels
        testPixelDifference();
    }

    private void backupBadFile() throws IOException {
        String newFilePath = outputFileName.replace(".png",".bad.png");
        Files.copy(Paths.get(outputFileName), Paths.get(newFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

//    public void cleanOutput() {
//        System.out.println("Deleting the file\"" + outputFileName + "\"");
//        File file = new File(outputFileName);
//        if (file.exists()) {
//            if(!file.delete()) System.out.println("   ERROR deleting file");
//            return;
//        }
//
//        System.out.println("   \"" + outputFileName + "\" could not be deleted because it was not found");
//    }

    private void setNameFileContext() {
        if (namesFileName.equals("names2.txt")) TestUtils.setNames2Variables();
        else TestUtils.setNames1Variables();
        System.out.println("Name file context set to " + TestUtils.NameFileContext);
    }

    private void generateOutput() throws Exception {
//            System.out.println("Generating output file for " + name + "/" + gender + " (" + System.getProperty(DrawingPanel.SAVE_PROPERTY) + ")");
        System.out.println("Generating output file for " + name + "/" + gender);

        // remove the output file (if it already exists)
//        cleanOutput();

        System.out.println("   Executing BabyNames");

        // set the System Property named by the DrawingPanel object to identify the output file
        // the default for this system property is NULL and when that is the case the DrawingPanel output
        // is defined by the calling class name (see line 677 in the DrawingPanel object)
        System.setProperty(DrawingPanel.SAVE_PROPERTY, outputFileName);
        System.out.println("   Output file destination set to " + System.getProperty(DrawingPanel.SAVE_PROPERTY));

        // %1$ is the first argument in the list (System.lineSeparator())
        // %2$ is the second argument in the list (name)
        // %3$ is the third argument in the list (gender)
        // All three are printed using the "s" format (string)
        // prints out name, line separator, gender, and then line separator again (all as strings)
        String userInput = String.format("%2$s%1$s%3$s%1$s", System.lineSeparator(), name, gender);
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        try {
            BabyNames.main(null);
            DrawingPanel.saveAll();
        } catch (Exception ex) {
            System.out.println("Exception thrown while executing BabyNames and DrawingPanel.saveAll():  " + ex);
            throw ex;
        }

        System.setIn(originalIn);

        System.out.println("      Output file generated for " + name + "/" + gender + "(graphic output stored in " + System.getProperty(DrawingPanel.SAVE_PROPERTY));
    }

    private void testPixelDifference() throws IOException {
        long pixelDiff = getPixelDifference();
        int MAX_PIXEL_DIFF_ALLOWED = 0;
        if (pixelDiff > MAX_PIXEL_DIFF_ALLOWED) {
            backupBadFile();
        }
        String message = String.format("MAX_PIXEL_DIFF_ALLOWED: %s \n pixelDiff: %s \n (%2$s <= %1$s) == %s",
                MAX_PIXEL_DIFF_ALLOWED, pixelDiff, pixelDiff <= MAX_PIXEL_DIFF_ALLOWED);
        assertTrue(pixelDiff <= MAX_PIXEL_DIFF_ALLOWED, message);

        // remove the output file (no longer needed)
//        cleanOutput();
    }

    private List<String> getContentsOfDir(String stringPath) {
        List<String> contents = new ArrayList<>();
        try (Stream<Path> paths = Files.list(new File(stringPath).toPath())) {
            paths.forEach((path -> contents.add(path.getFileName().toString())));
        } catch (IOException e) {
            System.out.println("An Error occurred:");
            e.printStackTrace();
        }
        return contents;
    }

    private long getPixelDifference() throws IOException {
        System.out.println("Comparing output file " + outputFileName + " to " + expectedOutputFilePath);

        InputStream actualStream = null;
        InputStream expectedStream = null;

        try {
            actualStream = new FileInputStream(outputFileName);
            expectedStream = new FileInputStream(expectedOutputFilePath);

            BufferedImage actualImage = ImageIO.read(actualStream);
            BufferedImage expectedImage = ImageIO.read(expectedStream);

            int actualImageWidth = actualImage.getWidth();
            int expectedImageWidth = expectedImage.getWidth();
            int actualImageHeight = actualImage.getHeight();
            int expectedImageHeight = expectedImage.getHeight();
            if ((actualImageWidth != expectedImageWidth) || (actualImageHeight != expectedImageHeight)) {
//                backupBadFile(fileName, name, gender);
                String message = String.format("Dimensions do not match. %1$s" +
                                "Expected Height: %2$s, Actual Height: %3$s %1$s" +
                                "Expected Width: %4$s, Actual Width: %5$s",
                        System.lineSeparator(),
                        expectedImageHeight,
                        actualImageHeight,
                        expectedImageWidth,
                        actualImageWidth);
                fail(message); //dimensions must match
                return Long.MAX_VALUE;
            } else {
                long diff = 0;
                for (int j = 0; j < actualImageHeight; j++) {
                    for (int i = 0; i < actualImageWidth; i++) {
                        //Getting the RGB values of a pixel
                        int pixel1 = actualImage.getRGB(i, j);
                        Color color1 = new Color(pixel1, true);
                        int r1 = color1.getRed();
                        int g1 = color1.getGreen();
                        int b1 = color1.getBlue();
                        int pixel2 = expectedImage.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int r2 = color2.getRed();
                        int g2 = color2.getGreen();
                        int b2 = color2.getBlue();
                        //sum of differences of RGB values of the two images
                        if (r1 - r2 != 0 || g1 - g2 != 0 || b1 - b2 != 0) {
                            diff += 1;
                        }
                    }
                }
                return diff;
            }
        } catch (Exception ex) {
            System.out.println("Error reading one or both image files - actual: " +
                    outputFileName + "   expected: " + expectedOutputFilePath);
            throw ex;
        } finally {
            if (actualStream != null) {
                try {
                    actualStream.close();
                } catch (IOException ex) {
                    System.out.println("Error closing actual file stream: " + outputFileName);
                }
            }
            if (expectedStream != null) {
                try {
                    expectedStream.close();
                } catch (IOException ex) {
                    System.out.println("Error closing expected file stream: " + expectedOutputFilePath);
                }
            }

            System.out.println("   Output file (" + outputFileName + ") compared");
        }
    }
}
