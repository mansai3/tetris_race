package com.example.tetorirece;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

public class MiddleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        TextView tokutenLabel = findViewById(R.id.tokutenLabel);
        int tokuten = getIntent().getIntExtra("tokuten",0);
        tokutenLabel.setText(tokuten+"");
    }

    public void startGame(View view){
        int tokuten = getIntent().getIntExtra("tokuten",0);
        if(tokuten < 301){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else if(301 <= tokuten && tokuten < 1000){
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity3.class));
        }
    }
}