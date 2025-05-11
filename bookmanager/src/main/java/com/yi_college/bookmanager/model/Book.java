package com.yi_college.bookmanager.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String author; //著者
    private String publisher; //出版社
    private LocalDate publishDate; //出版日
    private int stock; //在庫
}
