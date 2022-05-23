package com.example.musicapp.adapters;

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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {
    private Context context;
    private ArrayList<Song> favList;
    private IFavItemClick iFavItemClick;

    public interface IFavItemClick {
        void onFavItemClick(Song song);
    }

    public FavoriteAdapter(Context context, ArrayList<Song> favList, IFavItemClick iFavItemClick) {
        this.context = context;
        this.favList = favList;
        this.iFavItemClick = iFavItemClick;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Song song = favList.get(position);

        holder.title.setText(song.getTitle());
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
                iFavItemClick.onFavItemClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_item_title);
            image = itemView.findViewById(R.id.song_item_img);
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
