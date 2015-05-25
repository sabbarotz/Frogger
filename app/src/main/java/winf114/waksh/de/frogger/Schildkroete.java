package winf114.waksh.de.frogger;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public class Schildkroete extends Schwimmer {
    boolean sichtbar;

    public Schildkroete(int x, int y, int breite, int hoehe, int geschwindigkeit, boolean sichtbar) {
        super(x, y, breite, hoehe, geschwindigkeit);
        this.sichtbar = sichtbar;
    }
}
