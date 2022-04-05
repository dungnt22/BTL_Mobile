package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.musicapp.fragments.allSongFragment;
import com.example.musicapp.fragments.isPlayingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_IS_PLAYING = 0;
    private static final int FRAGMENT_ALL_SONG = 1;
    private static final int FRAGMENT_FAVOURITE = 2;

    private int currentFragment = 0;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        // add toggle to drawerLayout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //declare navigationView and capture select item event
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // mặc định mở fragment isPlaying đầu tiên
        changeFragment(new isPlayingFragment());
        navigationView.getMenu().findItem(R.id.menu_playing).setChecked(true);
    }

    // xử lý sự kiện chọn item trong menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_playing) {
            if (currentFragment != FRAGMENT_IS_PLAYING) {
                changeFragment(new isPlayingFragment());
                currentFragment = FRAGMENT_IS_PLAYING;
            }
        } else if (id == R.id.menu_allSong) {
            if (currentFragment != FRAGMENT_ALL_SONG) {
                changeFragment(new allSongFragment());
                currentFragment = FRAGMENT_ALL_SONG;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // chuyển fragment
    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    // ấn Back trên thiết bị -> đóng menu nếu menu đang mở
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}