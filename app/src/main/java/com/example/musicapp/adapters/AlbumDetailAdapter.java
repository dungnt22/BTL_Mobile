package com.example.musicapp.adapters;

import static com.example.musicapp.Base.getImage;

import android.content.Context;
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

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ItemHolder> {
    private Context context;
    private ArrayList<Song> songs;
    private IClick iClick;

    public interface IClick {
        void onClickItem(Song song);
    }

    public AlbumDetailAdapter(Context context, ArrayList<Song> songs, IClick iClick) {
        this.context = context;
        this.songs = songs;
        this.iClick = iClick;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new AlbumDetailAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Song song = songs.get(position);
        holder.name.setText(song.getTitle());

        byte[] image = getImage(song.getPath());
        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.image);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_music_record)
                    .into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClick.onClickItem(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.song_item_img);
            name = itemView.findViewById(R.id.song_item_title);
        }
    }
}
