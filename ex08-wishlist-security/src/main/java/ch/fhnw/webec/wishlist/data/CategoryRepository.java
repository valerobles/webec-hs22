package ch.fhnw.webec.wishlist.data;

import ch.fhnw.webec.wishlist.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
