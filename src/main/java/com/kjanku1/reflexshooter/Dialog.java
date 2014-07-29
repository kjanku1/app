package com.kjanku1.reflexshooter;

import android.app.Activity;
import android.content.DialogInterface;
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

        //int score = (((GameBoard) findViewById(R.id.canvas)).getScore());
        //((TextView) findViewById(R.id.scor)).setText("Score:" +Integer.toString((((GameBoard) findViewById(R.id.canvas)).getScore())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn:
                initGfx();
                ((GameBoard) findViewById(R.id.canvas)).setScore(0);

                frame.removeCallbacks(frameUpdate);

                Point sprite1 = new Point
                        (((GameBoard) findViewById(R.id.canvas)).getSprite1X(),
                                ((GameBoard) findViewById(R.id.canvas)).getSprite1Y());
                Point sprite2 = new Point
                        (((GameBoard) findViewById(R.id.canvas)).getSprite2X(),
                                ((GameBoard) findViewById(R.id.canvas)).getSprite2Y());
                Point sprite3 = new Point
                        (((GameBoard) findViewById(R.id.canvas)).getSprite3X(),
                                ((GameBoard) findViewById(R.id.canvas)).getSprite3Y());
                Point Floor = new Point
                        (((GameBoard) findViewById(R.id.canvas)).getFloorX(),
                                ((GameBoard) findViewById(R.id.canvas)).getFloorY());

                int yy = ((GameBoard) findViewById(R.id.canvas)).getVelocity();
                sprite1.x = sprite1.x + sprite1Velocity.x;
                sprite2.x = sprite2.x + sprite2Velocity.x;
                sprite3.y = sprite3.y + yy;

                ((GameBoard)findViewById(R.id.canvas)).setSprite1(sprite1.x, sprite1.y);
                ((GameBoard)findViewById(R.id.canvas)).setSprite2(sprite2.x, sprite2.y);
                ((GameBoard)findViewById(R.id.canvas)).setSprite3(sprite3.x, sprite3.y);
                ((GameBoard)findViewById(R.id.canvas)).setFloor(Floor.x, Floor.y);

                ((GameBoard)findViewById(R.id.canvas)).invalidate();
                frame.postDelayed(frameUpdate, FRAME_RATE);
                break;*/
            case R.id.btn2:
                finish();
                break;
        }
    }
}
