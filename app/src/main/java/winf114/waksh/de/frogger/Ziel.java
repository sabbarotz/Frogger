package winf114.waksh.de.frogger;

import android.graphics.Canvas;

/**
 * Created by Matzef on 30.05.2015.
 */
public class Ziel extends Spielobjekt {

    private boolean besetzt;

    public Ziel(int x, int y, int breite, int hoehe, int farbe) {
        super(x, y, breite, hoehe, farbe);
        besetzt = false;
    }

    public void move() {
        // Ziele bewegen sich nicht
    }
}

