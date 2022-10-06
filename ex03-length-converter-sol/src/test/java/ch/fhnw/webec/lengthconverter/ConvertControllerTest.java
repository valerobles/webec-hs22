package ch.fhnw.webec.lengthconverter;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertControllerTest {

    ConvertController controller = new ConvertController();
    Model model = new ConcurrentModel();

    @Test
    public void testConvertView() {
        var view = controller.convert(10, 10, model);
        assertEquals("/convert", view);
    }

    @Test
    public void testModelFeetInches() {
        controller.convert(5, 10, model);
        assertEquals(5, model.getAttribute("feet"));
        assertEquals(10, model.getAttribute("inches"));
    }

    @Test
    public void testConvertSimple() {
        controller.convert(5, 10, model);
        assertEquals(177, model.getAttribute("cmPart"));
        assertEquals(8, model.getAttribute("mmPart"));
    }

    @Test
    public void testConvertSpecialCase0() {
        controller.convert(0, 0, model);
        assertEquals(0, model.getAttribute("cmPart"));
        assertEquals(0, model.getAttribute("mmPart"));
    }
}
