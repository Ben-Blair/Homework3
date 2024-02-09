import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

class BabyNamesTest {
    private static final String MESSAGE_PREFIX = "This program allows you to search through the\n" +
            "data from the Social Security Administration\n" +
            "to see how popular a particular name has been\n" +
            "since ";

    @Nested
    @DisplayName("Console Functionality")
    @Order(1)
    @TestClassOrder(ClassOrderer.OrderAnnotation.class)
    class ConsoleFunctionalityTest {

        @Nested
        @DisplayName("Names File (names_txt)")
        @Order(1)
        @TestClassOrder(ClassOrderer.OrderAnnotation.class)
        class NamesFileTest {
            private static final String namesFileName = "names.txt";

            @Nested
            @DisplayName("Prompt Functionality")
            @Order(1)
            class PromptFunctionalityTest {

                @Test
                @DisplayName("User Prompt")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void testPromptMessageNames() {
                    String expectedOutput = MESSAGE_PREFIX + TestUtils.STARTING_YEAR_1;
                    ConsoleTest test = new ConsoleTest(namesFileName, "Test", "M", expectedOutput);
                    test.testConsoleOutput();
                }
            }

            @Nested
            @DisplayName("User Input Case Sensitivity")
            @Order(2)
            class NameCaseFunctionalityTest {

                @Test
                @DisplayName("ChAsE M")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void testCaseInsensitivityNames() {
                    String expectedOutput = "Chase    M  0 0  0  0  0 0 0 0  0  659  110  77 66";
                    ConsoleTest t = new ConsoleTest(namesFileName, "ChAsE", "M", expectedOutput);
                    t.testConsoleOutput();
                }

                @Test
                @DisplayName("michelle F")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void test2CaseInsensitivityNames() {
                    String expectedOutput = "Michelle   F  0  0  0  0  0 728  173 39 4  10 22  52 125";
                    ConsoleTest t = new ConsoleTest(namesFileName, "michelle", "F", expectedOutput);
                    t.testConsoleOutput();
                }
            }

            @Nested
            @DisplayName("Console Output")
            @Order(3)
            @TestClassOrder(ClassOrderer.OrderAnnotation.class)
            class ConsoleOutputTest {

                @Nested
                @DisplayName("SHOULD Be Found")
                @Order(1)
                class Found {
                    @Test
                    @DisplayName("Floyd M")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void testCorrectConsoleFoundOutputNames() {
                        String expectedOutput = String.format("Floyd M  60  48  55 61 61  103 142  198  282 384  638  0  0%s",
                                System.lineSeparator()) +
                                String.format("FLOYD m English Variant of LLOYD%s", System.lineSeparator());
                        ConsoleTest t = new ConsoleTest(namesFileName, "Floyd", "M", expectedOutput);
                        t.testConsoleOutput();
                    }

                    @Test
                    @DisplayName("Nita F")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void test2CorrectConsoleFoundOutputNames() {
                        String expectedOutput = String.format("Nita   F  594 669 600  657  508  520 523  557 0  0 0 0  0%s",
                                System.lineSeparator()) +
                                String.format("NITA f English Short form of ending in nita; f Native American Means \"bear\"" +
                                        " in Choctaw.%s", System.lineSeparator());
                        ConsoleTest t = new ConsoleTest(namesFileName, "Nita", "F", expectedOutput);
                        t.testConsoleOutput();
                    }
                }

                @Nested
                @DisplayName("Should NOT Be Found")
                @Order(2)
                class NotFound {

                    @Test
                    @DisplayName("Dario F")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void testCorrectConsoleNotFoundOutputNames() {
                        String name = "Dario";
                        String expectedOutput = String.format("\"%s\" not found.", name);
                        ConsoleTest t = new ConsoleTest(namesFileName, name, "F", expectedOutput);
                        t.testConsoleOutput();
                    }

                    @Test
                    @DisplayName("abbie M")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void test2CorrectConsoleNotFoundOutputNames() {
                        String name = "abbie";
                        String expectedOutput = String.format("\"%s\" not found.", name);
                        ConsoleTest t = new ConsoleTest(namesFileName, name, "M", expectedOutput);
                        t.testConsoleOutput();
                    }
                }
            }
        }

        @Nested
        @DisplayName("Names File (names2_txt)")
        @Order(2)
        @TestClassOrder(ClassOrderer.OrderAnnotation.class)
        class Names2FileTest {
            private static final String namesFileName = "names2.txt";

            @Nested
            @DisplayName("Prompt Functionality")
            @Order(1)
            class PromptFunctionalityTest {

                @Test
                @DisplayName("User Prompt")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void testPromptMessageNames2() {
                    String expectedOutput = MESSAGE_PREFIX + TestUtils.STARTING_YEAR_2;
                    ConsoleTest t = new ConsoleTest(namesFileName, "Test", "M", expectedOutput);
                    t.testConsoleOutput();
                }
            }

            @Nested
            @DisplayName("User Input Case Sensitivity")
            @Order(2)
            class NameCaseFunctionalityTest {

                @Test
                @DisplayName("michelle F")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void testCaseInsensitivityNames2() {
                    String expectedOutput = "Michelle   f 0 0 0  0  0 728 174  39";
                    ConsoleTest t = new ConsoleTest(namesFileName, "michelle", "F", expectedOutput);
                    t.testConsoleOutput();
                }

                @Test
                @DisplayName("zOIdBErg M")
                @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                public void test2CaseInsensitivityNames2() {
                    String name = "zOIdBErg";
                    String expectedOutput = String.format("\"%s\" not found.", name);
                    ConsoleTest t = new ConsoleTest(namesFileName, name, "M", expectedOutput);
                    t.testConsoleOutput();
                }
            }

            @Nested
            @DisplayName("Console Output")
            @Order(3)
            @TestClassOrder(ClassOrderer.OrderAnnotation.class)
            class ConsoleOutputTest {

                @Nested
                @DisplayName("SHOULD Be Found")
                @Order(1)
                class Found {

                    @Test
                    @DisplayName("Darleen F")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void testCorrectConsoleFoundOutputNames2() {
                        String expectedOutput = String.format("Darleen f  0  0 0  0 626 599 654 839%s", System.lineSeparator()) +
                                String.format("DARLEEN f English Variant of DARLENE%s", System.lineSeparator());
                        ConsoleTest t = new ConsoleTest(namesFileName, "Darleen", "F", expectedOutput);
                        t.testConsoleOutput();
                    }

                    @Test
                    @DisplayName("Jessie M")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void test2CorrectConsoleFoundOutputNames2() {
                        String expectedOutput = String.format("Jessie   m 73  69  72  111 133  162 203  260%s", System.lineSeparator())
                                + String.format("JESSIE M (no meaning found)%s", System.lineSeparator());
                        ConsoleTest t = new ConsoleTest(namesFileName, "Jessie", "M", expectedOutput);
                        t.testConsoleOutput();
                    }
                }

                @Nested
                @DisplayName("Should NOT Be Found")
                @Order(2)
                class NotFound {

                    @Test
                    @DisplayName("Abraham F")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void testCorrectConsoleNotFoundOutputNames2() {
                        String name = "Abraham";
                        String expectedOutput = String.format("\"%s\" not found.", name);
                        ConsoleTest t = new ConsoleTest(namesFileName, name, "F", expectedOutput);
                        t.testConsoleOutput();
                    }

                    @Test
                    @DisplayName("tod F")
                    @ResourceLock(value = "System.In", mode = ResourceAccessMode.READ_WRITE)
                    public void test2CorrectConsoleNotFoundOutputNames2() {
                        String name = "tod";
                        String expectedOutput = String.format("\"%s\" not found.", name);
                        ConsoleTest t = new ConsoleTest(namesFileName, name, "F", expectedOutput);
                        t.testConsoleOutput();
                    }
                }
            }
        }
    }
}