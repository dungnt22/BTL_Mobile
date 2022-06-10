package com.example.musicapp.fragments;

import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.fragments.isPlayingFragment.playlistFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongAdapter;
import com.example.musicapp.models.Song;

public class PlaylistFragment extends Fragment {
    private ImageView playlistButton;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistButton = view.findViewById(R.id.close_playlist);
        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(playlistFragment).commit();
            }
        });

        recyclerView = view.findViewById(R.id.playlist_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        songAdapter = new SongAdapter(this.getContext(), nowPlaying, new SongAdapter.IClickSongItem() {
            @Override
            public void onClickSongItem(Song song) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("song_item", song);
                isPlayingFragment isPlayingFragment = new isPlayingFragment();
                isPlayingFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, isPlayingFragment).commit();
            }
        });

        recyclerView.setAdapter(songAdapter);
        return view;
    }
}