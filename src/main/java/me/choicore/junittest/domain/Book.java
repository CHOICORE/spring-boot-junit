package me.choicore.junittest.domain;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;


@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 20, nullable = false)
    private String author;


    @Builder
    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public static Book createBook(String title, String author) {
        Book book = new Book();
        book.title = title;
        book.author = author;
        return book;
    }

    public Book copy() {
        Book copyEntity = new Book();
        copyEntity.id = this.id;
        copyEntity.title = this.title;
        copyEntity.author = this.author;
        return copyEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author);
    }

    public void updateTitleAndAuthor(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
