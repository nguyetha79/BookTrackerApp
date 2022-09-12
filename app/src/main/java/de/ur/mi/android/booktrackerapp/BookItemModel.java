package de.ur.mi.android.booktrackerapp;

import java.util.ArrayList;

public class BookItemModel {
    private String cover;
    private String title;
    private ArrayList<String> authors;
    private int numPages;
    private float rating;
    private String language;

    public BookItemModel(String cover, String title, ArrayList<String> authors,
                         int numPages, float rating, String language) {
        this.cover = cover;
        this.title = title;
        this.authors = authors;
        this.numPages = numPages;
        this.rating = rating;
        this.language = language;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
