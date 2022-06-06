package com.example.musicapp;

import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class Base {
    public static int nowPosition = 0;
    public static ArrayList<Song> nowPlaying = new ArrayList<>();
    public static ArrayList<Song> favoritePlaylist = new ArrayList<>();
    public static ArrayList<Song> albums = new ArrayList<>();
    public static ArrayList<Song> artists = new ArrayList<>();
    public static boolean shuffle = false;
    public static boolean repeat = false;
}
