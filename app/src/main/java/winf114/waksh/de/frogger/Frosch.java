package winf114.waksh.de.frogger;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public class Frosch {
    private int x;
    private int y;
    private int breite;
    private int hoehe;

    public Frosch(int x, int y, int breite, int hoehe) {
        this.setX(x);
        this.setY(y);
        this.setBreite(breite);
        this.setHoehe(hoehe);
    }

    public void move(richtung r) {
        switch(r){
            case vor:
                this.setY(this.getY()-this.getHoehe());
                break;
            case zurueck:
                this.setY(this.getY() + this.getHoehe());
                break;
            case links:
                this.setX(this.getX()-this.getBreite());
                break;
            case rechts:
                this.setX(this.getX()+this.getBreite());
                break;
        }
    }

    public boolean collidesWithHindernis(Hindernis h) {
        if (h.getY() > this.getY() + this.getHoehe() || this.getY() > h.getY() + h.getHoehe() || h.getX() > this.getX() + this.getBreite() || this.getX() > h.getX() + h.getBreite()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean collidesWithSchwimmer(Schwimmer s) {
        if (s.getY() > this.getY() + this.getHoehe() || this.getY() > s.getY() + s.getHoehe() || s.getX() > this.getX() + this.getBreite() || this.getX() > s.getX() + s.getBreite()) {
            return false;
        } else {
            return true;
        }
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
}