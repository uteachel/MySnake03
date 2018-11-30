package com.example.user.smile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    Bitmap pic, ballon, block;
    float dx, dy, ddx, ddy;

    Display display;
    int width, height;
    int sp;//speed

    int nBlocks;
    Point lastP, firstP;

    ArrayList<Point> arrBlocks;

    Random rnd;
    Point ptouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        rnd = new Random();
        sp = 5;
        nBlocks = 3;
        display = getWindowManager().getDefaultDisplay();
        pic = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        ballon = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        block = BitmapFactory.decodeResource(getResources(), R.drawable.block);
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        dx = rnd.nextInt(width - ballon.getHeight());
        dy = rnd.nextInt(height - ballon.getHeight());
        firstP = new Point((int) dx, (int) dy);
        ptouch = firstP;
        ddx = 4;
        ddy = 4;


        arrBlocks = new ArrayList<>();


        lastP = firstP;

        for (int i = 0; i < nBlocks; i++) {
            Point ap = new Point(rnd.nextInt(width - block.getWidth()), rnd.nextInt(width - block.getHeight()));
            arrBlocks.add(ap);

        }

        DrawView myView = new DrawView(this);
        myView.setOnTouchListener(this);
        setContentView(myView);


    }

    void restart() {
        nBlocks += 3;
        dx = rnd.nextInt(width - ballon.getHeight());
        dy = rnd.nextInt(height - ballon.getHeight());
        firstP = new Point((int) dx, (int) dy);
        ptouch = firstP;
        ddx = 4;
        ddy = 4;

        arrBlocks = new ArrayList<>();


        lastP =    (new Point((int) (dx + ddx *  sp), (int) (dy + ddy *  sp)));


        for (int i = 0; i < nBlocks; i++) {
            Point ap = new Point(rnd.nextInt(width - block.getWidth()), rnd.nextInt(width - block.getHeight()));
            arrBlocks.add(ap);

        }
    }

    void isCollision() {
        boolean ok;
        if (arrBlocks.size() == 0) restart();
        for (int i = 0; i < arrBlocks.size(); i++) {
            ok = Math.abs(arrBlocks.get(i).x - dx) < block.getWidth() / 2 && Math.abs(arrBlocks.get(i).y - dy) < block.getHeight() / 2;
            if (ok) {
                lastP = new Point(arrBlocks.get(i));
                arrBlocks.remove(i);

            }

        }


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            float px = motionEvent.getX();
            float py = motionEvent.getY();
            ptouch = new Point((int) px, (int) py);
            if (dx < px) dx += sp * ddx;
            if (dx > px) dx -= sp * ddx;
            if (dy > py) dy -= sp * ddy;
            if (dy < py) dy += sp * ddy;
            lastP.set((int) px, (int) py);
            moveBallon(lastP);

        }
        return true;
    }

    private void moveBallon(Point t) {
        Point p = t;

            int px = lastP.x;
            int py = lastP.y;

            if (px < t.x) p = new Point((int) (px + sp * ddx), py);
            if (px > t.x) p = new Point((int) (px - sp * ddx), py);
            if (py > t.y) p = new Point(px, (int) (py - sp * ddy));
            if (px < t.x) p = new Point(px, (int) (px + sp * ddy));
            lastP = p;


          }

    private class DrawView extends View {
        Paint p;
        Random rnd;

        public DrawView(Context context) {
            super(context);
            rnd = new Random();
            p = new Paint();
            //size of display
            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
            System.out.println("width,height=" + width + " " + height);


        }


        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(pic, 0, 0, p);
            p.setColor(Color.WHITE);
            p.setTextSize(100);
            canvas.drawText("Level: " + nBlocks / 3, 40, 120, p);
            dx = lastP.x;
            dy = lastP.y;
            dx += sp * ddx;
            dy += sp * ddy;
            lastP.set((int) dx, (int) dy);

            if (dx > (float) (width - ballon.getHeight()) || (dx < 0)) {
                ddx = -ddx;
            }
            if (dy > (float) (height - ballon.getHeight()) || (dy < 0)) {
                ddy = -ddy;
            }
            //draw apples
            for (int i = 0; i < arrBlocks.size(); i++) {

                canvas.drawBitmap(block, arrBlocks.get(i).x, arrBlocks.get(i).y, p);

            }

            moveBallon(ptouch);
            isCollision();
            // draw snake


                    canvas.drawBitmap(ballon, lastP.x - ballon.getHeight() / 2, lastP.y - ballon.getHeight() / 2, p);


            //call onDraw again

            invalidate();
        }


    }

}
