package com.example.musicapp;

import android.media.MediaMetadataRetriever;

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

    public static byte[] getImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] image = retriever.getEmbeddedPicture();
        retriever.release();
        return image;
    }

    public static void sortSong(ArrayList<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if (songs.get(i).getTitle().compareToIgnoreCase(songs.get(j).getTitle()) > 0) {
                    Song tempSong = songs.get(j);
                    songs.set(j, songs.get(i));
                    songs.set(i, tempSong);
                }
            }
        }
    }
}
