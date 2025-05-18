package com.college.yi.bookmanager.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yi_college.bookmanager.BookmanagerApplication;
import com.yi_college.bookmanager.model.Book;
import com.yi_college.bookmanager.service.BookService;

@SpringBootTest(classes = BookmanagerApplication.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private ObjectMapper mapper;
    private Book testBook;

    @BeforeEach
    public void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // テスト用のBookインスタンス（idはintなのでint型に）
        testBook = new Book();
        testBook.setId(100);
        testBook.setTitle("ユニークタイトル");
        testBook.setAuthor("テスト作者");
        testBook.setPublisher("テスト出版");
        testBook.setPublishDate(LocalDate.of(2023, 12, 25));
        testBook.setStock(7);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnListOfBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(testBook));

        mockMvc.perform(get("/api/books"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("ユニークタイトル"))
               .andExpect(jsonPath("$[0].stock").value(7));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldCreateNewBook() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("新規タイトル");
        newBook.setAuthor("著者A");
        newBook.setPublisher("出版社A");
        newBook.setPublishDate(LocalDate.of(2025, 5, 18));
        newBook.setStock(3);

        Book savedBook = new Book();
        savedBook.setId(101);
        savedBook.setTitle("新規タイトル");
        savedBook.setAuthor("著者A");
        savedBook.setPublisher("出版社A");
        savedBook.setPublishDate(LocalDate.of(2025, 5, 18));  // newBookと合わせました
        savedBook.setStock(3);

        when(bookService.createBook(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newBook)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(101))
               .andExpect(jsonPath("$.title").value("新規タイトル"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldUpdateExistingBook() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setId(100);
        updatedBook.setTitle("更新後タイトル");
        updatedBook.setAuthor("更新後作者");
        updatedBook.setPublisher("更新後出版社");
        updatedBook.setPublishDate(LocalDate.of(2021, 11, 22));
        updatedBook.setStock(12);

        when(bookService.updateBook(eq(100), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedBook)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("更新後タイトル"))
               .andExpect(jsonPath("$.stock").value(12));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldDeleteBookById() throws Exception {
        doNothing().when(bookService).deleteBook(100);

        mockMvc.perform(delete("/api/books/100"))
               .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(100);
    }
}
