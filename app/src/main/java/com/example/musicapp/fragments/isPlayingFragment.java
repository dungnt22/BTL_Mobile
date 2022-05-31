package com.example.musicapp.fragments;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.musicapp.ApplicationClass.ACTION_NEXT;
import static com.example.musicapp.ApplicationClass.ACTION_PLAY_PAUSE;
import static com.example.musicapp.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicapp.ApplicationClass.CHANNEL_ID_2;
import static com.example.musicapp.Base.favoritePlaylist;
import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.Base.nowPosition;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.ActionPlaying;
import com.example.musicapp.NotificationReceiver;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.example.musicapp.services.MusicService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class isPlayingFragment extends Fragment implements ActionPlaying, ServiceConnection, MediaPlayer.OnCompletionListener {

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

    MusicService musicService;

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
                if (musicService != null && fromUser) {
                    musicService.seekTo(progress * 1000);
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
                if (musicService != null) {
                    int currentPosition = musicService.getCurrentPosition() / 1000;
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

        if (shuffle) {
            shuffleButton.setImageResource(R.drawable.ic_shuffle_on);
        } else {
            shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
        }

        if (repeat) {
            repeatButton.setImageResource(R.drawable.ic_repeat_on);
        } else {
            repeatButton.setImageResource(R.drawable.ic_repeat_off);
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
        Intent intent = new Intent(this.getContext(), MusicService.class);
        intent.putExtra("index", nowPosition);
        getActivity().startService(intent);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        if (musicService != null) {
            musicService.showNotification(R.drawable.ic_pause);
        }
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

    @Override
    public void onResume() {
        Intent intent = new Intent(this.getActivity(), MusicService.class);
        getActivity().bindService(intent, this, BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unbindService(this);
        super.onPause();
    }

    public void doPlayOrPause() {
        if (musicService.isPlaying()) {
            musicService.showNotification(R.drawable.ic_play_2);
            playPauseButton.setImageResource(R.drawable.ic_play_2);
            musicService.pause();
        } else {
            musicService.showNotification(R.drawable.ic_pause);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            musicService.start();
        }
    }

    public void doNext() {
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

    public void doPre() {
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
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) iBinder;
        musicService = myBinder.getService();
        musicService.setCallBack(this);

        seekBar.setMax(musicService.getDuration() / 1000);
        musicService.getMediaPlayer().setOnCompletionListener(this);
        musicService.showNotification(R.drawable.ic_pause);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        doNext();
    }
}