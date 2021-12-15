package com.example.tetorirece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;





public class MainActivity2 extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView coin;
    private ImageView pink;
    private ImageView fastCar;
    private ImageView redCar;
    private ImageView lowCar;
    private ImageView goal;


    //サイズ
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;
    private int blackSize;

    // 位置
    private float boxY;
    private float orangeX;
    private float orangeY;
    private float coinX;
    private float coinY;
    private float pinkX;
    private float pinkY;
    private float fastCarX;
    private float fastCarY;
    private float redCarX;
    private float redCarY;
    private float lowCarX;
    private float lowCarY;
    private float goalX;

    //Score
    private int score = 0;

    //Handler & Timer
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Status
    private boolean action_flg = false;
    private boolean start_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        orange = findViewById(R.id.orange);
        coin = findViewById(R.id.coin);
        pink = findViewById(R.id.pink);
        fastCar = findViewById(R.id.fastCar);
        redCar = findViewById(R.id.redCar);
        lowCar = findViewById(R.id.lowCar);
        goal = findViewById(R.id.goallabel);


        //Screen Size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;


        orange.setX(-80.0f);
        orange.setY(-80.0f);
        coin.setX(-80.0f);
        coin.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        fastCar.setX(-80.0f);
        fastCar.setY(-80.0f);
        redCar.setX(-80.0f);
        redCar.setY(-80.0f);
        lowCar.setX(-80.0f);
        lowCar.setY(-80.0f);
        goal.setX(-1000.0f);
        goal.setY(0);

        scoreLabel.setText("Score : 0");

    }



    public void changePos(){

        hitCheck();
        int tokuten = getIntent().getIntExtra("tokuten",0);

        //Orange
        orangeX -= 12;
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (float)Math.floor(Math.random()*(frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //coin
        coinX -= 20;
        if(coinX < 0){
            coinX = screenWidth + 80;
            coinY = (float)Math.floor(Math.random()*(frameHeight - coin.getHeight()));
        }
        coin.setX(coinX);
        coin.setY(coinY);

        //FastCar
        fastCarX -= 30;

        if(fastCarX < 0){
            fastCarX = screenWidth + 10;
            fastCarY = (float)Math.floor(Math.random()*(frameHeight - fastCar.getHeight()));
        }
        fastCar.setX(fastCarX);
        fastCar.setY(fastCarY);

        //redCar
        redCarX -= 100;

        if(redCarX < 0){
            redCarX = screenWidth + 10000;
            redCarY = (float)Math.floor(Math.random()*(frameHeight - redCar.getHeight()));
        }
        redCar.setX(redCarX);
        redCar.setY(redCarY);

        //Lowcar
        lowCarX -= 10;

        if(lowCarX < 0){
            lowCarX = screenWidth + 400;
            lowCarY = (float)Math.floor(Math.random()*(frameHeight - lowCar.getHeight()));
        }
        lowCar.setX(lowCarX);
        lowCar.setY(lowCarY);

        //Pink
        pinkX -= 20;
        if(pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (float)Math.floor(Math.random()*(frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //goal
        goalX -= 5;
        if(goalX < 0){
            goalX = screenWidth + 5000;
        }
        goal.setX(goalX);


        if(action_flg){
            //Touching
            boxY -= 20;
        }else{
            //Releasing
            boxY += 20;
        }

        if(boxY < 0) boxY = 0;

        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : "+score);
    }

    public void hitCheck(){

        //Orange
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        //0 <= orangeCenterX <= boxWidth
        //boxY <= orangeCenterY <= boxY + boxHeight

        if(0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){

            orangeX = -10.0f;
            score += 10;
        }

        //Coin
        float coinCenterX = coinX + coin.getWidth() / 2;
        float coinCenterY = coinY + coin.getHeight() / 2;

        if(0 <= coinCenterX && coinCenterX <= boxSize && boxY <= coinCenterY && coinCenterY <= boxY + boxSize){

            coinX = -10.0f;
            score += 10;
        }

        //Pink
        float pinkCenterX = pinkX + pink.getWidth() / 2;
        float pinkCenterY = pinkY + pink.getHeight() / 2;

        if(0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

            pinkX = -10.0f;
            score += 30;
        }

        //Car
        float blackCenterX = fastCarX + fastCar.getWidth() / 2;
        float blackCenterY = fastCarY + fastCar.getHeight() / 2;

        float redCarCenterX = redCarX + redCar.getWidth() / 2;
        float redCarCenterY = redCarY + redCar.getHeight() / 2;

        float black2CenterX = lowCarX + lowCar.getWidth() / 2;
        float black2CenterY = lowCarY + lowCar.getHeight() / 2;

        //FastCar
        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {

            //Game Over!
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            stopAnime();
        }

        //RedCar
        if(0 <= redCarCenterX && redCarCenterX <= boxSize && boxY <= redCarCenterY && redCarCenterY <= boxY + boxSize) {

            //Game Over!
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            stopAnime();
        }

        //Lowcar
        if(0 <= black2CenterX && black2CenterX <= boxSize && boxY <= black2CenterY && black2CenterY <= boxY + boxSize) {

            //Game Over!
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            stopAnime();
        }

        //goal
        if(goalX == boxSize) {

            //Game Over!
            if(timer != null){
                timer.cancel();
                timer = null;
            }
            stopAnime();
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(start_flg == false){

            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }else{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }


        //フリック操作
        FrameLayout frame = findViewById(R.id.frame);
        float adjustX = 150.0f;
        float adjustY = 150.0f;

        FlickCheck flickCheck = new FlickCheck(frame, adjustX, adjustY) {

            @Override
            public void getFlick(int flickData) {
                switch (flickData) {
                    case FlickCheck.LEFT_FLICK:
                        // 左フリック
                        break;

                    case FlickCheck.RIGHT_FLICK:
                        // 右フリック
                        break;

                    case FlickCheck.UP_FLICK:
                        // 上フリック
                        action_flg = true;
                        break;

                    case FlickCheck.DOWN_FLICK:
                        // 下フリック
                        action_flg = false;
                        break;
                }
            }
        };
        return true;
    }

    public void stopAnime() {
        //結果画面へ
        Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
        intent.putExtra("SCORE",score);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
    }
}
