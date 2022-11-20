package me.choicore.junittest.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import me.choicore.junittest.service.BookService;
import me.choicore.junittest.web.dto.BookDTO;
import me.choicore.junittest.web.dto.GlobalCommonResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private BookService bookService;

    private static ObjectMapper objectMapper;

    private static HttpHeaders httpHeaders;

    private static final String BASE_URL = "/api/v1/book";

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
    }

    /* Mock - 가상 데이터로 테스트 */
    @Test
    @DisplayName("책 ID로 조회")
    void getBook() throws JsonProcessingException {
        // given
        Long id = 1L;
        given(bookService.findBookById(id)).willReturn(BookDTO.BookResponseDTO.builder()
                .id(id)
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build());

        // when
        HttpEntity<String> requestBody = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, requestBody, String.class);

        // verify
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int code = documentContext.read("$.code");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(code).isEqualTo(GlobalCommonResponseCode.SUCCESS.getCode());

    }


    @Test
    @DisplayName("책 목록 전체 조회")
    void getBooks() {

        // given

        // when
        HttpEntity<String> requestBody = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL, HttpMethod.GET, requestBody, String.class);

        // verify
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int code = documentContext.read("$.code");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(code).isEqualTo(GlobalCommonResponseCode.SUCCESS.getCode());

    }


    @Test
    @DisplayName("책 등록")
    void saveBook() throws JsonProcessingException {
        //given
        BookDTO.BookRequestDTO bookRequestDTO = BookDTO.BookRequestDTO.builder()
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build();

        given(bookService.saveBook(bookRequestDTO)).willReturn(BookDTO.BookResponseDTO.builder()
                .id(1L)
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build());


        String jsonBody = objectMapper.writeValueAsString(bookRequestDTO);

        HttpEntity<String> requestBody = new HttpEntity<>(jsonBody, httpHeaders);
        // when
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL, HttpMethod.POST, requestBody, String.class);

        // print

        // verify
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int code = documentContext.read("$.code");
        String title = documentContext.read("$.data.title");
        String author = documentContext.read("$.data.author");

        assertThat(code).isEqualTo(GlobalCommonResponseCode.SUCCESS.getCode());
        assertThat(title).isEqualTo(bookRequestDTO.getTitle());
        assertThat(author).isEqualTo(bookRequestDTO.getAuthor());
    }

    @Test
    @DisplayName("책 수정")
    void updateBook() throws JsonProcessingException {

        //given
        Long id = 1L;
        BookDTO.BookRequestDTO bookRequestDTO = BookDTO.BookRequestDTO.builder()
                .title("자바 ORM 표준 JPA 프로그래밍")
                .author("김영한")
                .build();
        given(bookService.updateBookById(id, bookRequestDTO)).willReturn(BookDTO.BookResponseDTO.builder()
                .id(id)
                .title(bookRequestDTO.getTitle())
                .author(bookRequestDTO.getAuthor())
                .build());

        String jsonBody = objectMapper.writeValueAsString(bookRequestDTO);
        HttpEntity<String> requestBody = new HttpEntity<>(jsonBody, httpHeaders);

        // when
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/" + id, HttpMethod.PUT, requestBody, String.class);

        // print


        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());


        int code = documentContext.read("$.code");
        String title = documentContext.read("$.data.title");
        String author = documentContext.read("$.data.author");

        assertThat(code).isEqualTo(GlobalCommonResponseCode.SUCCESS.getCode());
        assertThat(title).isEqualTo(bookRequestDTO.getTitle());
        assertThat(author).isEqualTo(bookRequestDTO.getAuthor());
    }

    @Test
    @DisplayName("책 삭제")
    void deleteBook() {
        //given
        Long id = 1L;

        // when
        HttpEntity<String> requestBody = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, requestBody, String.class);

        // print

        // verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int code = documentContext.read("$.code");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(code).isEqualTo(GlobalCommonResponseCode.SUCCESS.getCode());

    }
}