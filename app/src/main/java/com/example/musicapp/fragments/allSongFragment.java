package com.example.musicapp.fragments;

import static com.example.musicapp.MainActivity.allOfSong;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.SongAdapter;
import com.example.musicapp.models.Song;


public class allSongFragment extends Fragment {

    RecyclerView recyclerView;
    SongAdapter songAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_song, container, false);
        recyclerView = view.findViewById(R.id.listAllSong);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        songAdapter = new SongAdapter(this.getContext(), allOfSong, new SongAdapter.IClickSongItem() {
            @Override
            public void onClickSongItem(Song song) {
                MainActivity mMainActivity = (MainActivity) getActivity();
                mMainActivity.goToIsPlayingFragment(song);
            }
        });
        recyclerView.setAdapter(songAdapter);

        return view;
    }
}