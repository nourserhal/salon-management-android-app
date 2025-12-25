package com.example.noor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    ImageView imgStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgStart = findViewById(R.id.imgBackground);

        imgStart.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }
}
