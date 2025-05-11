package com.yi_college.bookmanager.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private int stock;
}
