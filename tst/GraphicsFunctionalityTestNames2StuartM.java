import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNames2StuartM extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNames2StuartM() {
        super("names2.txt", "Stuart", "M");
    }
}
