package ch.fhnw.webec.wishlist.service;

import ch.fhnw.webec.wishlist.data.CategoryRepository;
import ch.fhnw.webec.wishlist.data.WishRepository;
import ch.fhnw.webec.wishlist.data.WishlistRepository;
import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wish;
import ch.fhnw.webec.wishlist.model.Wishlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class WishlistServiceIT {

    final CategoryRepository categoryRepo;
    final WishlistService service;

    @Autowired
    public WishlistServiceIT(CategoryRepository categoryRepo, WishlistRepository wishlistRepo, WishRepository wishRepo) {
        this.categoryRepo = categoryRepo;
        this.service = new WishlistService(wishlistRepo, wishRepo);
    }

    @Test
    void testSaveFindById() {
        var wishlist = new Wishlist("My list");
        var id = service.save(wishlist).getId();
        assertNotNull(id);

        var retrieved = service.findById(id).orElseThrow(AssertionError::new);
        assertEquals(id, retrieved.getId());
        assertEquals("My list", retrieved.getName());
        assertEquals(LocalDate.now(), retrieved.getCreatedDate());
    }

    @Test
    void testSaveWithWishes() {
        var wishlist = new Wishlist("My list");
        wishlist.getEntries().add(new Wish("stuff", "https://example.com", 2, emptyList()));
        wishlist.getEntries().add(new Wish("more", "https://fhnw.ch", 1, emptyList()));
        var id = service.save(wishlist).getId();

        var retrieved = service.findById(id).orElseThrow(AssertionError::new);
        assertEquals(2, retrieved.getEntries().size());
        assertEquals("stuff", retrieved.getEntries().get(0).getDescription());
        assertEquals("https://example.com", retrieved.getEntries().get(0).getUrl());
        assertEquals(2, retrieved.getEntries().get(0).getPriority());
        assertEquals("more", retrieved.getEntries().get(1).getDescription());
        assertEquals("https://fhnw.ch", retrieved.getEntries().get(1).getUrl());
        assertEquals(1, retrieved.getEntries().get(1).getPriority());
    }

    @Test
    void testCountWishesByCategory() {
        var food = categoryRepo.save(new Category("food"));
        var electronics = categoryRepo.save(new Category("electronics"));
        var expensive = categoryRepo.save(new Category("expensive"));
        var cheap = categoryRepo.save(new Category("cheap"));

        var list1 = new Wishlist("List 1");
        list1.getEntries().add(new Wish("Chocolate", "", 2, List.of(food)));
        list1.getEntries().add(new Wish("Tablet", "", 1, List.of(electronics, expensive)));
        service.save(list1);

        var list2 = new Wishlist("List 2");
        list2.getEntries().add(new Wish("anything", "", 0, emptyList()));
        list2.getEntries().add(new Wish("Pool", "", 0, List.of(expensive)));
        service.save(list2);

        assertEquals(1, service.countWishesByCategory(food));
        assertEquals(1, service.countWishesByCategory(electronics));
        assertEquals(2, service.countWishesByCategory(expensive));
        assertEquals(0, service.countWishesByCategory(cheap));
    }
}
