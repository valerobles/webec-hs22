package ch.fhnw.webec.wishlist.controller;

import ch.fhnw.webec.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final WishlistService service;

    public HomeController(WishlistService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("wishlists", service.findAll());
        return "index";
    }
}
