package ch.fhnw.webec.lengthconverter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConvertController {

    @GetMapping("/convert")
    public String convert(int feet, int inches, Model model) {
        var cm = 30.48 * feet + 2.54 * inches;
        var cmPart = (int) cm;
        var mmPart = (int) (cm * 10 % 10);

        model.addAttribute("feet", feet);
        model.addAttribute("inches", inches);
        model.addAttribute("cmPart", cmPart);
        model.addAttribute("mmPart", mmPart);
        return "/convert";
    }
}
