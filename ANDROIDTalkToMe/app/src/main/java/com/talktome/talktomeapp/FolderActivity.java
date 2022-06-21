package com.talktome.talktomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        //Navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set translate home page selected
        bottomNavigationView.setSelectedItemId(R.id.nav_folder);

        //selected navigation item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_translate:
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                        overridePendingTransition(0,0);

                    case R.id.nav_video:
                        startActivity(new Intent(getApplicationContext(), VideoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_camera:
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_folder:
                        return true;
                }
                return false;
            }
        });
    }
}