package com.yi_college.bookmanager.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.yi_college.bookmanager.entity.BookEntity;

@Mapper
public interface BookRepository {

    List<BookEntity> findAll();

    @Select("SELECT * FROM books WHERE id = #{id}")
    BookEntity findById(int id);

    void insert(BookEntity book);

    void update(BookEntity book);

    void delete(int id);
}
