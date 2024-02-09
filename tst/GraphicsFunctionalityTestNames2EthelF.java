import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNames2EthelF extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNames2EthelF() {
        super("names2.txt", "Ethel", "F");
    }
}
