package me.choicore.junittest.web.dto;

import lombok.*;
import me.choicore.junittest.domain.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


public class BookDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookRequestDTO {
        @NotBlank
        @Size(min = 1, max = 50)
        private String title;
        @NotBlank
        @Size(min = 2, max = 20)
        private String author;

        public Book toEntity() {
            return Book.createBook(title, author);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookResponseDTO {

        private Long id;
        private String title;
        private String author;
        public static BookResponseDTO fromEntity(Book book) {
            return BookResponseDTO.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .build();
        }
    }
}



 /*public BookResponseDTO fromEntity(Book book) {
            this.id = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            return this;
        }*/