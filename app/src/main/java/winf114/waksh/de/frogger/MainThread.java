package winf114.waksh.de.frogger;

import java.util.ArrayList;

/**
 * Created by bhaetsch on 25.05.2015.
 */
public class MainThread extends Thread {
    private boolean running;
    private ArrayList<Spielobjekt> spielobjekte = new ArrayList<Spielobjekt>();

    @Override
    public void run(){
        while(isRunning()) {
            //TODO
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
