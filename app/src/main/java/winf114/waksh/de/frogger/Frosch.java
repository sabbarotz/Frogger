package winf114.waksh.de.frogger;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public class Frosch extends Spielobjekt{

    int geschwindigkeitVertikal;
    int geschwindigkeitHorizontal;
    private boolean moved;
    private richtung r;
    private GameActivity gameActivity;
    boolean imWasser = false;


    public Frosch(int x, int y, int breite, int hoehe, int geschwindigkeitVertikal, int geschwindigkeitHorizontal,int farbe, GameActivity gameActivity) {
        super(x, y, breite, hoehe, farbe);

        this.gameActivity = gameActivity;
        this.geschwindigkeitHorizontal = geschwindigkeitHorizontal;
        this.geschwindigkeitVertikal = geschwindigkeitVertikal;
        moved = false;

    }

    public void move(){
        if(moved){
            move(r);
        }
    }

    public void move(richtung r) {
        switch(r){
            case vor:
                this.setY(this.getY() - this.getHoehe() - geschwindigkeitVertikal);
                if(this.getY() <= gameActivity.lanePixelHoehe * 6){
                    imWasser = true;
                }
                break;
            case zurueck:
                this.setY(this.getY() + this.getHoehe() + geschwindigkeitVertikal);
                if(this.getY() > gameActivity.lanePixelHoehe * 5){
                    imWasser = false;
                }
                break;
            //TODO int casten!?
            case links:
                this.setX(this.getX()- geschwindigkeitHorizontal);
                break;
            case rechts:
                this.setX(this.getX() + geschwindigkeitHorizontal);
                break;
        }
        setZeichenBereich();
        moved = false;
    }

    public void sterben(){
        this.setX(gameActivity.startPositionX);
        this.setY(gameActivity.startPositionY);
        this.imWasser = false;
        this.setZeichenBereich();
    }

    public void setMoved(){
        this.moved = true;
    }

    public void setRichtung(richtung r) {
        this.r = r;
    }
}