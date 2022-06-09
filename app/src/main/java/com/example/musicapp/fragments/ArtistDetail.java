package com.example.musicapp.fragments;

import static com.example.musicapp.Base.getImage;
import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.MainActivity.allOfSong;
import static com.example.musicapp.MainActivity.currentFragment;
import static com.example.musicapp.MainActivity.navigationView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.ArtistDetailAdapter;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class ArtistDetail extends Fragment {
    private ImageView imageView;
    private RecyclerView artistList;
    private ArrayList<Song> artistSongs;
    String artistName;
    ArtistDetailAdapter artistDetailAdapter;

    public ArtistDetail() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_detail, container, false);
        init(view);
        getArtistFromBundle();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(artistSongs.size() < 1)) {
            artistDetailAdapter = new ArtistDetailAdapter(this.getContext(), artistSongs, new ArtistDetailAdapter.IClick() {
                @Override
                public void onClickItem(Song song) {
                    nowPlaying = artistSongs;

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
            artistList.setAdapter(artistDetailAdapter);
            artistList.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        }
    }

    private void init(View view) {
        imageView = view.findViewById(R.id.artist_detail_img);
        artistList = view.findViewById(R.id.artist_detail_list);
        artistSongs = new ArrayList<>();
    }

    private void getArtistFromBundle() {
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null) {
            Song artist = (Song) bundleReceiver.get("artist_item");
            artistName = artist.getArtist();
            for (int i = 0; i < allOfSong.size(); i++) {
                if (artistName.equals(allOfSong.get(i).getArtist())) {
                    artistSongs.add(allOfSong.get(i));
                }
            }

            byte[] image = getImage(artist.getPath());
            if (image != null) {
                if (this.getContext() != null) {
                    Glide.with(this.getContext()).asBitmap()
                            .load(image)
                            .into(imageView);
                }
            } else {
                if (this.getContext() != null) {
                    Glide.with(this.getContext())
                            .load(R.drawable.ic_music_record)
                            .into(imageView);
                }
            }
        }
    }
}
