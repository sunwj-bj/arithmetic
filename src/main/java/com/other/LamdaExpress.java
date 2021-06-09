package com.other;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LamdaExpress {
    @Test
    public void collect() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("The Fellowship of the Ring", "0395489318", 100));
        bookList.add(new Book("The Two Towers", "0345339711", 50));
        bookList.add(new Book("The Return of the King", "0618129171", 10));
        bookList.add(new Book("The Return of the King", "0618129111", 300));

        //Map<String,String> map = bookList.stream().collect(Collectors.toMap(Book::getIsbn, Book::getName));
        //转MAP,第三个参数传merge函数过去，如果有重复的key，就按照这个函数处理。
        Map<String, Book> bookMap = bookList.stream().filter(t -> t.price > 50 && t.getIsbn().equals("0618129111")).collect(Collectors.toMap(Book::getIsbn, Book -> Book, (k1, k2) -> k1));
        //转List
        List<String> collect = bookList.stream().map(Book::getIsbn).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(bookMap));
        System.out.println(JSONObject.toJSONString(collect));

    }

    class Book {
        private String name;
        private String isbn;
        private Integer price;

        public Book(String name, String isbn, Integer price) {
            this.name = name;
            this.isbn = isbn;
            this.price = price;
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

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }
    }
}
