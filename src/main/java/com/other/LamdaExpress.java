package com.other;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LamdaExpress {
    @Test
    public void collect(){
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("The Fellowship of the Ring", "0395489318"));
        bookList.add(new Book("The Two Towers",  "0345339711"));
        bookList.add(new Book("The Return of the King",  "0618129111"));
        bookList.add(new Book("The Return of the King",  "0618129111"));

        //Map<String,String> map = bookList.stream().collect(Collectors.toMap(Book::getIsbn, Book::getName));
        //第三个参数传merge函数过去，如果有重复的key，就按照这个函数处理。
        Map<String,Book> bookMap = bookList.stream().collect(Collectors.toMap(Book::getIsbn, Book->Book,(k1,k2)->k1));
        System.out.println(JSONObject.toJSONString(bookMap));

    }

    class Book {
        private String name;
        private String isbn;

        public Book(String name, String isbn) {
            this.name = name;
            this.isbn = isbn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

    }
}
