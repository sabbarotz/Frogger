package winf114.waksh.de.frogger;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public class Auto extends Hindernis {

    Paint paint = new Paint();
    Rect r = new Rect(getX(), getY(), 200, 50);
    int greenColorValue = Color.parseColor("#00ff00");


    public Auto(int x, int y, int breite, int hoehe, int geschwindigkeit) {
        super(x, y, breite, hoehe, geschwindigkeit);
        paint.setColor(greenColorValue);
    }

    public void draw(Canvas canvas) {
        r.set(getX(), getY(), getX() + 200, getY() + 50);
        canvas.drawRect(r, paint);
    }
}
