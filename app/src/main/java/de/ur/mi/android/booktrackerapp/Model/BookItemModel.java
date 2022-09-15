package de.ur.mi.android.booktrackerapp.Model;

public class BookItemModel {
    private String title;
    private String author;
    private String cover;
    private double rating;
    private int numPages;
    private String language;
    private String status;
    private int currPage;
    private int progress;


    public BookItemModel(String title, String author, String cover,
                         double rating, int numPages, String language) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.numPages = numPages;
        this.rating = rating;
        this.language = language;
    }

    public BookItemModel(String title, String author, String cover, double rating,
                         int numPages, String language, String status, int currPage, int progress) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.numPages = numPages;
        this.rating = rating;
        this.language = language;
        this.status = status;
        this.currPage = currPage;
        this.progress = progress;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authors) {
        this.author = authors;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
