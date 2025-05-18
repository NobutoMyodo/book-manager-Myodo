package com.yi_college.bookmanager.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String author; 
    private String publisher;
    private LocalDate publishDate;
    private int stock;
}
