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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {
    private Context context;
    private ArrayList<Song> artists;
    private ArtistAdapter.ArtistItemClick artistItemClick;

    public interface ArtistItemClick {
        void onClickArtistItem(Song artist);
    }

    public ArtistAdapter(Context context, ArrayList<Song> artists, ArtistItemClick artistItemClick) {
        this.context = context;
        this.artists = artists;
        this.artistItemClick = artistItemClick;
    }

    @NonNull
    @Override
    public ArtistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        return new ArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistHolder holder, int position) {
        Song song = artists.get(position);
        holder.textView.setText(song.getArtist());
        byte[] image = getImage(song.getPath());
        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_music_record)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistItemClick.onClickArtistItem(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ArtistHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_item_image);
            textView = itemView.findViewById(R.id.album_item_name);
        }
    }
}
