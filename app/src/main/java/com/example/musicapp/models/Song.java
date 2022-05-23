package com.example.musicapp.models;

import java.io.Serializable;

public class Song implements Serializable {
    private String path;
    private String title;
    private String artist;
    private String duration;
    private String album;
    private boolean favorite;

    public Song() {

    }

    public Song(String album, String title, String duration, String path, String artist) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        favorite = false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
