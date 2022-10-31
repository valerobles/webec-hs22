package ch.fhnw.webec.wishlist;

import ch.fhnw.webec.wishlist.model.Category;
import ch.fhnw.webec.wishlist.model.Wishlist;
import ch.fhnw.webec.wishlist.service.CategoryService;
import ch.fhnw.webec.wishlist.service.WishlistService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Component
@ConditionalOnProperty("wishlist.add-sample-data")
public class SampleDataAdder implements CommandLineRunner {

    public static final String WISHLISTS_FILE = "wishlists.json";
    public static final String CATEGORIES_FILE = "categories.json";

    private static final Logger logger = LoggerFactory.getLogger(SampleDataAdder.class);

    private final ObjectMapper mapper;
    private final WishlistService wishlistService;
    private final CategoryService categoryService;

    public SampleDataAdder(ObjectMapper mapper,
                           WishlistService wishlistService,
                           CategoryService categoryService) {
        this.mapper = mapper;
        this.wishlistService = wishlistService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws IOException {
        addSampleData();
    }

    public void addSampleData() throws IOException {
        if (wishlistService.findAll().isEmpty() && categoryService.findAll().isEmpty()) {
            logger.info("Adding sample data");
            var wishlists = mapper.readValue(SampleDataAdder.class.getResource(WISHLISTS_FILE),
                    new TypeReference<List<Wishlist>>() {});
            var categories = mapper.readValue(SampleDataAdder.class.getResource(CATEGORIES_FILE),
                    new TypeReference<List<Category>>() {});

            // "random" assignments of categories
            int catCount = categories.size();
            if (catCount > 0) {
                var random = new Random(42);
                for (var list : wishlists) {
                    for (var wish : list.getEntries()) {
                        for (int i = 0; i < random.nextInt(1, 3); i++) {
                            var category = categories.get(random.nextInt(catCount));
                            wish.getCategories().add(category);
                        }
                    }
                }
            }

            wishlists.forEach(wishlistService::save);
            categories.forEach(categoryService::save);
        }
    }
}
