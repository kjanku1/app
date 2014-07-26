package com.kjanku1.reflexshooter;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kjanku1.drawing.GameBoard;

import java.util.Random;

public class FullscreenActivity extends Activity implements View.OnClickListener {

    private Handler frame = new Handler();

    private Point sprite1Velocity;
    private Point sprite2Velocity;
    private Point sprite3Velocity;
    private int sprite1MaxX;
    private int sprite1MaxY;
    private int sprite2MaxX;
    private int sprite2MaxY;
    private int sprite1MinY;
    private int sprite3MaxY;

    private boolean isTouching = false;

    private static final int FRAME_RATE = 20;

   /* @Override
    synchronized public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouching = true;
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouching = false;
                break;
        }
        return true;
    }*/

    private void updateVelocity() {
        int y3 = (sprite3Velocity.y > 0) ? 1 : -1;
        int speed = 0;
        if (isTouching) {
            speed = Math.abs(sprite3Velocity.y)+1;
        } else {
            speed = Math.abs(sprite3Velocity.y)-1;
        }
        if (speed>5) speed =5;
        if (speed<1) speed =1;
        sprite3Velocity.y=speed*y3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        Handler h = new Handler();

        View btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);

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
            Random r = new Random();

            int y1 = r.nextInt((((GameBoard) findViewById(R.id.canvas)).getHeight()/4) - 5 + 1);
            int y2 = r.nextInt((((GameBoard) findViewById(R.id.canvas)).getHeight()/4*3) - 5 + 1);


            ((GameBoard) findViewById(R.id.canvas)).resetStarField();
            ((GameBoard) findViewById(R.id.canvas)).resetGrassField();
            ((GameBoard) findViewById(R.id.canvas)).resetDirt1Field();
            ((GameBoard) findViewById(R.id.canvas)).resetDirt2Field();
            ((GameBoard) findViewById(R.id.canvas)).setSprite1(findViewById(R.id.canvas).getWidth()/2, y1);
            ((GameBoard) findViewById(R.id.canvas)).setSprite2(findViewById(R.id.canvas).getWidth()/2, y2);
            ((GameBoard) findViewById(R.id.canvas)).setSprite3(findViewById(R.id.canvas).getWidth() - ((GameBoard) findViewById(R.id.canvas)).getSprite3Width() * 2, 500);
            ((GameBoard) findViewById(R.id.canvas)).setFloor(findViewById(R.id.canvas).getWidth(), (findViewById(R.id.canvas).getHeight() / 8));
        }
        sprite1Velocity = new Point(5,0);
        sprite2Velocity = new Point(5,0);
        sprite3Velocity = new Point(0,5);

        int floorY = ((GameBoard)findViewById(R.id.canvas)).getFloorY();

        sprite1MaxX = findViewById(R.id.canvas).getWidth() -
                ((GameBoard)findViewById(R.id.canvas)).getSprite1Width();
        sprite1MaxY = findViewById(R.id.canvas).getHeight() - floorY -
                ((GameBoard)findViewById(R.id.canvas)).getSprite1Height();
        sprite2MaxX = findViewById(R.id.canvas).getWidth() -
                ((GameBoard)findViewById(R.id.canvas)).getSprite2Width();
        sprite2MaxY = findViewById(R.id.canvas).getHeight()/2 - floorY -
                ((GameBoard)findViewById(R.id.canvas)).getSprite2Height();
        sprite1MinY = findViewById(R.id.canvas).getHeight()/2;
        sprite3MaxY = findViewById(R.id.canvas).getHeight() - floorY -
                ((GameBoard)findViewById(R.id.canvas)).getSprite3Height();

        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override

    public void onClick(View btn) {

            initGfx();
    }

    private Runnable frameUpdate = new Runnable() {

        @Override

        synchronized public void run(){

            if (((GameBoard)findViewById(R.id.canvas)).wasCollisionDetected()) {
                Point collisionPoint =
                        ((GameBoard)findViewById(R.id.canvas)).getLastCollision();
                if (collisionPoint.x>=0) {
                    ((TextView)findViewById(R.id.labelcc)).setText("Last Collision XY("+Integer.toString(collisionPoint.x)+","+Integer.toString(collisionPoint.y)+")");
                }
                //turn off the animation until reset gets pressed
                return;
            }

            frame.removeCallbacks(frameUpdate);

            updateVelocity();

            ((TextView)findViewById(R.id.labelspd)).setText("Sprite Acceleration ("+Integer.toString(sprite2Velocity.x)+","+Integer.toString(sprite2Velocity.y)+")");

            Point sprite1 = new Point
                    (((GameBoard)findViewById(R.id.canvas)).getSprite1X(),
                            ((GameBoard)findViewById(R.id.canvas)).getSprite1Y());
            Point sprite2 = new Point
                    (((GameBoard)findViewById(R.id.canvas)).getSprite2X(),
                            ((GameBoard)findViewById(R.id.canvas)).getSprite2Y());
            Point sprite3 = new Point
                    (((GameBoard)findViewById(R.id.canvas)).getSprite3X(),
                            ((GameBoard)findViewById(R.id.canvas)).getSprite3Y());
            Point Floor = new Point
                    (((GameBoard)findViewById(R.id.canvas)).getFloorX(),
                            ((GameBoard)findViewById(R.id.canvas)).getFloorY());

            sprite1.x = sprite1.x + sprite1Velocity.x;
            if (sprite1.x > sprite1MaxX || sprite1.x < 5) {
                sprite1Velocity.x *= -1;
                sprite1.y = sprite1.y -30;
                }
            sprite1.y = sprite1.y + sprite1Velocity.y;
            if (sprite1.y > sprite1MaxY || sprite1.y < 5) {
                sprite1Velocity.y *= -1;
                sprite1.y = (((GameBoard) findViewById(R.id.canvas)).getHeight()/4);
            }
            sprite1.y = sprite1.y + sprite1Velocity.y;
            if (sprite1.y > sprite1MinY || sprite1.y < 5) {
                sprite1Velocity.y *= -1;
                sprite1.y = (((GameBoard) findViewById(R.id.canvas)).getHeight()/4);
            }
            sprite2.x = sprite2.x + sprite2Velocity.x;
            if (sprite2.x > sprite2MaxX || sprite2.x < 5) {
                sprite2Velocity.x *= -1;
                sprite2.y = sprite2.y -30;
            }
            sprite2.y = sprite2.y + sprite2Velocity.y;
            if (sprite2.y > sprite2MaxY || sprite2.y < 5) {
                sprite2Velocity.y *= -1;
                sprite2.y = (((GameBoard)findViewById(R.id.canvas)).getHeight()/4*3);
            }
            sprite3.y = sprite3.y + sprite3Velocity.y;
            if (sprite3.y > sprite3MaxY || sprite3.y < 5) {
                sprite3Velocity.y *= -1;}

            ((GameBoard)findViewById(R.id.canvas)).setSprite1(sprite1.x, sprite1.y);
            ((GameBoard)findViewById(R.id.canvas)).setSprite2(sprite2.x, sprite2.y);
            ((GameBoard)findViewById(R.id.canvas)).setSprite3(sprite3.x, sprite3.y);
            ((GameBoard)findViewById(R.id.canvas)).setFloor(Floor.x, Floor.y);

            ((GameBoard)findViewById(R.id.canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };
}