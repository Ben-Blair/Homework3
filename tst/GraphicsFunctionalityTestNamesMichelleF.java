import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNamesMichelleF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNamesMichelleF() {
        super("names.txt", "michelle", "F");
    }
}