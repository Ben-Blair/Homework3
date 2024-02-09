import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNamesDonM extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNamesDonM() {
        super("names.txt", "Don", "M");
    }
}