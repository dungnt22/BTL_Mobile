package com.example.musicapp.fragments;

import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.MainActivity.allOfSong;
import static com.example.musicapp.MainActivity.currentFragment;
import static com.example.musicapp.MainActivity.navigationView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.musicapp.adapters.AlbumDetailAdapter;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class AlbumDetail extends Fragment {
    private ImageView albumImage;
    private RecyclerView albumList;
    private String albumName;
    private ArrayList<Song> albumSongs;
    AlbumDetailAdapter albumDetailAdapter;

    public AlbumDetail() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_detail, container, false);
        init(view);
        getAlbumFromBundle();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)) {
            albumDetailAdapter = new AlbumDetailAdapter(this.getContext(), albumSongs, new AlbumDetailAdapter.IClick() {
                @Override
                public void onClickItem(Song song) {
                    nowPlaying = albumSongs;

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
            albumList.setAdapter(albumDetailAdapter);
            albumList.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        }
    }

    private void init(View view) {
        albumImage = view.findViewById(R.id.album_detail_img);
        albumList = view.findViewById(R.id.album_detail_list);
        albumSongs = new ArrayList<>();
    }

    private void getAlbumFromBundle() {
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            Song album = (Song) bundleReceive.get("album_item");
            albumName = album.getAlbum();
            for (int i = 0; i < allOfSong.size(); i++) {
                if (albumName.equals(allOfSong.get(i).getAlbum())) {
                    albumSongs.add(allOfSong.get(i));
                }
            }

            byte[] image = getImage(album.getPath());
            if (image != null) {
                if (this.getContext() != null) {
                    Glide.with(this.getContext()).asBitmap()
                            .load(image)
                            .into(albumImage);
                }
            } else {
                if (this.getContext() != null) {
                    Glide.with(this.getContext())
                            .load(R.drawable.ic_music_record)
                            .into(albumImage);
                }
            }
        }
    }

    private byte[] getImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] image = retriever.getEmbeddedPicture();
        retriever.release();
        return image;
    }

}