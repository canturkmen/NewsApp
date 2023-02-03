package com.example.turkmen_mehmetcan;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    private int id;
    private String Title, Content, Date, imagePath, Category;
    public News(int id, String title, String content, String date, String imagePath, String category) {
        this.id = id;
        Title = title;
        Content = content;
        Date = date;
        this.imagePath = imagePath;
        Category = category;
    }

    public News(int id, String date, String title, String imagePath) {
        this.id = id;
        Title = title;
        Date = date;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
