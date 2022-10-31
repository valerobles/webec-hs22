package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wishlist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.unmodifiableList;

@Service
public class WishlistService {

    private final List<Wishlist> wishlists = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    public List<Wishlist> findAll() {
        return unmodifiableList(wishlists);
    }

    public Optional<Wishlist> findById(int id) {
        return wishlists.stream().filter(l -> l.getId() == id).findFirst();
    }

    public long countWishesByCategory(Category category) {
        return wishlists.stream()
                .flatMap(l -> l.getEntries().stream())
                .filter(w -> w.getCategories().contains(category))
                .count();
    }

    public Wishlist save(Wishlist wishlist) {
        if (wishlist.getId() == null) {
            wishlist.setId(nextId.getAndIncrement());
            wishlists.add(wishlist);
        }
        for (var wish : wishlist.getEntries()) {
            if (wish.getId() == null) {
                wish.setId(nextId.getAndIncrement());
            }
        }
        return wishlist;
    }

    public void delete(Wishlist wishlist) {
        wishlists.remove(wishlist);
    }
}
