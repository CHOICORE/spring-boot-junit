package me.choicore.junittest.web.api;


import lombok.RequiredArgsConstructor;
import me.choicore.junittest.service.BookService;
import me.choicore.junittest.web.dto.GlobalCommonResponseCode;
import me.choicore.junittest.web.dto.GlobalCommonResponseDTO;
import me.choicore.junittest.web.dto.ListMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static me.choicore.junittest.web.dto.BookDTO.BookRequestDTO;
import static me.choicore.junittest.web.dto.BookDTO.BookResponseDTO;


@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
@RestController
public class BookApiController {


    private final BookService bookService;


    @GetMapping("/{id}")
    public ResponseEntity<GlobalCommonResponseDTO> getBook(@PathVariable Long id) {
        BookResponseDTO bookResponseDTO = bookService.findBookById(id);


        GlobalCommonResponseDTO<Object> body = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.SUCCESS.getCode())
                .message(String.format("book id is %d founded, HttpStatus : %d", id, HttpStatus.OK.value()))

                .data(bookResponseDTO)
                .build();


        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<GlobalCommonResponseDTO> getBooks() {
        List<BookResponseDTO> booksDTO = bookService.findBooks();
        HashMap<String, Object> bodyMap = new HashMap<>();
        ListMap<String, List<BookResponseDTO>> listBody = new ListMap("books", booksDTO);
        GlobalCommonResponseDTO body = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.SUCCESS.getCode())
                .message(String.format("book find all, HttpStatus: %d", HttpStatus.OK.value()))
                .data(listBody)
                .build();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<GlobalCommonResponseDTO> saveBook(@RequestBody @Valid BookRequestDTO bookRequestDTO/*, BindingResult bindingResult*/) {
        // request do
        BookResponseDTO bookDTO = bookService.saveBook(bookRequestDTO);
        // send response
        GlobalCommonResponseDTO body = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.SUCCESS.getCode())
                .message(String.format("book id %d is saved, HttpStatus : %d", bookDTO.getId(), HttpStatus.CREATED.value()))
                .data(bookDTO)
                .build();
        return new ResponseEntity(body, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<GlobalCommonResponseDTO> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        BookResponseDTO bookDTO = bookService.updateBookById(id, bookRequestDTO);
        GlobalCommonResponseDTO body = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.SUCCESS.getCode())
                .message(String.format("book id %d is updated, HttpStatus : %d", bookDTO.getId(), HttpStatus.OK.value()))
                .data(bookDTO)
                .build();
        return ResponseEntity.ok(body);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalCommonResponseDTO> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        GlobalCommonResponseDTO body = GlobalCommonResponseDTO.builder()
                .code(GlobalCommonResponseCode.SUCCESS.getCode())
                .message(String.format("book id %d is deleted, HttpStatus : %d", id, HttpStatus.NO_CONTENT.value()))
                .build();
        return ResponseEntity.ok(body);
    }
}
