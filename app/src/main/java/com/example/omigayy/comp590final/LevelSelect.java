package com.example.omigayy.comp590final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LevelSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void selectLevel(View v){
        Intent intent;// = new Intent(LevelSelect.this, Level0.class);
        switch(v.getId()) {
            case R.id.global:
                intent = new Intent(LevelSelect.this, Level0.class);
                startActivity(intent);
                break;
            case R.id.science0:
                intent = new Intent(LevelSelect.this, Level1.class);
                startActivity(intent);
                break;
            case R.id.science1:
               intent = new Intent(LevelSelect.this, Level2.class);
               startActivity(intent);
               break;
//            case R.id.science2:
//                intent = new Intent(LevelSelect.this, Level3.class);
//                startActivity(intent);
//                break;
//            case R.id.science3:
//                intent = new Intent(LevelSelect.this, Level4.class);
//                startActivity(intent);
//                break;
        }
    }
}
