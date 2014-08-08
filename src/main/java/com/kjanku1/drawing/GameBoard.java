package com.kjanku1.drawing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kjanku1.reflexshooter.Dialog;
import com.kjanku1.reflexshooter.FullscreenActivity;
import com.kjanku1.reflexshooter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends View {

    public int scorex4 = 0;
    private Paint p;
    private List<Point> starField = null;
    private List<Point> grassField = null;
    private List<Point> dirt1Field = null;
    private List<Point> dirt2Field = null;

    private int starAlpha = 80;
    private int starFade = 3;
    private int brown = getResources().getColor(R.color.brown);

    private Rect sprite1Bounds = new Rect(0,0,0,0);
    private Rect sprite2Bounds = new Rect(0,0,0,0);
    private Rect sprite3Bounds = new Rect(0,0,0,0);
    private Rect floorBounds = new Rect(0,0,getWidth(),getHeight()/8);

    private Point sprite1;
    private Point sprite2;
    private Point sprite3;
    private Point floor;

    private Bitmap bm1 = null;
    private Bitmap bm2 = null;
    private Bitmap bm3 = null;
    private Matrix m = null;

    private boolean collisionDetected = false;
    private Point lastCollision = new Point(-1,-1);
    private int sprite3Rotation = 0;

    private static final int NUM_OF_STARS = 100;
    private static final int NUM_OF_GRASS = 150;
    private static final int NUM_OF_DIRT1 = 150;
    private static final int NUM_OF_DIRT2 = 150;

    synchronized public int getScore() { return scorex4/4; }
    synchronized public void setScore(int i) { scorex4=i; }

    synchronized public void setSprite1(int x, int y) { sprite1=new Point(x,y); }
    synchronized public void setSprite2(int x, int y) {
        sprite2=new Point(x,y);
    }
    synchronized public void setSprite3(int x, int y) {
        sprite3=new Point(x,y);
    }
    synchronized public void setFloor(int x, int y) {
        floor=new Point(x,y);
    }

    synchronized public int getSprite1X() { return sprite1.x; }
    synchronized public int getSprite2X() { return sprite2.x; }
    synchronized public int getSprite3X() { return sprite3.x; }
    synchronized public int getFloorX() {
        return floor.x;
    }

    synchronized public int getSprite1Y() { return sprite1.y; }
    synchronized public int getSprite2Y() { return sprite2.y; }
    synchronized public int getSprite3Y() { return sprite3.y; }
    synchronized public int getFloorY() { return floor.y; }

    synchronized public void resetStarField(){
        starField = null;
    }
    synchronized public void resetGrassField(){
        grassField = null;
    }
    synchronized public void resetDirt1Field(){
        dirt1Field = null;
    }
    synchronized public void resetDirt2Field(){
        dirt2Field = null;
    }

    synchronized public int getSprite1Width() { return sprite1Bounds.width(); }
    synchronized public int getSprite2Width() { return sprite2Bounds.width(); }
    synchronized public int getSprite3Width() { return sprite3Bounds.width(); }

    synchronized public int getSprite1Height() { return sprite1Bounds.height(); }
    synchronized public int getSprite2Height() { return sprite2Bounds.height(); }
    synchronized public int getSprite3Height() { return sprite3Bounds.height(); }

    synchronized public int getVelocity(){return Velocity.y;}

    synchronized public Point getLastCollision() {
        return lastCollision;
    }
    synchronized public boolean wasCollisionDetected() {
        return collisionDetected;
    }

    public Point Velocity;


    public GameBoard(Context context, AttributeSet aSet) {
        super(context, aSet);

        p = new Paint();

        sprite1 = new Point(-1,-1);
        sprite2 = new Point(-1,-1);
        sprite3 = new Point(-1,-1);
        floor = new Point(-1,-1);
        Velocity = new Point(0,10);

        m = new Matrix();
        p = new Paint();
        bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.clone);
        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.clone2);
        bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2);

        sprite1Bounds = new Rect(0,0, bm1.getWidth(), bm1.getHeight());
        sprite2Bounds = new Rect(0,0, bm2.getWidth(), bm2.getHeight());
        sprite3Bounds = new Rect(0,0, bm3.getWidth(), bm3.getHeight());
        floorBounds = new Rect(0,0,getWidth(),getFloorY());


    }


    private void initializeStars(int maxX, int maxY) {
        starField = new ArrayList<Point>();
        for (int i=0; i<NUM_OF_STARS; i++){
            Random r = new Random();

            int x = r.nextInt(maxX-5+1)+5;
            int y = r.nextInt(maxY-5+1)+5;

            starField.add(new Point (x,y));
        }
        collisionDetected = false;
    }
    private void initializeGrass(int maxX, int maxY) {
        grassField = new ArrayList<Point>();
        for (int i = 0; i < NUM_OF_GRASS; i++) {
            Random r = new Random();

            int x = r.nextInt(maxX - 5 + 1) + 5;
            int y = r.nextInt(maxY - 5 + 1) + 5 + getHeight()-(getFloorY());

            grassField.add(new Point(x, y));
        }
        collisionDetected = false;
    }
    private void initializeDirt1(int maxX, int maxY) {
        dirt1Field = new ArrayList<Point>();
        for (int i = 0; i < NUM_OF_DIRT1; i++) {
            Random r = new Random();

            int x = r.nextInt(maxX - 5 + 1) + 5;
            int y = r.nextInt(maxY - 5 + 1) + 5 + getHeight()-(getFloorY());
            dirt1Field.add(new Point(x, y));
        }
        collisionDetected = false;
    }
    private void initializeDirt2(int maxX, int maxY) {
        dirt2Field = new ArrayList<Point>();
        for (int i = 0; i < NUM_OF_DIRT2; i++) {
            Random r = new Random();

            int x = r.nextInt(maxX - 5 + 1) + 5;
            int y = r.nextInt(maxY - 5 + 1) + 5 + getHeight()-(getFloorY());

            dirt2Field.add(new Point(x, y));
        }
        collisionDetected = false;
    }

    public void scoreUp(Context context) {

        final SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        int soundId = sp.load(context, R.raw.level_up, 1);
        sp.play(soundId, 1, 1, 0, 0, 1);

        final MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.level_up);
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.release();
            }

            ;
        });
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sp.release();
            }
        });
        scorex4 = scorex4 + 1;
    }
    public void jump(Context context) {

        final SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        /** soundId for Later handling of sound pool **/
        int soundId = sp.load(context, R.raw.drum2, 1);
        sp.play(soundId, 1, 1, 0, 0, 1);

        final MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.drum2);
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.release();
            };
        });
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sp.release();
            }
        });

        int i = 1 *(-40);
        Velocity.y = i;

        Handler h = new Handler();

        h.postDelayed(new Runnable(){

            @Override
            public void run(){
                int i = 10;
                Velocity.y = i;
            }
        }
                ,50);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN) {

            jump(getContext());
        }
        return true;
    }
    public boolean checkForCollision() {
        if (sprite1.x<0 && sprite2.x<0 && sprite3.x<0 && sprite1.y<0 && sprite2.y<0 && sprite3.y<0) return false;
        Rect r3 = new Rect(sprite1.x, sprite1.y, sprite1.x
                + sprite1Bounds.width(),  sprite1.y + sprite1Bounds.height());
        Rect r2 = new Rect(sprite2.x, sprite2.y, sprite2.x +
                sprite2Bounds.width(), sprite2.y + sprite2Bounds.height());
        Rect r1 = new Rect(sprite3.x, sprite3.y, sprite3.x +
                sprite3Bounds.width(), sprite3.y + sprite3Bounds.height());
        Rect r4 = new Rect(r1);
        if(r1.intersect(r2)) {
            for (int i = r1.left; i<r1.right; i++) {
                for (int j = r1.top; j<r1.bottom; j++) {
                    if (bm3.getPixel(i-r4.left, j-r4.top)!=
                            Color.TRANSPARENT) {
                        if (bm2.getPixel(i-r2.left, j-r2.top) !=
                                Color.TRANSPARENT) {
                            lastCollision = new Point(sprite2.x +
                                    i-r2.left, sprite2.y + j-r2.top);
                            return true;
                        }
                    }
                }
            }
        }
        if(r1.intersect(r3)) {
            for (int i = r1.left; i<r1.right; i++) {
                for (int j = r1.top; j<r1.bottom; j++) {
                    if (bm3.getPixel(i-r4.left, j-r4.top)!=
                            Color.TRANSPARENT) {
                        if (bm1.getPixel(i-r3.left, j-r3.top) !=
                                Color.TRANSPARENT) {
                            lastCollision = new Point(sprite1.x +
                                    i-r3.left, sprite1.y + j-r3.top);
                            return true;
                        }
                    }
                }
            }
        }
        if(r1.bottom > getHeight()-getFloorY()) {
            lastCollision = new Point(sprite3.x+getSprite3Height()/2, sprite3.y+getSprite3Height());
            return true;
        }
        if(r1.top <= 0) {
            lastCollision = new Point(sprite3.x+getSprite3Height()/2, 0);
            return true;
        }
        lastCollision = new Point(-1,-1);
        return false;
    }

    @Override

    synchronized public void onDraw(Canvas canvas) {

        //draw canvas
        p.setColor(Color.BLACK);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        //draw stars
        if (starField == null) {
            initializeStars(canvas.getWidth(), canvas.getHeight());
        }

        p.setColor(Color.CYAN);
        p.setAlpha(starAlpha += starFade);

        if (starAlpha >= 252 || starAlpha <= 80) starFade = starFade * -1;
        p.setStrokeWidth(6);

        for (int i = 0; i < NUM_OF_STARS; i++) {
            canvas.drawPoint(starField.get(i).x++, starField.get(i).y, p);
            //when reach the end of screen, x=0
            if (starField.get(i).x > canvas.getWidth()) {
                starField.get(i).x = 0;
            }
        }
        //draw sprites
        if (sprite1.x >= 0) {
            canvas.drawBitmap(bm1, sprite1.x, sprite1.y, null);
            if (sprite1.x > sprite3.x+getSprite3Width()/2 && sprite2.x < sprite3.x+getSprite1Width()) {
                scoreUp(getContext());
            }
            //when reach the end of screen, x=0
            if (sprite1.x > canvas.getWidth()) {
                    for (int i = 0; i < 1; i++) {
                        Random r = new Random();
                        int y1 = r.nextInt(((getHeight()/2-getHeight()/8)/2)-getSprite1Height()) ;
                        sprite1.x = 0;
                        sprite1.y = y1+getSprite1Height();
                        sprite2.x = 0;
                        sprite2.y = y1+getSprite2Height()+getSprite3Height()*3;
                    }
            }
        }
        if (sprite2.x >= 0) {
            canvas.drawBitmap(bm2, sprite2.x, sprite2.y, null);
        }
        if (sprite3.x >= 0) {
            m.reset();
            m.postTranslate((float) (sprite3.x), (float) (sprite3.y));
            m.postRotate(sprite3Rotation,
                    (float) (sprite3.x + sprite3Bounds.width()/2),
                    (float) (sprite3.y + sprite3Bounds.height()/2));
            canvas.drawBitmap(bm3, m, null);
            sprite3Rotation += 5;
            if (sprite3Rotation >= 360) sprite3Rotation = 0;
        }
        if (floor.x >= 0) {
            p.setColor(brown);
            p.setAlpha(255);
            p.setStrokeWidth(1);
            canvas.drawRect(0, getHeight() - getFloorY(), getFloorX(), getHeight(), p);
            canvas.drawBitmap(bm3, getHeight() - getFloorY(), getFloorX(), p);
        }
        //draw grass
        if (grassField == null) {
            initializeGrass(getWidth(), getHeight() / 30);
        }
        p.setColor(Color.GREEN);
        p.setAlpha(255);
        p.setStrokeWidth(6);

        for (int i = 0; i < NUM_OF_GRASS; i++) {
            canvas.drawPoint(grassField.get(i).x, grassField.get(i).y, p);
        }
        //draw dirt1
        if (dirt1Field == null) {
            initializeDirt1(getWidth(), getHeight() / 8);
        }
        p.setColor(Color.GRAY);
        p.setAlpha(255);
        p.setStrokeWidth(6);

        for (int i = 0; i < NUM_OF_DIRT1; i++) {
            canvas.drawPoint(dirt1Field.get(i).x, dirt1Field.get(i).y, p);
        }
        //draw dirt2
        if (dirt2Field == null) {
            initializeDirt2(getWidth(), getHeight() / 8);
        }
        p.setColor(Color.BLACK);
        p.setAlpha(255);
        p.setStrokeWidth(6);

        for (int i = 0; i < NUM_OF_DIRT2; i++) {
            canvas.drawPoint(dirt2Field.get(i).x, dirt2Field.get(i).y, p);
        }

        collisionDetected = checkForCollision();
         if (collisionDetected) {
             Intent i = new Intent(getContext(), Dialog.class);
             getContext().startActivity(i);
             //if there is one lets draw a red X
             p.setColor(Color.RED);
             p.setAlpha(255);
             p.setStrokeWidth(5);
             canvas.drawLine(lastCollision.x - 5, lastCollision.y - 5,
                    lastCollision.x + 5, lastCollision.y + 5, p);
            canvas.drawLine(lastCollision.x + 5, lastCollision.y - 5,
                    lastCollision.x - 5, lastCollision.y + 5, p);
        }
    }
}
