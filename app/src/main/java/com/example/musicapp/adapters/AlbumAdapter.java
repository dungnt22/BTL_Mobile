package com.example.musicapp.adapters;

import static com.example.musicapp.Base.getImage;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context mContext;
    private ArrayList<Song> albums;
    private AlbumAdapter.IClickAlbumItem iClickAlbumItem;

    public interface IClickAlbumItem {
        void onClickAlbumItem(Song album);
    }

    public AlbumAdapter(Context context, ArrayList<Song> albums, IClickAlbumItem iClickAlbumItem) {
        this.mContext = context;
        this.albums = albums;
        this.iClickAlbumItem = iClickAlbumItem;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Song song = albums.get(position);
        holder.albumName.setText(song.getAlbum());
        byte[] image = getImage(song.getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.albumImg);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.ic_music_record)
                    .into(holder.albumImg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickAlbumItem.onClickAlbumItem(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumImg;
        private TextView albumName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImg = itemView.findViewById(R.id.album_item_image);
            albumName = itemView.findViewById(R.id.album_item_name);
        }
    }
}
