package com.kjanku1.reflexshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.kjanku1.drawing.GameBoard;

import junit.framework.Test;

import java.io.IOException;
import java.util.Random;

public class FullscreenActivity extends Activity implements View.OnClickListener {

    private Handler frame = new Handler();

    private Point sprite1Velocity;
    private Point sprite2Velocity;

    MediaPlayer mp;

    private static final int FRAME_RATE = 20;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        fa =this;
        View btn = findViewById(R.id.btn3);
        btn.setOnClickListener(this);

        Handler h = new Handler();

        h.postDelayed(new Runnable(){

            @Override

            public void run(){
                initGfx();
            }
        }
                ,1000);
    }

    synchronized public void initGfx(){

        for (int i = 0; i < 1; i++) {
            int sprite1Height = ((GameBoard) findViewById(R.id.canvas)).getSprite1Height();
            int sprite2Height = ((GameBoard) findViewById(R.id.canvas)).getSprite2Height();
            Random r = new Random();
            int y1 = r.nextInt((((GameBoard)findViewById(R.id.canvas)).getHeight()/2-(((GameBoard)findViewById(R.id.canvas)).getHeight()/8)/2)-sprite1Height);

            ((GameBoard) findViewById(R.id.canvas)).resetStarField();
            ((GameBoard) findViewById(R.id.canvas)).resetGrassField();
            ((GameBoard) findViewById(R.id.canvas)).resetDirt1Field();
            ((GameBoard) findViewById(R.id.canvas)).resetDirt2Field();
            ((GameBoard) findViewById(R.id.canvas)).setSprite1(findViewById(R.id.canvas).getWidth() / ((GameBoard) findViewById(R.id.canvas)).getSprite1Width(), y1 + ((GameBoard) findViewById(R.id.canvas)).getSprite1Height());
            ((GameBoard) findViewById(R.id.canvas)).setSprite2(findViewById(R.id.canvas).getWidth() / ((GameBoard) findViewById(R.id.canvas)).getSprite2Width(), y1 +sprite2Height+ ((GameBoard) findViewById(R.id.canvas)).getSprite3Height() * 3);
            ((GameBoard) findViewById(R.id.canvas)).setSprite3(findViewById(R.id.canvas).getWidth() - ((GameBoard) findViewById(R.id.canvas)).getSprite3Width() * 2, (findViewById(R.id.canvas)).getHeight() / 3);
            ((GameBoard) findViewById(R.id.canvas)).setFloor(findViewById(R.id.canvas).getWidth(), (findViewById(R.id.canvas).getHeight() / 8));
        }
        sprite1Velocity = new Point(7,0);
        sprite2Velocity = new Point(7,0);

        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn3:

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
                break;
            }
        }
    private Runnable frameUpdate = new Runnable() {

        @Override

        synchronized public void run() {

            if (((GameBoard) findViewById(R.id.canvas)).wasCollisionDetected()) {
                Point collisionPoint =
                        ((GameBoard) findViewById(R.id.canvas)).getLastCollision();
                if (collisionPoint.x >= 0) {
                    ((TextView) findViewById(R.id.labelcc)).setText("Last Collision XY(" + Integer.toString(collisionPoint.x) + "," + Integer.toString(collisionPoint.y) + ")");
                }

                if(mp !=null){mp.release();}

                MediaPlayer mp = MediaPlayer.create(FullscreenActivity.this, R.raw.explosion2);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    };
                });
                return;
            }

            frame.removeCallbacks(frameUpdate);

            int score = (((GameBoard) findViewById(R.id.canvas)).getScore());
            ((TextView) findViewById(R.id.labelspd)).setText("Score:" +Integer.toString(score));

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
        }
    };
}