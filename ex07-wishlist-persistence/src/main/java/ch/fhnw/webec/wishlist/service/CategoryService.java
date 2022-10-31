package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.data.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        return repo.findAll();
    }

    public Optional<Category> findById(int id) {
        return repo.findById(id);
    }

    public Category save(Category category) {
        return repo.save(category);
    }

    public void delete(Category category) {
       repo.delete(category);
    }
}
