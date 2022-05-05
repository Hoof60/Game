import processing.core.PApplet;
import processing.core.PImage;

public class Level1 {

    Robot r;
    PImage background;
    PApplet app;

    public Level1( PApplet app) {
        this.app = app;
        r = new Robot(2000, 500, app);
        //background = app.loadImage("Assets/Level1Background.png");
        //background.resize(400, 150);
    }

    public void hitCheck(Ray hitscanRay) {
        //Check if player bullet hits enemies
        r.hitbox.get(0).show();
        if (hitscanRay.cast(r.hitbox.get(0)) != null){
            System.out.println("true");
        }
    }

    void draw(){
        //app.image(background, 0, 0);
        r.draw();
    }

}
