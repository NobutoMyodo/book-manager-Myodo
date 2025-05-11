package com.yi_college.bookmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("books")
public class BookController {
	@GetMapping
    public String showBookPage() {
        return "index";// サービスから書籍リストを取得
	}
	
}
