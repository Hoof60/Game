import processing.core.PApplet;
import processing.core.PVector;

// Daniel Shiffman
// https://thecodingtrain.com/CodingChallenges/145-2d-ray-casting.html
// https://youtu.be/TOEi6T2mtHo

// 2D Ray Casting
// https://editor.p5js.org/codingtrain/sketches/Nqsq3DFv-

class Ray {
    PVector pos, dir;
    PApplet app;

    Ray(PVector _pos, float angle, PApplet app) {
        this.pos = _pos;
        this.dir = PVector.fromAngle(angle);
        this.app = app;
    }

    void lookAt(float x, float y) {
        this.dir.x = x - this.pos.x;
        this.dir.y = y - this.pos.y;
        this.dir.normalize();
    }

    void show() {
        app.stroke(255);
        app.push();
        app.translate(this.pos.x, this.pos.y);
        app.line(0, 0, this.dir.x * 10, this.dir.y * 10);
        app.pop();
    }

    //, PVector target
    PVector cast(Boundary wall) {
        float x1 = wall.a.x;
        float y1 = wall.a.y;
        float x2 = wall.b.x;
        float y2 = wall.b.y;

        float x3 = this.pos.x;
        float y3 = this.pos.y;
        float x4 = this.pos.x + this.dir.x;
        float y4 = this.pos.y + this.dir.y;

        float den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return null;
        }

        float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
        float u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

//        double distanceToTarget = Math.sqrt(Math.pow(x3 - target.x,2) + Math.pow(y3 - target.y,2));
        if (t > 0 && t < 1 && u > 0) {

            PVector pt = new PVector();
            pt.x = x1 + t * (x2 - x1);
            pt.y = y1 + t * (y2 - y1);

//            boolean behindPlayer = false;
//
//            if(x3 > target.x){ // target is to the left
//                if (pt.x < target.x){
//                    behindPlayer = true;
//                }
//            }
//            if(x3 <= target.x){ // target is to the right
//                if (pt.x > target.x){
//                    behindPlayer = true;
//                }
//            }
//            if(y3 > target.y){ // target is above
//                if (pt.y < target.y){
//                    behindPlayer = true;
//                }
//            }
//            if(y3 <= target.y){ // target is below
//                if (pt.y > target.y){
//                    behindPlayer = true;
//                }
//            }
            // Adjust ray collision appropriately.
//            if (!behindPlayer) {
                return pt;
//            }
        }


        return null;
    }
}
