package com.example.musicapp.fragments;

import static com.example.musicapp.Base.favoritePlaylist;
import static com.example.musicapp.Base.mediaPlayer;
import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.Base.nowPosition;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class isPlayingFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    private TextView titleSong;
    private TextView artist;
    private ImageView imgAlbum;
    private TextView timePlayed;
    private TextView timeTotal;
    private ImageView nextButton;
    private ImageView preButton;
    private ImageView repeatButton;
    private ImageView shuffleButton;
    private ImageView favButton;
    private SeekBar seekBar;
    private FloatingActionButton playPauseButton;

    private final Handler handler = new Handler();
    private Song song;
    private boolean repeat = false;
    private boolean shuffle = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_is_playing, container, false);
        init(view);
        getSongFromBundle();
        setView();

        mediaPlayer.setOnCompletionListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPosition);
                    timePlayed.setText(convertTime(currentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPlayOrPause();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNext();
            }
        });

        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPre();
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRepeat();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doShuffle();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFavourite();
            }
        });

        return view;
    }

    private void init(View view) {
        titleSong = view.findViewById(R.id.titleSong);
        artist = view.findViewById(R.id.artist);
        imgAlbum = view.findViewById(R.id.img_cover);
        timePlayed = view.findViewById(R.id.time_played);
        timeTotal = view.findViewById(R.id.time_total);
        seekBar = view.findViewById(R.id.seeking);
        playPauseButton = view.findViewById(R.id.button_play);
        nextButton = view.findViewById(R.id.button_next);
        preButton = view.findViewById(R.id.button_pre);
        repeatButton = view.findViewById(R.id.button_repeat);
        shuffleButton = view.findViewById(R.id.button_shuffle);
        favButton = view.findViewById(R.id.button_favorite);
    }

    private void getSongFromBundle() {
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            song = (Song) bundleReceive.get("song_item");
            nowPosition = nowPlaying.indexOf(song);
        }
    }

    private void setView() {

        titleSong.setText(song.getTitle());
        artist.setText(song.getArtist());
        timeTotal.setText(convertTime(Integer.parseInt(song.getDuration()) / 1000));
        if (song.isFavorite()) {
            favButton.setImageResource(R.drawable.ic_favorite_full);
        } else {
            favButton.setImageResource(R.drawable.ic_favorite);
        }

        byte[] image = getImage(song.getPath());
        if (image != null) {
            if (this.getContext() != null) {
                Glide.with(this.getContext()).asBitmap()
                        .load(image)
                        .into(imgAlbum);
            }
        } else {
            if (this.getContext() != null) {
                Glide.with(this.getContext())
                        .load(R.drawable.ic_music_record)
                        .into(imgAlbum);
            }
        }
        playLocalMedia();
    }

    private byte[] getImage(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] image = retriever.getEmbeddedPicture();
        retriever.release();
        return image;
    }

    private void playLocalMedia() {
        Uri uri = Uri.parse(song.getPath());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this.getContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        mediaPlayer.setOnCompletionListener(this);
    }

    private String convertTime(int milliseconds) {
        int minutes = milliseconds / 60;
        int seconds = milliseconds % 60;
        if (seconds < 10) {
            return minutes + ":" + "0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }

    private void doPlayOrPause() {
        if (mediaPlayer.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.ic_play_2);
            mediaPlayer.pause();
        } else {
            playPauseButton.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
    }

    private void doNext() {
        if (shuffle) {
            nowPosition = getRandomIndex(nowPlaying.size() - 1);
            song = nowPlaying.get(nowPosition);
            setView();
        } else if (repeat) {
            nowPosition = ((nowPosition + 1) % nowPlaying.size());
            song = nowPlaying.get(nowPosition);
            setView();
        } else if (nowPosition < (nowPlaying.size() - 1)) {
            nowPosition = nowPosition + 1;
            song = nowPlaying.get(nowPosition);
            setView();
        } else {
            Toast.makeText(this.getContext(), "Bạn đang ở cuối danh sách nhạc!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doPre() {
        if (shuffle) {
            nowPosition = getRandomIndex(nowPlaying.size() - 1);
            song = nowPlaying.get(nowPosition);
            setView();
        } else if (nowPosition > 0) {
            nowPosition = nowPosition - 1;
            song = nowPlaying.get(nowPosition);
            setView();
        } else if (nowPosition == 0){
            nowPosition = nowPlaying.size() - 1;
            song = nowPlaying.get(nowPosition);
            setView();
        }
    }

    private void doRepeat() {
        if (repeat) {
            repeat = false;
            repeatButton.setImageResource(R.drawable.ic_repeat_off);
        } else {
            repeat = true;
            repeatButton.setImageResource(R.drawable.ic_repeat_on);
        }
    }

    private void doShuffle() {
        if (shuffle) {
            shuffle = false;
            shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
        } else {
            shuffle = true;
            shuffleButton.setImageResource(R.drawable.ic_shuffle_on);
        }
    }

    private void doFavourite() {
        if (song.isFavorite()) {
            song.setFavorite(false);
            favButton.setImageResource(R.drawable.ic_favorite);
            favoritePlaylist.remove(song);
        } else {
            song.setFavorite(true);
            favButton.setImageResource(R.drawable.ic_favorite_full);
            favoritePlaylist.add(song);
        }
    }

    private int getRandomIndex(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        doNext();
    }
}