package winf114.waksh.de.frogger;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;


public class GameActivity extends Activity implements SurfaceHolder.Callback{

    final int LANE_HOEHE_PROZENT = 6;       //Höhe einer "Lane" im Spiel in % des Screens
    final int OBJEKT_HOEHE_PROZENT = 80;    //Höhe des Objekts in % der Lane Hoehe

    int lanePixelHoehe;             //Höhe einer "Lane" im Spiel in Pixeln
    private int objektPixelHoehe;           //Höhe der Objekt (eg.Frosch) im Spiel in Pixeln
    Rect spielFlaeche;
    int lanePadding;
    int froschbreite;
    int froschSpeed;
    int startPositionX;
    int startPositionY;

    Frosch frosch;
    private Hindernis auto01;
    private Hindernis auto02;
    private Hindernis auto03;
    private Hindernis auto04;
    private Hindernis auto05;
    private Hindernis baum01;
    private Hindernis baum02;
    private Hindernis baum03;
    private Hindernis baum04;
    private Hindernis baum05;
    private Ziel ziel01;
    private Ziel ziel02;
    private Ziel ziel03;
    private Ziel ziel04;
    private Ziel ziel05;

    protected ArrayList<Spielobjekt> spielobjekte;


    TextView textView;
    private Hintergrund hintergrund;

    private MainThread mainThread;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean isSurfaceCreated = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        spielFlaeche = new Rect(0,0,0,0);

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

        String breiteSTR = Integer.toString(width);
        String hoeheSTR = Integer.toString(height);

        textView.setText(hoeheSTR + ":" + breiteSTR + ":" + froschbreite + ":" + froschSpeed);

        hintergrund = new Hintergrund(width, lanePixelHoehe);

        spielobjekte = new ArrayList<Spielobjekt>();

        spielobjekte.add(frosch = new Frosch(startPositionX, startPositionY, froschbreite , objektPixelHoehe, (lanePixelHoehe - objektPixelHoehe),froschSpeed, Color.parseColor("#9db426"), this));


        // xx = XX(linker rand, lane+(zentriert in lane),breiteObjekt,hoeheObjekt, geschw., farbe)
        spielobjekte.add(baum01 = new Hindernis(-200, lanePixelHoehe * 1 + lanePadding, 200, objektPixelHoehe, 4, Color.parseColor("#c19132"), this));
        spielobjekte.add(baum02 = new Hindernis(-400, lanePixelHoehe * 2 + lanePadding, 400, objektPixelHoehe, -2, Color.parseColor("#c19132"), this));
        spielobjekte.add(baum03 = new Hindernis(-250, lanePixelHoehe * 3 + lanePadding, 250, objektPixelHoehe, 3, Color.parseColor("#c19132"), this));
        spielobjekte.add(baum04 = new Hindernis(-100, lanePixelHoehe * 4 + lanePadding, 100, objektPixelHoehe, -5, Color.parseColor("#c19132"), this));
        spielobjekte.add(baum05 = new Hindernis(-150, lanePixelHoehe * 5 + lanePadding, 400, objektPixelHoehe, 2, Color.parseColor("#c19132"), this));

        spielobjekte.add(auto01 = new Hindernis(-200, lanePixelHoehe * 7 + lanePadding, 200, objektPixelHoehe, 4, Color.parseColor("#750707"), this));
        spielobjekte.add(auto02 = new Hindernis(-400, lanePixelHoehe * 8 + lanePadding, 400, objektPixelHoehe, -2, Color.parseColor("#750707"), this));
        spielobjekte.add(auto03 = new Hindernis(-250, lanePixelHoehe * 9 + lanePadding, 250, objektPixelHoehe, 3, Color.parseColor("#750707"), this));
        spielobjekte.add(auto04 = new Hindernis(-100, lanePixelHoehe * 10 + lanePadding, 100, objektPixelHoehe, -5, Color.parseColor("#750707"), this));
        spielobjekte.add(auto05 = new Hindernis(-150, lanePixelHoehe * 11 + lanePadding, 150, objektPixelHoehe, 4, Color.parseColor("#750707"), this));

        spielobjekte.add(ziel01 = new Ziel(startPositionX, 0, froschbreite, lanePixelHoehe, Color.parseColor("#000000")));
        spielobjekte.add(ziel02 = new Ziel(startPositionX + (3 * froschbreite), 0, froschbreite, lanePixelHoehe, Color.parseColor("#000000")));
        spielobjekte.add(ziel03 = new Ziel(startPositionX - (3 * froschbreite), 0, froschbreite, lanePixelHoehe, Color.parseColor("#000000")));
        spielobjekte.add(ziel04 = new Ziel(startPositionX + (6 * froschbreite), 0, froschbreite, lanePixelHoehe, Color.parseColor("#000000")));
        spielobjekte.add(ziel05 = new Ziel(startPositionX - (6 * froschbreite), 0, froschbreite, lanePixelHoehe, Color.parseColor("#000000")));

        // frosch geschwindigkeit abhaengig von lanehoehe


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

        // 4 Knöpfe und ein Test-Textfeld

        textView = (TextView) findViewById(R.id.textView1);

        Button linksButton = (Button) findViewById(R.id.links);
        linksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Links");
                frosch.setMoved();
                frosch.setRichtung(richtung.links);
            }
        });

        Button rechtsButton = (Button) findViewById(R.id.rechts);
        rechtsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Rechts");
                frosch.setMoved();
                frosch.setRichtung(richtung.rechts);
            }
        });

        Button untenButton = (Button) findViewById(R.id.unten);
        untenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Unten");
                frosch.setMoved();
                frosch.setRichtung(richtung.zurueck);
            }
        });

        Button obenButton = (Button) findViewById(R.id.oben);
        obenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Oben");
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
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        hintergrund.draw(canvas);
        for(Spielobjekt s : spielobjekte){
            s.draw(canvas);
        }
    }

    public Rect getSpielFlaeche(){
        return spielFlaeche;
    }

    private void erstelleSpielParameter(int width, int height){
        this.lanePixelHoehe = height * LANE_HOEHE_PROZENT / 100;
        this.objektPixelHoehe = lanePixelHoehe * OBJEKT_HOEHE_PROZENT / 100;
        lanePadding = (lanePixelHoehe - objektPixelHoehe)/2; //zentriert die Obj in den Lanes
        spielFlaeche.set(0,0,width,height * LANE_HOEHE_PROZENT / 100 * 13);
        froschbreite = width / 15;
        froschSpeed = froschbreite;
        startPositionX = width / 2 - (froschbreite/2);
        startPositionY = lanePixelHoehe * 12 + lanePadding;
    }
}
