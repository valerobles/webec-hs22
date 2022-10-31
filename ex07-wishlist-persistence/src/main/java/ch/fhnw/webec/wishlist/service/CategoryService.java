package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.unmodifiableList;

@Service
public class CategoryService {

    private final List<Category> categories = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    private final WishlistService wishlistService;

    public CategoryService(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    public List<Category> findAll() {
        return unmodifiableList(categories);
    }

    public Optional<Category> findById(int id) {
        return categories.stream().filter(c -> c.getId() == id).findFirst();
    }

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(nextId.getAndIncrement());
            categories.add(category);
        }
        return category;
    }

    public void delete(Category category) {
        wishlistService.findAll().stream()
                .flatMap(l -> l.getEntries().stream())
                .forEach(w -> w.getCategories().remove(category));
        categories.remove(category);
    }
}
