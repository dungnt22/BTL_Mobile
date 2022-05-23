package com.example.musicapp.fragments;

import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.MainActivity.allOfSong;
import static com.example.musicapp.MainActivity.currentFragment;
import static com.example.musicapp.MainActivity.navigationView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongAdapter;
import com.example.musicapp.models.Song;


public class allSongFragment extends Fragment {

    RecyclerView recyclerView;
    public static SongAdapter songAdapter;

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
                nowPlaying = allOfSong;

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                isPlayingFragment isPlayingFragment = new isPlayingFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("song_item", song);

                isPlayingFragment.setArguments(bundle);

                currentFragment = 0;
                navigationView.getMenu().findItem(R.id.menu_playing).setChecked(true);

                fragmentTransaction.addToBackStack(isPlayingFragment.getClass().getName());
                fragmentTransaction.replace(R.id.content_frame, isPlayingFragment);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(songAdapter);

        return view;
    }
}

/**
 * viet ham chuyen fragment ngay trong allSongFragment
 */