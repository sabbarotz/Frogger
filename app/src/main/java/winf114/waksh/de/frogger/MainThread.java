package winf114.waksh.de.frogger;

import java.util.ArrayList;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.util.Log;



/**
 * Created by bhaetsch on 25.05.2015.
 */
public class MainThread extends Thread {

    private volatile boolean running = false;
    private volatile boolean paused = false;
    private SurfaceHolder surfaceHolder;
    private GameActivity gameActivity;
    private Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameActivity gameActivity) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameActivity = gameActivity;
    }

    @Override
    public void run() {
        while (isRunning()) {

            while (running) {
                if (paused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //Exception
                    }
                } else {
                    canvas = null;
                    // try locking the canvas for exclusive pixel editing on the surface
                        try {
                            while (!gameActivity.isSurfaceCreated) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    //Exception
                                }
                            }
                            canvas = this.surfaceHolder.lockCanvas();
                            synchronized (surfaceHolder) {
                                // update game state


                                for (Spielobjekt s : gameActivity.spielobjekte) {
                                    s.move();

                                }

                                // Kol Spielobjekt mit Rand
                                for (Spielobjekt s : gameActivity.spielobjekte) {
                                    if (s instanceof Hindernis) {
                                        if (!s.kollidiertMit(gameActivity.spielFlaeche)) {
                                            ((Hindernis) s).erscheintWieder();
                                        }
                                    }
                                }

                                // Kol Frosch mit Hindernis
                                for (Spielobjekt s : gameActivity.spielobjekte) {
                                    if (s instanceof Hindernis) {
                                        if (!gameActivity.frosch.imWasser)
                                            if (gameActivity.frosch.kollidiertMit(s.getZeichenBereich())) {
                                                gameActivity.frosch.sterben();
                                            }
                                        else if (gameActivity.frosch.imWasser){
                                            if (!gameActivity.frosch.kollidiertMit(s.getZeichenBereich())) {
                                                gameActivity.frosch.sterben();
                                            }
                                        }
                                    }
                                }
                                // Kol Frosch mit Rand
                                if (!gameActivity.frosch.kollidiertMit(gameActivity.spielFlaeche)) {
                                    gameActivity.frosch.sterben();
                                }


                                // draws the canvas
                                this.gameActivity.onDraw(canvas);
                            }
                        } finally {
                            // in case of an exception the surface is not left in
                            // an inconsistent state
                            if (canvas != null) {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                        } // end finally
                    }
                }
            }
        }



    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


}
