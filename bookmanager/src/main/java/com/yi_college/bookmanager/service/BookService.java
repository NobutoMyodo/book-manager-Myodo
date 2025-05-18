package com.yi_college.bookmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.yi_college.bookmanager.entity.BookEntity;
import com.yi_college.bookmanager.model.Book;
import com.yi_college.bookmanager.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        List<BookEntity> entities = bookRepository.findAll();
        return entities.stream()
                .map(entity -> new Book(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getAuthor(),
                        entity.getPublisher(),
                        entity.getPublishedDate(),
                        entity.getStock()
                ))
                .collect(Collectors.toList());
    }

    public Book createBook(Book book) {
        BookEntity entity = new BookEntity();
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPublisher(book.getPublisher());
        entity.setPublishedDate(book.getPublishDate());
        entity.setStock(book.getStock());
        
        bookRepository.insert(entity);
        book.setId(entity.getId());
        return book;
    }
//レビュー修正
    public Book updateBook(int id, Book book) {
        BookEntity existing = bookRepository.findById(id);
        if (existing == null) {
            return null;
        }

        // 既存エンティティの値を更新する
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setPublisher(book.getPublisher());
        existing.setPublishedDate(book.getPublishDate());
        existing.setStock(book.getStock());

        bookRepository.update(existing);

        // Book DTOを返す場合は引数のbookのままでOK
        return book;
    }

    public void deleteBook(int id) {
        // IDが存在しない場合は404を返す
        BookEntity existing = bookRepository.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }

        bookRepository.delete(id);
    }
}
