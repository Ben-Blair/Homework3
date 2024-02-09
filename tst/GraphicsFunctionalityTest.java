import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public abstract class GraphicsFunctionalityTest {
    private final GraphicsTest test;

    public GraphicsFunctionalityTest(String namesFileName, String name, String gender) {
        this.test = new GraphicsTest(namesFileName, name, gender);
    }

    @BeforeAll
    public static void disableSystemExit() throws IOException {
        TestUtils.forbidSystemExitCall();
    }

    @AfterAll
    public static void restoreEnvironmentToOriginalSettings() throws IOException {
        TestUtils.enableSystemExit();
    }

    @Test
    @DisplayName("Generate Output")
    @Order(1)
    @ResourceLock(value = "OutputFile", mode = ResourceAccessMode.READ_WRITE)
    public void generateOutputFile() {
        test.generateOutputFile();
    }

    @Test
    @DisplayName("Compare to Expected")
    @Order(2)
    @ResourceLock(value = "OutputFile", mode = ResourceAccessMode.READ_WRITE)
    public void compareOutputFileToExpected() throws IOException {
        test.compareOutputFileToExpected();
    }
}