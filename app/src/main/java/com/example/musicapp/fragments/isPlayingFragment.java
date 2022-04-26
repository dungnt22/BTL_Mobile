package com.example.musicapp.fragments;

import static com.example.musicapp.MainActivity.allOfSong;

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

public class isPlayingFragment extends Fragment {

    private TextView titleSong;
    private TextView artist;
    private ImageView imgAlbum;
    private TextView timePlayed;
    private TextView timeTotal;
    private ImageView nextButton;
    private ImageView preButton;
    private ImageView repeatButton;
    private ImageView shuffleButton;
    private SeekBar seekBar;
    private FloatingActionButton playPauseButton;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Song song;
    private int nowPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_is_playing, container, false);
        init(view);
        getSongFromBundle();
        setView();

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
                if (mediaPlayer.isPlaying()) {
                    playPauseButton.setImageResource(R.drawable.ic_play_2);
                    mediaPlayer.pause();
                } else {
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
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
    }

    private void getSongFromBundle() {
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            song = (Song) bundleReceive.get("song_item");
            nowPosition = allOfSong.indexOf(song);
        }
    }

    private void setView() {

        titleSong.setText(song.getTitle());
        artist.setText(song.getArtist());
        timeTotal.setText(convertTime(Integer.parseInt(song.getDuration()) / 1000));

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

    private void doNext() {
        if (nowPosition < allOfSong.size()) {
            nowPosition = nowPosition + 1;
            song = allOfSong.get(nowPosition);
            setView();
        } else {
            Toast.makeText(this.getContext(), "Bạn đang ở cuối danh sách nhạc!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doPre() {
        if (nowPosition > 0) {
            nowPosition = nowPosition - 1;
            song = allOfSong.get(nowPosition);
            setView();
        } else {
            Toast.makeText(this.getContext(), "Bạn đang ở đầu danh sách nhạc!", Toast.LENGTH_SHORT).show();
        }
    }

    private void doRepeat() {

    }

    private void doShuffle() {

    }
}