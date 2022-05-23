package com.example.musicapp.fragments;

import static com.example.musicapp.Base.favoritePlaylist;
import static com.example.musicapp.Base.nowPlaying;
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
import com.example.musicapp.adapters.FavoriteAdapter;
import com.example.musicapp.models.Song;


public class FavouriteFragment extends Fragment {
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.favorite_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        favoriteAdapter = new FavoriteAdapter(this.getContext(), favoritePlaylist, new FavoriteAdapter.IFavItemClick() {
            @Override
            public void onFavItemClick(Song song) {
                nowPlaying = favoritePlaylist;

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
        recyclerView.setAdapter(favoriteAdapter);
        return view;
    }
}