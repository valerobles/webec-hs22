package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Integer> {
    long countByCategoriesContaining(Category category);
}
