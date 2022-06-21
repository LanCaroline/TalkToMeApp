package com.talktome.talktomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name, email;
    ImageButton signOutBtn;
    TextView textVoice;
    ImageButton voiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Voice Comand
        textVoice = findViewById(R.id.textVoice);
        voiceBtn = findViewById(R.id.voiceBtn);

        //button click to show speech to text dialog
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        // Conta do E-mail logado

        //name = findViewById(R.id.name);
        //email = findViewById(R.id.email);
        signOutBtn = findViewById(R.id.signOutBtn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
//            name.setText(personName);
//            email.setText(personEmail);
        }

        // Sair da conta logada
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



        //Navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set translate home page selected
        bottomNavigationView.setSelectedItemId(R.id.nav_translate);

        //selected navigation item
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_translate:
                        return true;

                    case R.id.nav_video:
                        startActivity(new Intent(getApplicationContext(), VideoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_camera:
                        startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_folder:
                        startActivity(new Intent(getApplicationContext(), FolderActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    //Sair da conta
    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    //Listen Audio
    private void speak(){
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Oi, diga alguma coisa");

        // start intent
        try{
            //in there was no error
            //show dialog
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            //if there was some error
            //get message of error and show
            Toast.makeText(this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //receive voice input and handle it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //voice
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set to text view
                    textVoice.setText(result.get(0));
                }
                break;
            }
        }
    }

}