package com.kjanku1.reflexshooter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kjanku1.drawing.GameBoard;
import com.kjanku1.reflexshooter.R;

import java.util.Random;

public class Dialog extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        View btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        ((View)findViewById(R.id.btn2)).setOnClickListener(this);

       // int score = (((GameBoard) findViewById(R.id.canvas)).getScore());
        //((TextView) findViewById(R.id.scor)).setText("Score:" +Integer.toString(score));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
            FullscreenActivity.fa.finish();
            Intent intent = new Intent(this, FullscreenActivity.class);
            startActivity(intent);
            //((GameBoard) findViewById(R.id.canvas)).setScore(0);
            finish();
            case R.id.btn2:
                finish();
                break;
        }
    }
}
