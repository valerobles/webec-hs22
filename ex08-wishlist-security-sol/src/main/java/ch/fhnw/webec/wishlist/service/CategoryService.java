package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.data.CategoryRepository;
import ch.fhnw.webec.wishlist.data.WishlistRepository;
import ch.fhnw.webec.wishlist.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final WishlistRepository wishlistRepo;

    public CategoryService(CategoryRepository categoryRepo, WishlistRepository wishlistRepo) {
        this.categoryRepo = categoryRepo;
        this.wishlistRepo = wishlistRepo;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Optional<Category> findById(int id) {
        return categoryRepo.findById(id);
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    public void delete(Category category) {
        wishlistRepo.findAll().stream()
            .flatMap(l -> l.getEntries().stream())
            .forEach(w -> w.getCategories().remove(category));
        // changes in wishlists are flushed below:
        categoryRepo.delete(category);
    }
}
