package me.choicore.junittest.service;


import lombok.RequiredArgsConstructor;
import me.choicore.junittest.domain.Book;
import me.choicore.junittest.repository.BookRepository;
import me.choicore.junittest.util.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static me.choicore.junittest.web.dto.BookDTO.BookRequestDTO;
import static me.choicore.junittest.web.dto.BookDTO.BookResponseDTO;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final MailSender mailSender;

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public BookResponseDTO saveBook(BookRequestDTO bookRequestDTO) {

        Book book = bookRepository.save(bookRequestDTO.toEntity());

        if (book != null) {
            if (!mailSender.send()) {
                throw new IllegalArgumentException("메일 전송 실패");
            }
        }

        return BookResponseDTO.fromEntity(book);
    }

    public List<BookResponseDTO> findBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponseDTO::fromEntity)
                .collect(toList());
    }

    public BookResponseDTO findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        return BookResponseDTO.fromEntity(book);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }


    @Transactional(rollbackFor = IllegalArgumentException.class)
    public BookResponseDTO updateBookById(Long id, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        book.updateTitleAndAuthor(bookRequestDTO.getTitle(), bookRequestDTO.getAuthor());

        return BookResponseDTO.fromEntity(book);
    }


}
