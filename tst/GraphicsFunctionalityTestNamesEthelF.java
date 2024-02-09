import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNamesEthelF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNamesEthelF() {
        super("names.txt", "ethel", "F");
    }
}