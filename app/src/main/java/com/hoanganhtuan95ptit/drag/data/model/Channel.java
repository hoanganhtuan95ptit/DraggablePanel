package com.hoanganhtuan95ptit.drag.data.model;

public class Channel {
    private String id;
    private int thumb;
    private int title;

    public Channel(String id, int thumb, int title) {
        this.id = id;
        this.thumb = thumb;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
