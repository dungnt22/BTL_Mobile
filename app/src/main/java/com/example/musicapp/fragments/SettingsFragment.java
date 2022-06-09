package com.example.musicapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.musicapp.R;

public class SettingsFragment extends Fragment {
    RadioGroup themes;
    RadioButton lightRadioButton;
    RadioButton darkRadioButton;

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
        loadSetting();
        themes.setOnCheckedChangeListener(onCheckedChangeListener);

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
    }
}