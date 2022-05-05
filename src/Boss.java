import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Boss {
    PVector position;
    PApplet app;
    PImage face1;
    PImage face2;
    boolean speaking;
    boolean faceToggle;
    int faceCounter;

    public Boss(PApplet app) {
        this.app = app;
        speaking = true;
        face1 = app.loadImage("Assets/Boss1.png");
        face2 = app.loadImage("Assets/boss2.png");
        face1.resize(200, 200);
        face2.resize(200, 200);
    }

    void draw() {
        if (faceCounter <= 0) {
            faceToggle = !faceToggle;
            faceCounter = 3;
        } else {
            faceCounter--;
        }

        if (faceToggle || !speaking) {
            app.image(face1, 0, 0);
        } else {
            app.image(face2, 0, 0);
        }
    }

}
