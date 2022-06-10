package com.example.musicapp.fragments;

import static com.example.musicapp.Base.artists;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicapp.R;
import com.example.musicapp.adapters.ArtistAdapter;
import com.example.musicapp.models.Song;

public class ArtistFragment extends Fragment {
    RecyclerView recyclerView;
    ArtistAdapter artistAdapter;

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        recyclerView = view.findViewById(R.id.listArtist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        artistAdapter = new ArtistAdapter(this.getContext(), artists, new ArtistAdapter.ArtistItemClick() {
            @Override
            public void onClickArtistItem(Song artist) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                ArtistDetail artistDetail = new ArtistDetail();
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist_item", artist);
                artistDetail.setArguments(bundle);

                fragmentTransaction.addToBackStack(artistDetail.getClass().getName());
                fragmentTransaction.replace(R.id.content_frame, artistDetail);
                fragmentTransaction.commit();
            }
        });

        recyclerView.setAdapter(artistAdapter);

        return view;
    }
}