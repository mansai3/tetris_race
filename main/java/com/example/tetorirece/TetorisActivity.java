package com.example.tetorirece;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class TetorisActivity extends AppCompatActivity {
    int gameover = 0;
    private class FieldView extends SurfaceView {



        Random mRand = new Random(System.currentTimeMillis());


        int[][][] blocks = {     //ブロックの登録
                {//Lブロック
                        {1,0},
                        {1,0},
                        {1,1}
                },
                {//Jブロック
                        {0,1},
                        {0,1},
                        {1,1}
                },
                {//Oブロック
                        {1,1},
                        {1,1}
                },
                {//Tブロック
                        {1,0},
                        {1,1},
                        {1,0}
                },
                {//Zブロック
                        {1,0},
                        {1,1},
                        {0,1}
                },
                {//Sブロック
                        {0,1},
                        {1,1},
                        {1,0}
                },
                {//Iブロック
                        {1},
                        {1},
                        {1},
                        {1}
                }
        };

        int[][] block = blocks[mRand.nextInt(blocks.length)];
        int posx, posy;
        int mapWidth  = 10;    //マップの横の大きさ
        int mapHeight = 20;    //マップの縦の大きさ
        int[][] map = new int[mapHeight][];

        int score = 0;
        int tokuten = 0;


        public FieldView(Context context) {
            super(context);

            setBackgroundColor(0xFFFFFFFF);
            setFocusable(true);    //タッチモードとキーパッドモードの切り替え
            setFocusableInTouchMode(true);    //タッチモードのみでのビューのフォーカスイベント
            requestFocus();

        }

        public void initGame() {
            for (int y = 0; y < mapHeight; y++) {
                map[y] = new int[mapWidth];
                for (int x = 0; x < mapWidth; x++) {
                    map[y][x] = 0;
                }
            }
        }

        private void paintMatrix(Canvas canvas, int[][] matrix, int offsetx, int offsety, int color) {
            ShapeDrawable rect = new ShapeDrawable(new RectShape());
            rect.getPaint().setColor(color);
            int h = matrix.length;
            int w = matrix[0].length;

            for (int y = 0; y < h; y ++) {
                for (int x = 0; x < w; x ++) {
                    if (matrix[y][x] != 0) {    //1ブロックの大きさの変更
                        int px = (x + offsetx) * 96;
                        int py = (y + offsety) * 96;
                        rect.setBounds(px, py, px + 96, py + 96);
                        rect.draw(canvas);
                    }
                }
            }
        }


        boolean check(int[][] block, int offsetx, int offsety) {    //ブロックの移動範囲をマップの大きさ内に設定
            if (offsetx < 0 || offsety < 0 ||
                    mapHeight < offsety + block.length ||
                    mapWidth < offsetx + block[0].length) {
                return false;
            }
            for (int y = 0; y < block.length; y ++) {
                for (int x = 0; x < block[y].length; x ++) {
                    if (block[y][x] != 0 && map[y + offsety][x + offsetx] != 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        void mergeMatrix(int[][] block, int offsetx, int offsety) {
            for (int y = 0; y < block.length; y ++) {
                for (int x = 0; x < block[0].length; x ++) {
                    if (block[y][x] != 0) {
                        map[offsety + y][offsetx + x] = block[y][x];
                    }
                }
            }
        }

        void clearRows() {
            // 埋まった行は消す。nullで一旦マーキング
            for (int y = 0; y < mapHeight; y ++) {
                boolean full = true;
                for (int x = 0; x < mapWidth; x ++) {
                    if (map[y][x] == 0) {
                        full = false;
                        break;
                    }
                }

                if (full) map[y] = null;
            }

            // 新しいmapにnull以外の行を詰めてコピーする
            int[][] newMap = new int[mapHeight][];
            int y2 = mapHeight - 1;
            for (int y = mapHeight - 1; y >= 0; y--) {
                if (map[y] == null) {
                    continue;
                } else {
                    newMap[y2--] = map[y];
                }
            }

            // 消えた行数分新しい行を追加する
            for (int i = 0; i <= y2; i++) {
                int[] newRow = new int[mapWidth];
                for (int j = 0; j < mapWidth; j ++) {
                    newRow[j] = 0;
                    tokuten += 10;
                }
                newMap[i] = newRow;
            }
            map = newMap;
        }

        /**
         * Draws the 2D layer.
         */
        @Override
        //画面上に黒の枠線を作成する
        protected void onDraw(Canvas canvas) {
            ShapeDrawable rect = new ShapeDrawable(new RectShape());
            rect.setBounds(0, 0, 970, 1930);
            rect.getPaint().setColor(0xFF000000);
            rect.draw(canvas);
            canvas.translate(5, 5);
            rect.setBounds(0, 0, 960, 1920);
            rect.getPaint().setColor(0xFFFFFFFF);
            rect.draw(canvas);

            paintMatrix(canvas, block, posx, posy, 0xFFFF0000);
            paintMatrix(canvas, map, 0, 0, 0xFF808080);

            Paint textView = new Paint();
            textView.setColor(Color.RED);
            textView.setTextSize(50);


            Paint scoretext = new Paint();
            scoretext.setColor(Color.BLACK);
            scoretext.setTextSize(250);

            Paint scoretext2 = new Paint();
            scoretext2.setColor(Color.BLACK);
            scoretext2.setTextSize(250);

            Paint scoretext3 = new Paint();
            scoretext3.setColor(Color.BLACK);
            scoretext3.setTextSize(250);

            Paint scoretext4 = new Paint();
            scoretext4.setColor(Color.BLACK);
            scoretext4.setTextSize(100);


            canvas.drawText("Time : "+String.valueOf(count),1000,80,textView);
            canvas.drawText("Score : "+String.valueOf(tokuten),1000,150,textView);
            canvas.drawText(" →  ",500,2100,scoretext);
            canvas.drawText(" ↓ " ,260,2230,scoretext2);
            canvas.drawText(" ← " ,0,2100,scoretext3);
            canvas.drawText(" 回転 " ,1000,2100,scoretext4);


            if(count == 120){
                mainTimer.cancel();
                stopAnime();
            }

        }

        int[][] rotate(final int[][] block) {    //回転
            int[][] rotated = new int[block[0].length][];
            for (int x = 0; x < block[0].length; x ++) {
                rotated[x] = new int[block.length];
                for (int y = 0; y < block.length; y ++) {
                    rotated[x][block.length - y - 1] = block[y][x];
                }
            }
            return rotated;
        }



        @Override
        //ボタンでの操作
        public boolean onTouchEvent(MotionEvent event) {

            float x1 = event.getX();
            float y1 = event.getY();

            System.out.println(x1+y1);

            switch (event.getAction()) {

                case (MotionEvent.ACTION_DOWN):

                    if (x1 >= 1000 && x1 <= 1300 && y1 >= 2010 && y1 <= 2130) {
                        int[][] newBlock = rotate(block);
                        if (check(newBlock, posx, posy)) {
                            block = newBlock;
                        }
                    } else if (x1 >= 500 && x1 <= 750 && y1 >= 1950 && y1 <= 2070) {
                        if (check(block, posx + 1, posy)) {
                            posx = posx + 1;
                        }
                    } else if (x1 >= 130 && x1 <= 320 && y1 >= 1950 && y1 <= 2070) {

                        if (check(block, posx - 1, posy)) {
                            posx = posx - 1;
                        }

                    } else if (x1 >= 340 && x1 <= 480 && y1 >= 2060 && y1 <= 2260) {

                        int y = posy;
                        while (check(block, posx, y)) {
                            y++;
                        }
                        if (y > 0) posy = y - 1;

                    }
            }
//            mHandler.sendEmptyMessage(INVALIDATE);
            return true;
        }


        public void startAnime() {
            mHandler.sendEmptyMessage(INVALIDATE);
            mHandler.sendEmptyMessage(DROPBLOCK);
        }

        public void stopAnime() {
            mHandler.removeMessages(INVALIDATE);
            mHandler.removeMessages(DROPBLOCK);

//            if(gameover == 1){
//結果画面へ
            Intent intent = new Intent(getApplicationContext(), MiddleActivity.class);
            intent.putExtra("tokuten", tokuten);
            startActivity(intent);
//        }

        }

        private static final int INVALIDATE = 1;
        private static final int DROPBLOCK = 2;


        /**
         * Controls the animation using the message queue. Every time we receive an
         * INVALIDATE message, we redraw and place another message in the queue.
         */
        private final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case INVALIDATE:
                        invalidate();
                        break;
                    case DROPBLOCK:

                        if (check(block, posx, posy + 1)) {
                            posy++;//ブロックが落ちていくのをカウント
                        } else {
                            //一個のブロックを置き終わったことを感知
                            mergeMatrix(block, posx, posy);
                            clearRows();
                            posx = 0; posy = 0;
                            block = blocks[mRand.nextInt(blocks.length)];

                            //以下を追加
                            if((check(block,posx,posy+1)) == false){
                                stopAnime();
//                                gameover = 1;
                            }
                        }

                        invalidate();
                        Message massage = new Message();
                        massage.what = DROPBLOCK;
                        sendMessageDelayed(massage, 500);
                        break;
                }
            }
        };
    }

    FieldView mFieldView;

    private void setFieldView() {
        if (mFieldView == null) {
            mFieldView = new FieldView(getApplication());
            setContentView(mFieldView);
        }
    }

//    @Override
//    protected void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        setFieldView();
        mFieldView.initGame();
        mFieldView.startAnime();
        Looper.myQueue().addIdleHandler(new Idler());
    }


    @Override
    protected void onPause() {
        super.onPause();
        mFieldView.stopAnime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFieldView.stopAnime();
    }

    @Override
    public void onBackPressed() {
    }


    // Allow the activity to go idle before its animation starts
    class Idler implements MessageQueue.IdleHandler {
        public Idler() {
            super();
        }

        public final boolean queueIdle() {
            return false;
        }
    }


    //制限時間の処理
    private Timer mainTimer;					//タイマー用
    private MainTimerTask mainTimerTask;		//タイマタスククラス
    private TextView countText;					//テキストビュー
    private int count = 0;						//カウント
    private Handler mHandler = new Handler();   //UI Threadへのpost用ハンドラ

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_tetoris);

        //タイマーインスタンス生成
        this.mainTimer = new Timer();
        //タスククラスインスタンス生成
        this.mainTimerTask = new MainTimerTask();
        //タイマースケジュール設定＆開始
        this.mainTimer.schedule(mainTimerTask, 1000,1000);

    }

    /**
     * タイマータスク派生クラス
     * run()に定周期で処理したい内容を記述
     *
     */
    public class MainTimerTask extends TimerTask {
        @Override
        public void run() {
            //ここに定周期で実行したい処理を記述します
            mHandler.post( new Runnable() {
                public void run() {

                    //実行間隔分を加算処理
                    count += 1;
                    //画面にカウントを表示
//                    countText.setText(String.valueOf(count));
                }
            });
        }


    }


}