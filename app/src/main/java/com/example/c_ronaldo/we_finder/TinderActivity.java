package com.example.c_ronaldo.we_finder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TinderActivity extends AppCompatActivity {
    private final String TITLE = "探探";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinder);
        setTitle(TITLE);
    }
}
