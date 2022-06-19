package com.example.musicapp.services;

import static com.example.musicapp.ApplicationClass.ACTION_CLEAR;
import static com.example.musicapp.ApplicationClass.ACTION_NEXT;
import static com.example.musicapp.ApplicationClass.ACTION_PLAY_PAUSE;
import static com.example.musicapp.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicapp.ApplicationClass.CHANNEL_ID_2;
import static com.example.musicapp.Base.getImage;
import static com.example.musicapp.Base.nowPlaying;
import static com.example.musicapp.Base.nowPosition;
import static com.example.musicapp.Base.repeat;
import static com.example.musicapp.Base.shuffle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.musicapp.ActionPlaying;
import com.example.musicapp.NotificationReceiver;
import com.example.musicapp.R;
import com.example.musicapp.fragments.isPlayingFragment;
import com.example.musicapp.models.Song;

import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    IBinder myBinder = new MyBinder();
    private MediaPlayer mediaPlayer;
    ActionPlaying actionPlaying;
    private int position;
    Song mSong;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getIntExtra("index", -1);
        String actionName = intent.getStringExtra("ActionName");
        if (position != -1) {
            playMusic(position);
        }
        if (actionName != null) {
            switch (actionName) {
                case "PlayPause":
                    pausePlay();
                    break;
                case "Next":
                    next();
                    onCompleted();
                    break;
                case "Previous":
                    previous();
                    onCompleted();
                    break;
                case "Clear":
                    clear();
                    break;
            }
        }
        return START_STICKY;
    }

    private void playMusic(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        createMusic(position);
        mediaPlayer.start();
    }

    public void createMusic(int position) {
        Song song = nowPlaying.get(position);
        Uri uri = Uri.parse(song.getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
        onCompleted();
    }

    public void onCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }

    public void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
    }

    public void showNotification(int playPauseBtn) {
        Intent intent = new Intent(this, isPlayingFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent preIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prePending = PendingIntent.getBroadcast(this, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_PLAY_PAUSE);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent clearIntent = new Intent(this, NotificationReceiver.class)
                .setAction(ACTION_CLEAR);
        PendingIntent clearPending = PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Song song = nowPlaying.get(nowPosition);
        mSong = song;
        byte[] image = getImage(song.getPath());
        Bitmap thumb = null;
        if (image != null) {
            thumb = BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_record);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(playPauseBtn)
                .setLargeIcon(thumb)
                .setContentTitle(song.getTitle())
                .setContentText(song.getArtist())
                .addAction(R.drawable.ic_previous, "Previous", prePending)
                .addAction(playPauseBtn, "Pause", pausePending)
                .addAction(R.drawable.ic_next, "Next", nextPending)
                .addAction(R.drawable.ic_clear, "Clear", clearPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        startForeground(2, notification);
    }

    private void pausePlay() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                showNotification(R.drawable.ic_play_2);
                mediaPlayer.pause();
            } else {
                showNotification(R.drawable.ic_pause);
                mediaPlayer.start();
            }
            sendDataToIsPlayingFragment(ACTION_PLAY_PAUSE);
        }
    }

    private void previous() {
        if (shuffle) {
            nowPosition = getRandomIndex(nowPlaying.size() - 1);
        } else if (repeat && nowPosition == 0) {
            nowPosition = nowPlaying.size() - 1;
        } else if (nowPosition > 0) {
            nowPosition = nowPosition - 1;
        }
        playMusic(nowPosition);
        showNotification(R.drawable.ic_pause);
        sendDataToIsPlayingFragment(ACTION_PREVIOUS);
    }

    private void next() {
        if (shuffle) {
            nowPosition = getRandomIndex(nowPlaying.size() - 1);
        } else if (repeat) {
            nowPosition = ((nowPosition + 1) % nowPlaying.size());
        } else if (nowPosition < (nowPlaying.size() - 1)) {
            nowPosition = nowPosition + 1;
        }
        playMusic(nowPosition);
        showNotification(R.drawable.ic_pause);
        sendDataToIsPlayingFragment(ACTION_NEXT);
    }

    private void clear() {
        stopSelf();
        pause();
        sendDataToIsPlayingFragment(ACTION_CLEAR);
    }

    private void sendDataToIsPlayingFragment(String action) {
        Intent intent = new Intent("send_data_to_fragment");
        Bundle bundle = new Bundle();
        bundle.putSerializable("oj_song", mSong);
        bundle.putBoolean("status_player", isPlaying());
        bundle.putString("action_music", action);
        intent.putExtras(bundle);

        sendBroadcast(intent);
    }

    private int getRandomIndex(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

}
