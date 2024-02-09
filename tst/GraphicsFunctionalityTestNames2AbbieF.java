import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNames2AbbieF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNames2AbbieF() {
        super("names2.txt", "Abbie", "F");
    }
}
