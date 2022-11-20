package me.choicore.junittest.service;

import me.choicore.junittest.domain.Book;
import me.choicore.junittest.repository.BookRepository;
import me.choicore.junittest.util.MailSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static me.choicore.junittest.web.dto.BookDTO.BookRequestDTO;
import static me.choicore.junittest.web.dto.BookDTO.BookResponseDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Import({BookService.class, BookRepository.class, MailSender.class})
//@SpringBootTest
class BookServiceTest {


    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @DisplayName("책 등록")
    void saveBook() {
        // given
        BookRequestDTO bookRequestDTO = BookRequestDTO.builder()
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build();
        // stub
        Book book = bookRequestDTO.toEntity();
        when(bookRepository.save(book)).thenReturn(book);
        when(mailSender.send()).thenReturn(true);

        // when
        BookResponseDTO bookResponseDTO = bookService.saveBook(bookRequestDTO);

        // verify
        assertThat(bookResponseDTO.getTitle()).isEqualTo(bookRequestDTO.getTitle());
        assertThat(bookResponseDTO.getAuthor()).isEqualTo(bookRequestDTO.getAuthor());
    }


    @Test
    @DisplayName("책 목록 전체 조회")
    void findBooks() {
        // given

        // stub
        List<Book> books = new ArrayList<>();
        books.add(Book.builder()
                .id(1L)
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build());
        books.add(Book.builder()
                .id(2L)
                .title("스프링 부트와 AWS로 혼자 구현하는 웹 서비스")
                .author("이동욱")
                .build());
        when(bookRepository.findAll()).thenReturn(books);
        List<BookResponseDTO> comparedBooks = books.stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());

        // when
        List<BookResponseDTO> findBooks = bookService.findBooks();

        // verify
        assertThat(findBooks.size()).isEqualTo(books.size());
        IntStream.range(0, findBooks.size()).forEach(i -> {
            BookResponseDTO findBook = findBooks.get(i);
            BookResponseDTO comparedBook = comparedBooks.get(i);
            assertThat(findBook).isEqualTo(comparedBook);
        });
    }

    @Test
    @DisplayName("책 목록 ID로 조회")
    void findBook() {
        // given
        Long id = 1L;

        // stub
        Book book = Book.builder()
                .id(id)
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        BookResponseDTO comparedBook = BookResponseDTO.fromEntity(book);

        // when
        BookResponseDTO findBook = bookService.findBookById(id);

        // verify
        assertThat(findBook).isEqualTo(comparedBook);
    }


    @Test
    @DisplayName("책 수정")
    void updateBook() {
        // given
        Long id = 1L;
        BookRequestDTO bookRequestDTO = BookRequestDTO.builder()
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build();

        // stub
        Book comparedBook = Book.builder()
                .id(id)
                .title("Spring Boot")
                .author("KYH")
                .build();
        when(bookRepository.findById(id)).thenReturn(Optional.of(comparedBook));

        // when
        BookResponseDTO findBook = bookService.updateBookById(id, bookRequestDTO);


        // print

        // verify
        assertThat(findBook.getId()).isEqualTo(comparedBook.getId());
        assertThat(findBook.getTitle()).isEqualTo(comparedBook.getTitle());
        assertThat(findBook.getAuthor()).isEqualTo(comparedBook.getAuthor());

    }


}