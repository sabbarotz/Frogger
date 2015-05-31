package winf114.waksh.de.frogger;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.SurfaceView;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.graphics.Paint;
import java.util.ArrayList;
import android.media.MediaPlayer;

public class GameActivity extends Activity implements SurfaceHolder.Callback{

    //TODO Felder einpacken
    private final int LANE_HOEHE_PROZENT = 5;       //Höhe einer "Lane" im Spiel in % des Screens
    private final int OBJEKT_HOEHE_PROZENT = 80;    //Höhe des Objekts in % der Lane Hoehe

    int lanePixelHoehe;                             //Höhe einer "Lane" im Spiel in Pixeln
    protected Rect spielFlaeche;
    protected Rect erweiterteSpielFlaeche;
    private int lanePadding;
    private int objektPixelHoehe;                   //Höhe der Objekt (eg.Frosch) im Spiel in Pixeln
    private int objektPixelBreite;
    protected int froschGeschwX;
    protected int froschGeschwY;
    protected int startPositionX;
    protected int startPositionY;
    private int smallTextSize;
    private int largeTextSize;

    public MediaPlayer musik;

    protected int punkte;
    protected LebensAnzeige lebensAnzeige;

    protected ArrayList<Spielobjekt> spielobjekte;
    Frosch frosch;
    Frosch deadFrosch;
    private Hindernis auto01;
    private Hindernis auto02;
    private Hindernis auto03;
    private Hindernis auto04;
    private Hindernis auto05;
    private Hindernis auto06;
    private Hindernis auto07;
    private Hindernis baum01;
    private Hindernis baum02;
    private Hindernis baum03;
    private Hindernis baum04;
    private Hindernis baum05;
    private Hindernis baum06;
    private Hindernis baum07;
    private Hindernis baum08;
    private Hindernis baum09;
    private Hindernis baum10;
    private Hindernis baum11;
    private Hindernis baum12;
    private Hindernis baum13;
    private Ziel ziel01;
    private Ziel ziel02;
    private Ziel ziel03;
    private Ziel ziel04;
    private Ziel ziel05;

    private long renderTimeBegin;
    private long renderTime;
    private long renderTimeMax;
    private long renderTimeAvg;
    private String renderTimeAvgStr;
    private long renderTimeSum;
    private int renderCycles;

    protected String testText;
    private Farbe farbe;
    private Paint textStift;
    private Hintergrund hintergrund;

    private MainThread mainThread;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    protected boolean isSurfaceCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        aktiviereImmersiveFullscreen();
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        musik = MediaPlayer.create(getApplicationContext(), R.drawable.froggertest);
        musik.setLooping(true);
        // musik.start();

        spielFlaeche = new Rect(0,0,0,0);
        punkte = 0;
        testText = "";

        farbe = new Farbe();
        textStift = new Paint();
        textStift.setColor(Farbe.text);

        mainThread = new MainThread(surfaceHolder, this);
    }

    // SurfaceView Methoden
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GameActivity", "surfaceCreated");
        isSurfaceCreated = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        erstelleSpielParameter(width, height);
        hintergrund = new Hintergrund(width, lanePixelHoehe);
        testText = height + ":" + width + ":" + objektPixelBreite + ":" + froschGeschwX;

        deadFrosch = new Frosch(0, 0, objektPixelBreite, objektPixelHoehe, 0, 0, Farbe.deadFrosch, this);
        lebensAnzeige = new LebensAnzeige(startPositionX + (objektPixelBreite /2), lanePixelHoehe * 13 + (objektPixelBreite * 60 / 100), objektPixelBreite * 60 / 100, objektPixelHoehe * 60 / 100, Farbe.frosch);

        spielobjekte = new ArrayList<Spielobjekt>();
        // xx = new XX(x, y(zentriert in lane), breiteObjekt, hoeheObjekt, geschwindigkeit, farbe, gameActivity)

        // LANE 2
        int hindernisBreite = objektPixelBreite * 3;
        int hindernisGeschw = 4;
        int lanePositionY = lanePixelHoehe * 1  + lanePadding;
        spielobjekte.add(baum01 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 7, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum08 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 14, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum09 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, 4, Farbe.baum, this));

        //LANE 3
        hindernisBreite = objektPixelBreite * 6;
        hindernisGeschw = -2;
        lanePositionY = lanePixelHoehe * 2  + lanePadding;
        spielobjekte.add(baum02 = new Hindernis(erweiterteSpielFlaeche.right + objektPixelBreite * 3, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));

        //LANE 4
        hindernisBreite = objektPixelBreite * 4;
        hindernisGeschw = 3;
        lanePositionY = lanePixelHoehe * 3  + lanePadding;
        spielobjekte.add(baum03 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, 3, Farbe.baum, this));
        spielobjekte.add(baum12 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 8, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum13 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite *16, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));

        //LANE 5
        hindernisBreite = objektPixelBreite * 5;
        hindernisGeschw = -2;
        lanePositionY = lanePixelHoehe * 4  + lanePadding;
        spielobjekte.add(baum04 = new Hindernis(erweiterteSpielFlaeche.right, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum10 = new Hindernis(erweiterteSpielFlaeche.right + objektPixelBreite * 10, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));

        //LANE 6
        hindernisBreite = objektPixelBreite * 3;
        hindernisGeschw = 2;
        lanePositionY = lanePixelHoehe * 5  + lanePadding;
        spielobjekte.add(baum05 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum06 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 7, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));
        spielobjekte.add(baum07 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 14, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.baum, this));

        //LANE 8
        hindernisBreite = objektPixelBreite * 2;
        hindernisGeschw = 4;
        lanePositionY = lanePixelHoehe * 7  + lanePadding;
        spielobjekte.add(auto01 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));

        //LANE 9
        hindernisBreite = objektPixelBreite * 4;
        hindernisGeschw = -2;
        lanePositionY = lanePixelHoehe * 8  + lanePadding;
        spielobjekte.add(auto02 = new Hindernis(erweiterteSpielFlaeche.right, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));

        //LANE 10
        hindernisBreite = objektPixelBreite * 3;
        hindernisGeschw = 3;
        lanePositionY = lanePixelHoehe * 9  + lanePadding;
        spielobjekte.add(auto03 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));

        //LANE 11
        hindernisBreite = objektPixelBreite * 2;
        hindernisGeschw = -5;
        lanePositionY = lanePixelHoehe * 10  + lanePadding;
        spielobjekte.add(auto04 = new Hindernis(erweiterteSpielFlaeche.right, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));
        spielobjekte.add(auto07 = new Hindernis(erweiterteSpielFlaeche.right + objektPixelBreite * 6, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));

        //LANE 12
        hindernisBreite = objektPixelBreite * 2;
        hindernisGeschw = 4;
        lanePositionY = lanePixelHoehe * 11  + lanePadding;
        spielobjekte.add(auto05 = new Hindernis(erweiterteSpielFlaeche.left, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));
        spielobjekte.add(auto06 = new Hindernis(erweiterteSpielFlaeche.left + objektPixelBreite * 5, lanePositionY, hindernisBreite, objektPixelHoehe, hindernisGeschw, Farbe.auto, this));

        //LANE 1 Ziele
        spielobjekte.add(ziel01 = new Ziel(startPositionX, 0, objektPixelBreite, lanePixelHoehe, Farbe.zielLeer));
        spielobjekte.add(ziel02 = new Ziel(startPositionX + (3 * objektPixelBreite), 0, objektPixelBreite, lanePixelHoehe, Farbe.zielLeer));
        spielobjekte.add(ziel03 = new Ziel(startPositionX - (3 * objektPixelBreite), 0, objektPixelBreite, lanePixelHoehe, Farbe.zielLeer));
        spielobjekte.add(ziel04 = new Ziel(startPositionX + (6 * objektPixelBreite), 0, objektPixelBreite, lanePixelHoehe, Farbe.zielLeer));
        spielobjekte.add(ziel05 = new Ziel(startPositionX - (6 * objektPixelBreite), 0, objektPixelBreite, lanePixelHoehe, Farbe.zielLeer));

        //Frosch
        spielobjekte.add(frosch = new Frosch(startPositionX, startPositionY, objektPixelBreite, objektPixelHoehe, froschGeschwY, froschGeschwX, Farbe.frosch, this));
    }

    @Override
    public void onResume() {
        Log.d("GameActivity", "onResume");

        super.onResume();

        if (!mainThread.isPaused()) {
            Log.d("GameActivity", "mainThread.start");
            mainThread.start();
        }
        mainThread.setRunning(true);
        mainThread.setPaused(false);

        // SteuerungsButtons
        Button linksButton = (Button) findViewById(R.id.links);
        linksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frosch.setMoved();
                frosch.setRichtung(richtung.links);
            }
        });

        Button rechtsButton = (Button) findViewById(R.id.rechts);
        rechtsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frosch.setMoved();
                frosch.setRichtung(richtung.rechts);
            }
        });

        Button untenButton = (Button) findViewById(R.id.unten);
        untenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frosch.setMoved();
                frosch.setRichtung(richtung.zurueck);
            }
        });

        Button obenButton = (Button) findViewById(R.id.oben);
        obenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                punkte += 10;
                frosch.setMoved();
                frosch.setRichtung(richtung.vor);
            }
        });
    }

    @Override
    public void onBackPressed() {
        mainThread.setRunning(false);
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        Log.d("GameActivity", "onPause");
        super.onPause();
        Log.d("GameActivity", "1");
        mainThread.setPaused(true);
        Log.d("GameActivity", "2");
        mainThread.setRunning(false);
        Log.d("GameActivity", "3");
        musik.stop();
    }

    @Override
    public void onRestart() {
        Log.d("GameActivity", "onRestart");
        super.onRestart();
    }

    @Override
    public void onStart() {
        Log.d("GameActivity", "onStart");
        super.onStart();
    }

    @Override
    public void onStop(){
        Log.d("GameActivity", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("GameActivity", "onDestroy");
        super.onDestroy();
        mainThread.setRunning(false);
        musik.stop();
    }

    protected void onDraw(Canvas canvas) {
        renderTimeBegin = System.currentTimeMillis();
        render(canvas);
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
    }

    private void erstelleSpielParameter(int width, int height){
        lanePixelHoehe = height * LANE_HOEHE_PROZENT / 100;
        objektPixelHoehe = lanePixelHoehe * OBJEKT_HOEHE_PROZENT / 100;
        lanePadding = (lanePixelHoehe - objektPixelHoehe)/2; //zentriert die Obj in den Lanes
        spielFlaeche.set(0, 0, width, height * LANE_HOEHE_PROZENT / 100 * 13);
        objektPixelBreite = width / 15;
        froschGeschwX = objektPixelBreite;
        froschGeschwY = lanePixelHoehe;
        startPositionX = width / 2 - (objektPixelBreite /2);
        startPositionY = lanePixelHoehe * 12 + lanePadding;
        smallTextSize = lanePixelHoehe/3;
        largeTextSize = objektPixelHoehe;
        erweiterteSpielFlaeche = new Rect(spielFlaeche.left - objektPixelBreite * 8,spielFlaeche.top,spielFlaeche.right + objektPixelBreite * 8,spielFlaeche.bottom);
    }

    private void render(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        hintergrund.draw(canvas);
        for(Spielobjekt s : spielobjekte){
            s.draw(canvas);
        }
        if(frosch.istTot){
            deadFrosch.draw(canvas);
        }
        textStift.setTextSize(smallTextSize);
        canvas.drawText("GCmax|avg: " + mainThread.gameCycleTimeMax + mainThread.gameCycleTimeAvgStr + " (ms)", 10, lanePixelHoehe * 15, textStift);
        canvas.drawText("RCmax|avg: " + renderTimeMax + renderTimeAvgStr + " (ms)", 10, lanePixelHoehe * 15 - (lanePixelHoehe / 2), textStift);
        canvas.drawText("imWasser: " + frosch.imWasser, startPositionX + (objektPixelBreite /2), lanePixelHoehe * 15  - (lanePixelHoehe / 2), textStift);
        canvas.drawText(testText, startPositionX + (objektPixelBreite / 2), lanePixelHoehe * 15, textStift);
        textStift.setTextSize(largeTextSize);
        canvas.drawText("Punkte: " + punkte, 10, lanePixelHoehe * 14, textStift);
        lebensAnzeige.draw(canvas);
    }

    private void aktiviereImmersiveFullscreen(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
