import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNamesAbbieF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNamesAbbieF() {
        super("names.txt", "Abbie", "F");
    }
}