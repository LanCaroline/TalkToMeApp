package com.talktome.sinaistalktome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static String LOGTAG = "OpenCv_Log";
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private ImageView imageView;
    private ImageButton button;
    TextView textVoice;
    ImageButton voiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.capturedImage);
        button = findViewById(R.id.openCamera);
        textVoice = findViewById(R.id.textVoice);
        voiceBtn = findViewById(R.id.voiceBtn);

        if (OpenCVLoader.initDebug()){
            Log.d(LOGTAG, "OpenCv inicializado");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(open_camera, 100);
            }
        });

        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

    }

    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Oi, diga alguma coisa");

        try{
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textVoice.setText(result.get(0));
                }
                break;
            }
        }

        Bitmap video = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(video);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu idioma){
        getMenuInflater().inflate(R.menu.idioma,idioma);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.portugues:
                Toast.makeText(this, "Português", Toast.LENGTH_SHORT).show();
            case R.id.espanhol:
                Toast.makeText(this, "Espanhol", Toast.LENGTH_SHORT).show();
            case R.id.frances:
                Toast.makeText(this, "Frances", Toast.LENGTH_SHORT).show();
            case R.id.ingles:
                Toast.makeText(this, "Inglês", Toast.LENGTH_SHORT).show();
            case R.id.russo:
                Toast.makeText(this, "Russo", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
}