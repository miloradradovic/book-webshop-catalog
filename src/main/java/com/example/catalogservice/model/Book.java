package com.example.catalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year_released", nullable = false)
    private int yearReleased;

    @Column(name = "recap", nullable = false)
    private String recap;

    @Column(name = "in_stock", nullable = false)
    private int inStock;

    @Column(name = "price", nullable = false)
    private double price;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @JoinTable(name = "genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "writer_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "writer_id"))
    private Set<Writer> writers;

    public Book(String name, int yearReleased, String recap, int inStock, double price, Set<Genre> genres) {
        this.name = name;
        this.yearReleased = yearReleased;
        this.recap = recap;
        this.inStock = inStock;
        this.price = price;
        this.genres = genres;
    }

    public Book(int id, String name, int yearReleased, String recap, int inStock, double price, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.yearReleased = yearReleased;
        this.recap = recap;
        this.inStock = inStock;
        this.price = price;
        this.genres = genres;
    }

    public Book(String name, int yearReleased, String recap, int inStock, double price, Set<Genre> genres, Set<Writer> writers) {
        this.name = name;
        this.yearReleased = yearReleased;
        this.recap = recap;
        this.inStock = inStock;
        this.price = price;
        this.genres = genres;
        this.writers = writers;
    }
}
