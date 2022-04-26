package com.example.musicapp;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private Context context;
    private ArrayList<Song> songs;
    private IClickSongItem iClickSongItem;

    public interface IClickSongItem {
        void onClickSongItem(Song song);
    }

    public SongAdapter(Context context, ArrayList<Song> songs, IClickSongItem iClickSongItem) {
        this.context = context;
        this.songs = songs;
        this.iClickSongItem = iClickSongItem;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        if (song == null) {
            return;
        }

        holder.title.setText(song.getTitle());
        byte[] image = getImage(song.getPath());
        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.album);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_music_record)
                    .into(holder.album);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickSongItem.onClickSongItem(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView album;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_item_title);
            album = itemView.findViewById(R.id.song_item_img);
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
