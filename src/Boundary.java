import processing.core.PApplet;
import processing.core.PVector;
// Daniel Shiffman
// https://thecodingtrain.com/CodingChallenges/145-2d-ray-casting.html
// https://youtu.be/TOEi6T2mtHo

// 2D Ray Casting
// https://editor.p5js.org/codingtrain/sketches/Nqsq3DFv-

    public class Boundary {
        PVector a, b;
        PApplet app;
        Boundary(float x1, float y1, float x2, float  y2, PApplet app) {
            this.a = new PVector(x1, y1);
            this.b = new PVector(x2, y2);
            this.app = app;
        }

        void show() {
            app.stroke(255, 0, 0);
            app.line(this.a.x, this.a.y, this.b.x, this.b.y);
            app.stroke(0);
        }
    }
