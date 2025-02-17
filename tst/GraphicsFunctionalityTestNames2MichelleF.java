import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNames2MichelleF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNames2MichelleF() {
        super("names2.txt", "Michelle", "F");
    }
}
