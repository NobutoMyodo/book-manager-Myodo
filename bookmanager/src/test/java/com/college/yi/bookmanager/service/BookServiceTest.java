package com.college.yi.bookmanager.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.yi_college.bookmanager.entity.BookEntity;
import com.yi_college.bookmanager.model.Book;
import com.yi_college.bookmanager.repository.BookRepository;
import com.yi_college.bookmanager.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Test
    @DisplayName("getAllBooks(): 2件返ること & Repository呼び出し検証")
    void getAllBooks_shouldReturnList() {
        // given
        given(bookRepository.findAll()).willReturn(Arrays.asList(
                entity(1, "タイトル1", 3),
                entity(2, "タイトル2", 5)));

        // when
        List<Book> books = bookService.getAllBooks();

        // then
        assertThat(books).hasSize(2)
                         .extracting(Book::getTitle)
                         .containsExactly("タイトル1", "タイトル2");
        then(bookRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("createBook(): ID 採番されて返ること")
    void createBook_shouldAssignId() {
        // given
        Book newBook = new Book(null, "新しい本", "著者X",
                                "出版社X", LocalDate.now(), 2);

        willAnswer(inv -> {
            BookEntity arg = inv.getArgument(0);
            arg.setId(10);          // モックでID付与
            return null;
        }).given(bookRepository).insert(any(BookEntity.class));

        // when
        Book result = bookService.createBook(newBook);

        // then
        assertThat(result.getId()).isEqualTo(10);
        then(bookRepository).should(times(1)).insert(any(BookEntity.class));
    }

    @Test
    @DisplayName("updateBook(): 該当IDが存在する場合に更新される")
    void updateBook_whenExists() {
        // given
        BookEntity existing = entity(5, "古い本", 1);
        given(bookRepository.findById(5)).willReturn(existing);

        Book dto = new Book(null, "新しいタイトル", "新しい著者",
                            "新出版社", LocalDate.of(2023,5,5), 10);

        // when
        Book result = bookService.updateBook(5, dto);

        // then
        assertThat(result.getTitle()).isEqualTo("新しいタイトル");
        assertThat(existing.getTitle()).isEqualTo("新しいタイトル"); // Entityの値も更新される
        assertThat(result.getStock()).isEqualTo(10);
        then(bookRepository).should(times(1)).update(existing);
    }

    @Test
    @DisplayName("updateBook(): ID が存在しない場合に null を返す")
    void updateBook_whenNotExists() {
        given(bookRepository.findById(99)).willReturn(null);

        Book result = bookService.updateBook(99, new Book(null, "dummy", "d", "p", LocalDate.now(), 1));

        assertNull(result);
        then(bookRepository).should(never()).update(any());
    }

    @Nested
    @DisplayName("deleteBook() のテスト")
    class DeleteBookTests {

        @Test
        @DisplayName("ID が存在する場合に例外なく削除される")
        void deleteBook_whenExists() {
            // given
            given(bookRepository.findById(1)).willReturn(
                    entity(1, "削除する本", 2));

            // when / then
            assertThatCode(() -> bookService.deleteBook(1))
                    .doesNotThrowAnyException();

            then(bookRepository).should(times(1)).delete(1);
        }

        @Test
        @DisplayName("ID が存在しない場合に ResponseStatusException が投げられる")
        void deleteBook_whenNotExists() {
            // given
            given(bookRepository.findById(1)).willReturn(null);

            // when / then
            assertThatThrownBy(() -> bookService.deleteBook(1))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("Book not found");

            then(bookRepository).should(never()).delete(anyInt());
        }
    }

    // テスト用のBookEntity生成メソッド
    private BookEntity entity(int id, String title, int stock) {
        BookEntity e = new BookEntity();
        e.setId(id);
        e.setTitle(title);
        e.setAuthor("著者");
        e.setPublisher("出版社");
        e.setPublishedDate(LocalDate.now());
        e.setStock(stock);
        return e;
    }
}
