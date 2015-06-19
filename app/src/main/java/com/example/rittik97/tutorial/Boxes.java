package com.example.rittik97.tutorial;

/**
 * Created by rittik97 on 5/13/2015.
 */
public class Boxes {
    private String title;
    private String link;
    private String imagelink;

    public Boxes(String title, String link, String imagelink) {
        this.title = title;
        this.link = link;
        this.imagelink = imagelink;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImagelink() {
        return imagelink;
    }
}
