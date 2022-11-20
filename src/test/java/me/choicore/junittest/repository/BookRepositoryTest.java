package me.choicore.junittest.repository;

import me.choicore.junittest.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;


    @BeforeEach
    void setUp() {
        System.out.println("############### call -> @BeforeEach ###############");
        Book book = Book.createBook("자바 ORM 표준 JPA 프로그래밍", "김영한");
        Book savedBook = bookRepository.save(book);

        System.out.println("savedBook.id() = " + savedBook.getId());
    }

    @Test
    @DisplayName("책 등록")
    void saveBook() {
        // given
        Book book = Book.createBook("스프링 부트와 AWS로 혼자 구현하는 웹 서비스", "이동욱");
        // when
        Book savedBook = bookRepository.save(book);
        // verify
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("책 목록 조회")
    void findBooks() {
        // given
        // when
        List<Book> books = bookRepository.findAll();
        // verify
        assertThat(books.size()).isEqualTo(1);
    }

    @Sql("classpath:/db/book_Init.sql")
    @Test
    @DisplayName("책 번호로 조회")
    void findBookById() {
        // given
        Long id = 1L;
        String title = "자바 ORM 표준 JPA 프로그래밍";
        String author = "김영한";
        // when
        Book book = bookRepository.findById(1L).get();
        // verify
        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getAuthor()).isEqualTo(author);
    }

    @Sql("classpath:/db/book_Init.sql")
    @Test
    @DisplayName("책 정보 수정")
    void updateBook() {
        // given
        Long id = 1L;
        String title = "이것이 자바다";
        String author = "신용권";
        Book book = bookRepository.findById(1L).get();
        Book oldBook = book.copy();

        // when
        book.updateTitleAndAuthor(title, author);
        bookRepository.flush();

        // verify
        assertThat(book.getId()).isEqualTo(oldBook.getId());
        assertThat(book.getTitle()).isNotEqualTo(oldBook.getTitle());
        assertThat(book.getAuthor()).isNotEqualTo(oldBook.getAuthor());
        assertThat(book.getId()).isEqualTo(id);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getAuthor()).isEqualTo(author);
    }

    @Sql("classpath:/db/book_Init.sql")
    @Test
    @DisplayName("책 번호로 삭제")
    void deleteBookById() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);
        // verify
        assertThat(bookRepository.findById(id).isPresent()).isFalse();
    }
}