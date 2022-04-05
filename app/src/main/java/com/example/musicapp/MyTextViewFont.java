package com.example.musicapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class MyTextViewFont extends AppCompatTextView {
    public MyTextViewFont(@NonNull Context context) {
        super(context);
        init();
    }

    public MyTextViewFont(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewFont(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fa-solid-900.ttf");
        setTypeface(typeface);
    }
}
