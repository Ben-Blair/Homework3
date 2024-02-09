import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Isolated;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("GraphicalFunctionality")
@Isolated
public class GraphicsFunctionalityTestNames2DonM extends GraphicsFunctionalityTest {
    public GraphicsFunctionalityTestNames2DonM() {
        super("names2.txt", "Don", "M");
    }
}