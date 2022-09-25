package ru.masloboev.firsthomework.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Book {
    @NotEmpty
    private int id;
    @Size(min = 2, max = 200, message = "Name should be between 2 and 200 characters")
    private String name;
    @Size(min = 2, max = 100, message = "Name of author should be between 2 and 200 characters")
    private String author;
    @NotEmpty
    private int year;

    public Book(){

    }

    public Book(int id, String name, String author, int year) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
