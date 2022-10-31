package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wishlist;
import ch.fhnw.webec.wishlist.data.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    WishlistRepository repo;

    public WishlistService(WishlistRepository repo){
        this.repo = repo;
    }

    public List<Wishlist> findAll() {
        return repo.findAll();
    }

    public Optional<Wishlist> findById(int id) {
        return repo.findById(id);
    }

    public long countWishesByCategory(Category category) {
        return repo.findAll().stream()
                .flatMap(l -> l.getEntries().stream())
                .filter(w -> w.getCategories().contains(category))
                .count();
    }

    public Wishlist save(Wishlist wishlist) {
        return repo.save(wishlist);
    }

    public void delete(Wishlist wishlist){
        repo.delete(wishlist);
    }
}
