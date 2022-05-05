import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PApplet.pow;

final class Bullet {

    private final PApplet app;
    PVector start;
    PVector position;
    PVector velocity;
    private float invMass;
    float k1 = 0.001f;
    float k2 = 0.001f;

    // damping factor to simulate drag, as per Millington
    private static final float DAMPING = .995f;

    // Creates a missile at given coordinates with given velocity and mass.
    Bullet(PVector position, PVector velocity, float invM, PApplet app) {
        this.position = position;
        this.velocity = velocity.mult(0.02f);
        this.start = position.get();
        invMass = invM;
        this.app = app;
    }

    void draw() {
        app.fill(250);
        app.rect(position.x, position.y, 5, 5);
    }


    // moves the missile returns true if the missile is out of bounds.
    boolean move(PVector force) {
        // If infinite mass, we don't integrate
        if (invMass <= 0f) return false;

        // update position
        position.add(velocity);

        PVector acceleration = force.get();
        acceleration.mult(invMass);

        // update velocity
        velocity.add(acceleration);

        //calculate drag
        PVector n = new PVector(velocity.x, velocity.y);
        n.normalize();
        PVector drag = n.mult((k1 * velocity.mag() + k2 * (pow(velocity.mag(), 2))));
        drag.x = -drag.x;
        drag.y = -drag.y;

        // apply damping
        velocity.add(drag);

        // Apply an impulse to bounce off the edge of the screen
        if ((position.x < 0) || (position.x > app.width)) position.x = app.displayWidth - position.x;
        if (position.y >=  app.displayHeight) return true;
        return false;
    }
}
