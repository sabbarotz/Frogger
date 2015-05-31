package winf114.waksh.de.frogger;

import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.util.Log;
import android.graphics.Rect;



/**
 * Created by bhaetsch on 25.05.2015.
 */
public class MainThread extends Thread {

    private volatile boolean running = false;
    private volatile boolean paused = false;
    private SurfaceHolder surfaceHolder;
    private GameActivity gameActivity;
    private Canvas canvas;

    protected long renderTimeBegin;
    protected long renderTime;
    protected long renderTimeMax;
    protected long renderTimeAvg;
    protected String renderTimeAvgStr;
    protected long renderTimeSum;
    protected int renderCycles;

    int zieleErreicht;
    boolean hitTree = false;

    public MainThread(SurfaceHolder surfaceHolder, GameActivity gameActivity) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameActivity = gameActivity;
        zieleErreicht = 0;
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

                                // gamecycle messung
                                renderTimeBegin = System.currentTimeMillis();

                                // 20 cycles wird der tote frosch angezeigt
                                if (renderCycles % 20 == 0){
                                  gameActivity.frosch.istTot = false;
                                }

                                // wenn 5 ziele gefüllt sind wird das spiel zurück gesetzt
                                if (zieleErreicht == 5){
                                    gameActivity.punkte+=500;
                                    for (Spielobjekt s : gameActivity.spielobjekte){
                                        if(s instanceof Ziel){
                                            ((Ziel) s).setBesetzt(false);
                                            zieleErreicht = 0;
                                        }
                                    }
                                }

                                // alles bewegen
                                for (Spielobjekt s : gameActivity.spielobjekte) {
                                    s.move();

                                }

                                // Kol Spielobjekt mit Rand
                                Rect erweiterteSpielFlaeche = new Rect(gameActivity.spielFlaeche.left-500,gameActivity.spielFlaeche.top,gameActivity.spielFlaeche.right+500,gameActivity.spielFlaeche.bottom);
                                for (Spielobjekt s : gameActivity.spielobjekte) {
                                    if (s instanceof Hindernis) {
                                        if (!s.kollidiertMit(erweiterteSpielFlaeche)) {
                                            ((Hindernis) s).erscheintWieder();
                                        }
                                    }
                                }

                                // Kol Frosch mit Spielobjekt
                                for (Spielobjekt s : gameActivity.spielobjekte) {

                                    // mit hindernis
                                    if (s instanceof Hindernis) {

                                        //nicht im wasser(auto)
                                        if (!gameActivity.frosch.imWasser) {
                                            if (gameActivity.frosch.kollidiertMit(s.getZeichenBereich())) {
                                                gameActivity.testText = "hit car";
                                                gameActivity.frosch.sterben();

                                            }
                                        }

                                        // im wasser (baumstamm)
                                        if (gameActivity.frosch.imWasser) {
                                            if (gameActivity.frosch.kollidiertMit(s.getZeichenBereich())) {
                                                gameActivity.testText = "hit tree";
                                                hitTree = true;
                                            }
                                        }
                                    }



                                    // mit Ziel
                                    if(s instanceof Ziel){
                                        // unbesetzt
                                        if (gameActivity.frosch.kollidiertMit(s.getZeichenBereich()) && !((Ziel) s).isBesetzt()) {
                                            zieleErreicht++;
                                            ((Ziel) s).setBesetzt(true);
                                            gameActivity.frosch.gewinnt();
                                        }
                                        // besetzt
                                        if(gameActivity.frosch.kollidiertMit(s.getZeichenBereich()) && ((Ziel) s).isBesetzt()){
                                            gameActivity.frosch.sterben();
                                        }
                                    }
                                }


                                // Kol Frosch mit Rand
                                if (!gameActivity.frosch.kollidiertMit(gameActivity.spielFlaeche)) {
                                    gameActivity.frosch.sterben();
                                }

                                renderCycles++;
                                if (renderTime > renderTimeMax){
                                    renderTimeMax = renderTime;}
                                renderTime = System.currentTimeMillis()- renderTimeBegin;
                                renderTimeSum = renderTimeSum + renderTime;
                                if(renderCycles == 20){
                                    renderTimeAvg = renderTimeSum / renderCycles;
                                    renderTimeAvgStr = " | " + renderTimeAvg;
                                    renderCycles = 0;
                                    renderTimeSum = 0;
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
