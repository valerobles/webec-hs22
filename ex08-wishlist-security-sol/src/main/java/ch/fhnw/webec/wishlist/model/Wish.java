package ch.fhnw.webec.wishlist.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparing;

@Entity
public class Wish {

    @Id
    @GeneratedValue
    private Integer id;
    private String description;
    private String url;
    private int priority;
    @ManyToMany
    private Set<Category> categories = new HashSet<>();
    private LocalDate createdDate;

    protected Wish() {} // for JPA & JSON deserialization

    public Wish(String description, String url, int priority, Collection<Category> categories) {
        this.description = description;
        this.url = url.isEmpty() ? null : url;
        this.priority = priority;
        this.categories.addAll(categories);
        this.createdDate = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public List<Category> getCategoriesSorted() {
        return categories.stream()
                .sorted(comparing(Category::getName, String::compareToIgnoreCase))
                .toList();
    }
}
