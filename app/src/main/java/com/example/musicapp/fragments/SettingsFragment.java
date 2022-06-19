package com.example.musicapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.musicapp.MainActivity;
import com.example.musicapp.R;
import com.example.musicapp.adapters.ImageAdapter;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    RadioGroup themes;
    RadioButton lightRadioButton;
    RadioButton darkRadioButton;
    EditText username;
    Button changeUsername;
    ImageView avatar;
    Button changeAvatar;
    Button loadImageFromDevice;
    GridView gridImage;
    ImageAdapter imageAdapter;
    ArrayList<String> srcList = new ArrayList<>();
    String avatarSrc = MainActivity.account.getAvatar();

    public interface OnDataPass {
        public void onDataPass(boolean data);
    }

    OnDataPass dataPasser;

    public static final String LIGHT_DARK_FRAG = "lightDarkFrag";
    public static final String key_dark = "darkFrag";

    public SettingsFragment() {
        // Required empty public constructor
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences(LIGHT_DARK_FRAG, Context.MODE_PRIVATE).edit();

            switch (checkedId) {
                case R.id.theme1:
                    editor.putBoolean(key_dark, false);
                    editor.apply();
                    passData(false);
                    break;
                case R.id.theme2:
                    editor.putBoolean(key_dark, true);
                    editor.apply();
                    passData(true);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        themes = view.findViewById(R.id.themesGroup);
        lightRadioButton = view.findViewById(R.id.theme1);
        darkRadioButton = view.findViewById(R.id.theme2);
        username = view.findViewById(R.id.username);
        avatar = view.findViewById(R.id.avatar);
        changeUsername = view.findViewById(R.id.changeUsername);
        changeAvatar = view.findViewById(R.id.changeAvatar);
        loadImageFromDevice = view.findViewById(R.id.loadImageFromDevice);
        gridImage = view.findViewById(R.id.gridImage);
        loadSetting();
        themes.setOnCheckedChangeListener(onCheckedChangeListener);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                changeUsername.setEnabled(true);
            }
        });
        loadImageFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageFromDevice.setVisibility(View.GONE);
                loadImageFromDevice();
            }
        });
        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.appMusic.dbManager.changeUsernameAccount(username.getText().toString());
                username.setFocusable(false);
                username.setFocusableInTouchMode(true);
                changeUsername.setEnabled(false);
                MainActivity.loadAccount();
            }
        });
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.appMusic.dbManager.changeAvatarAccount(avatarSrc);
                changeAvatar.setEnabled(false);
                MainActivity.loadAccount();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(boolean data) {
        dataPasser.onDataPass(data);
    }

    private void loadSetting() {
        SharedPreferences prefs = getContext().getSharedPreferences(LIGHT_DARK_FRAG, Context.MODE_PRIVATE);
        boolean checkRadioButtonDark = prefs.getBoolean(key_dark, false);

        if (checkRadioButtonDark) {
            darkRadioButton.setChecked(true);
        } else {
            lightRadioButton.setChecked(true);
        }

        username.setHint(MainActivity.account.getUsername());
        if (avatarSrc == null) {
            avatar.setImageResource(R.drawable.avatar);
        } else {
            avatar.setImageBitmap(BitmapFactory.decodeFile(avatarSrc));
        }
        changeUsername.setEnabled(false);
        changeAvatar.setEnabled(false);
    }

    private void loadImageFromDevice() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
        }

        String[] projection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
        };

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String data = cursor.getString(dataColumnIndex);
                srcList.add(data);
                cursor.moveToNext();
            }
            cursor.close();

            imageAdapter = new ImageAdapter(getActivity(), R.layout.image_item, srcList);
            gridImage.setAdapter(imageAdapter);

            gridImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    avatar.setImageBitmap(BitmapFactory.decodeFile(srcList.get(i)));
                    avatarSrc = srcList.get(i);
                    changeAvatar.setEnabled(true);
                }
            });
        }
    }
}