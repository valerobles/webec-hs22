package ch.fhnw.webec.wishlist.controller;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.service.CategoryService;
import ch.fhnw.webec.wishlist.service.WishlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final WishlistService wishlistService;

    public CategoryController(CategoryService categoryService, WishlistService wishlistService) {
        this.categoryService = categoryService;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/category")
    public String list(Model model) {
        var categories = categoryService.findAll();
        var wishCounts = categories.stream()
                .collect(toMap(identity(), wishlistService::countWishesByCategory));
        model.addAttribute("categories", categories);
        model.addAttribute("wishCounts", wishCounts);
        return "category-list";
    }

    @PostMapping("/category/create")
    public String create(String name) {
        categoryService.save(new Category(name));
        return "redirect:/category";
    }

    @PostMapping("/category/delete")
    public String delete(int id) {
        categoryService.findById(id).ifPresent(categoryService::delete);
        return "redirect:/category";
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    public String notFound(Model model) {
        return "404";
    }
}
