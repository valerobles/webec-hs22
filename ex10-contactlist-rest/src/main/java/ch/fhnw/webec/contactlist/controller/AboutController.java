package ch.fhnw.webec.contactlist.controller;

import ch.fhnw.webec.contactlist.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    private final ContactService service;

    public AboutController(ContactService service) {
        this.service = service;
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("contactCount", service.getContactList(null).size());
        model.addAttribute("phoneNumberCount", service.phoneNumberCount());
        model.addAttribute("emailCount", service.emailCount());
        return "about";
    }
}
