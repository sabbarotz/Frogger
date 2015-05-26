package winf114.waksh.de.frogger;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Matzef on 25.05.2015.
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread mainThread;
    private Frosch frosch;
    Paint p = new Paint();
    Color c = new Color();


public MainGamePanel(Context context) {
    super(context);

    // adding the callback (this) to the surface holder to intercept events
    getHolder().addCallback(this);

    // make the GamePanel focusable so it can handle events
    setFocusable(true);

    // mainThread = new MainThread(getHolder(), this);
    frosch = new Frosch(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 400,1400,10,10);
    }

    @Override
public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
        }

    @Override
public void surfaceDestroyed(SurfaceHolder holder) {
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        }

    @Override
public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the frosch
            frosch.handleActionDown((int)event.getX(), (int)event.getY());
            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                mainThread.setRunning(false);
                ((GameActivity)getContext()).finish();
            } else {
            }

        } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (frosch.isTouched()) {
                // the frosch was picked up and is being dragged
                frosch.setX((int)event.getX());
                frosch.setY((int)event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            if (frosch.isTouched()) {
                frosch.setTouched(false);
            }
        }
        return true;
    }

    @Override
protected void onDraw(Canvas canvas) {
        // fills the canvas with black
        canvas.drawColor(Color.parseColor("#0e2f44"));
        p.setColor(0xffffffff);
        canvas.drawLine(0, getHeight() - 50, getWidth(), getHeight() - 50, p);
        canvas.drawText("Ende", 0, getHeight(), p);
        frosch.draw(canvas);
    }
}