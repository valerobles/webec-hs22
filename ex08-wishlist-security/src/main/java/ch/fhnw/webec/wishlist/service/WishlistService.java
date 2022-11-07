package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.data.WishlistRepository;
import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wishlist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepo;
    private final WishRepository wishRepo;

    public WishlistService(WishlistRepository wishlistRepo, WishRepository wishRepo) {
        this.wishlistRepo = wishlistRepo;
        this.wishRepo = wishRepo;
    }

    public List<Wishlist> findAll() {
        return wishlistRepo.findAll();
    }

    public Optional<Wishlist> findById(int id) {
        return wishlistRepo.findById(id);
    }

    public long countWishesByCategory(Category category) {
        return wishRepo.countByCategoriesContaining(category);
    }

    public Wishlist save(Wishlist wishlist) {
        return wishlistRepo.save(wishlist);
    }

    public void delete(Wishlist wishlist) {
        wishlistRepo.delete(wishlist);
    }
}
