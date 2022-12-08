package ch.fhnw.webec.wishlist.controller;

import ch.fhnw.webec.wishlist.model.Wish;
import ch.fhnw.webec.wishlist.model.Wishlist;
import ch.fhnw.webec.wishlist.service.CategoryService;
import ch.fhnw.webec.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class WishlistController {

    private final WishlistService wishlistService;
    private final CategoryService categoryService;

    public WishlistController(WishlistService wishlistService,
                              CategoryService categoryService) {
        this.wishlistService = wishlistService;
        this.categoryService = categoryService;
    }

    @GetMapping("/wishlist/{id}")
    public String show(@PathVariable int id, Model model) {
        model.addAttribute("wishlist", wishlistService.findById(id).orElseThrow());
        model.addAttribute("categories", categoryService.findAll());
        return "wishlist";
    }

    @PostMapping("/wishlist/create")
    public String create(String name) {
        wishlistService.save(new Wishlist(name));
        return "redirect:/";
    }

    @PostMapping("/wishlist/{id}/add-wish")
    public String addWish(@PathVariable int id, String name,
                          String url, int priority,
                          @RequestParam(required = false) List<Integer> categoryIds) {
        var wishlist = wishlistService.findById(id).orElseThrow();
        if (categoryIds == null) {
            categoryIds = emptyList();
        }
        var categories = categoryIds.stream()
                .map(categoryService::findById)
                .map(Optional::orElseThrow)
                .toList();
        wishlist.getEntries().add(new Wish(name, url, priority, categories));
        wishlistService.save(wishlist);
        return "redirect:/wishlist/" + id;
    }

    @PostMapping("/wishlist/{id}/delete-wish")
    public String deleteWish(@PathVariable int id, int wishId) {
        var wishlist = wishlistService.findById(id).orElseThrow();
        wishlist.getEntries().stream()
                .filter(w -> w.getId() == wishId)
                .findFirst()
                .ifPresent(wishlist.getEntries()::remove);
        wishlistService.save(wishlist);
        return "redirect:/wishlist/" + id;
    }

    @PostMapping("/wishlist/delete")
    public String delete(int id) {
        wishlistService.findById(id).ifPresent(wishlistService::delete);
        return "redirect:/";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    public String notFound(Model model) {
        return "404";
    }
}
