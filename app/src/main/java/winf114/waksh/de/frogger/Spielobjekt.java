package winf114.waksh.de.frogger;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public abstract class Spielobjekt {
    private int x;                  //horizontale Position der linken oberen Ecke
    private int y;                  //vertikale Position der linken oberen Ecke
    private int breite;             //in Pixeln
    private int hoehe;              //in Pixeln
    private int geschwindigkeit;    //negativ==links; positiv==rechts, 0==statisch

    public Spielobjekt(int x, int y, int breite, int hoehe, int geschwindigkeit) {
        this.setX(x);
        this.setY(y);
        this.setBreite(breite);
        this.setHoehe(hoehe);
        this.setGeschwindigkeit(geschwindigkeit);
    }

    public void move() {
        this.setX(this.x + this.geschwindigkeit);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBreite() {
        return breite;
    }

    public void setBreite(int breite) {
        this.breite = breite;
    }

    public int getHoehe() {
        return hoehe;
    }

    public void setHoehe(int hoehe) {
        this.hoehe = hoehe;
    }

    public int getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(int geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }
}
