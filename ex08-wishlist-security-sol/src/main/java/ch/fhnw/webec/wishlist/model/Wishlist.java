package ch.fhnw.webec.wishlist.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static javax.persistence.CascadeType.ALL;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private LocalDate createdDate;
    @OneToMany(cascade = ALL)
    @JoinColumn(name = "wishlist")
    private List<Wish> entries = new ArrayList<>();

    protected Wishlist() {} // for JPA & JSON deserialization

    public Wishlist(String name) {
        this.name = name;
        this.createdDate = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public List<Wish> getEntries() {
        return entries;
    }

    public List<Wish> getEntriesByPriority() {
        return entries.stream()
                .sorted(comparing(Wish::getPriority).reversed())
                .toList();
    }
}
