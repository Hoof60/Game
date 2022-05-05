import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static processing.core.PApplet.atan2;
import static processing.core.PConstants.PI;

final class Character {
    PVector position;
    PApplet app;
    PVector velocity;
    private float invMass = 0.05f;
    boolean onGround = true;
    static final PVector gravity = new PVector(0, 20f);
    static final float xDrag = 0.5f;
    static final float moveSpeed = 10;
    private float XAcceleration = 2f;
    boolean aiming = false;
    PImage Idle;
    PImage AimingBody;
    PImage gunArm;
    PImage crouch;
    Float orientation = 0f;
    boolean ducking;

    int bullets;

    Character(PApplet app) {
        position = new PVector(100, 500);
        bullets = 30;
        this.app = app;
        this.velocity = new PVector(0, 0);
        Idle = app.loadImage("Assets/Idle.png");
        AimingBody = app.loadImage("Assets/Dude.png");
        gunArm = app.loadImage("Assets/gunArm.png");
        crouch = app.loadImage("Assets/crouch.png");
//        AimingBody.resize(200, 400);
//        Idle.resize(200, 400);
    }

    void draw() {
        app.fill(200);
        if (ducking){
            app.image(crouch, position.x, position.y);
        } else if (aiming) {
            float angle = atan2(app.mouseY - (position.y + 110), (app.mouseX + position.x - 500) - (position.x + 190));
//            System.out.println(angle);
            if (angle < -2 || angle > 1) {
                app.pushMatrix();
                app.translate(Idle.width, 0);
                app.scale(-1, 1);
                app.image(AimingBody, -position.x, position.y);
                app.pushMatrix();
                app.translate(-position.x + 190, position.y + 110);
                app.rotate(-(PI +angle));
                //app.line(0, 0, app.mouseX + position.x - 500, app.mouseY);
                app.image(gunArm, 0, -20);
                app.popMatrix();
            } else {
                app.image(AimingBody, position.x, position.y);
                app.pushMatrix();
                app.translate(position.x + 190, position.y + 110);
                app.rotate(angle);
                app.image(gunArm, 0, -20);
            }
            app.line(position.x + 190, position.y + 100, app.mouseX + position.x - 500, app.mouseY);
            app.popMatrix();
        } else {
            if (orientation == 0) {
                app.image(Idle, position.x, position.y);
            } else {
                app.pushMatrix();
                app.translate(Idle.width, 0);
                app.scale(-1, 1);
                app.image(Idle, -position.x, position.y);
                app.popMatrix();
            }

        }

        app.fill(0);
        app.text(bullets, 10, 40);
        app.fill(200);
        app.stroke(255);

        //CrossHair
//        app.line(app.mouseX+ position.x - 500 - 20, app.mouseY, app.mouseX+ position.x - 500 - 10, app.mouseY);
//        app.line(app.mouseX+ position.x - 500 + 20, app.mouseY, app.mouseX+ position.x - 500 + 10, app.mouseY);
//        app.line(app.mouseX+ position.x - 500, app.mouseY - 20, app.mouseX + position.x - 500, app.mouseY - 10);
//        app.line(app.mouseX+ position.x - 500, app.mouseY + 20, app.mouseX + position.x - 500, app.mouseY + 10);

        app.noStroke();
        if (position.y + 500 >= app.displayHeight) {
            onGround = true;
            velocity.y = 0;
        }
        integrate(gravity);
    }

    void move(int dir) {
        if (!ducking) {
            switch (dir) {
                case 0: {
                    if (velocity.x < moveSpeed) {
                        velocity.x += XAcceleration;
                    }
                    orientation = 0f;
                    break;
                }
                case 1: {
                    if (onGround) {
                        velocity.y = -30f;
                        onGround = false;
                        integrate(gravity);
                    }
                    break;
                }
                case 2: {
                    if (velocity.x > -moveSpeed) {
                        velocity.x -= XAcceleration;
                    }
                    orientation = -1f;
                    break;
                }
                case 3: {
                    break;
                }
            }
        }
    }

    boolean integrate(PVector force) {
        // If infinite mass, we don't integrate
        if (invMass <= 0f) return false;

        // update position
        position.add(velocity);

        PVector acceleration = force.get();
        acceleration.mult(invMass);

        if (velocity.x > 0) {
            acceleration.x = -xDrag;
        } else if (velocity.x < 0) {
            acceleration.x = xDrag;
        }

        // update velocity
        velocity.add(acceleration);

        //calculate drag
        PVector n = new PVector(velocity.x, velocity.y);
        n.normalize();

        // Apply an impulse to bounce off the edge of the screen
        return false;
    }


    // Reduces the number of available missiles.
    Ray fire() {
        if (aiming) {
            app.stroke(255, 0, 0);
            app.strokeWeight(4);
            float angle = atan2(app.mouseY - (position.y + 110), (app.mouseX + position.x - 500) - (position.x + 190));
            app.pushMatrix();
            app.translate(-position.x + 500, 0);
            if (angle < -2 || angle > 1) {
                //app.line(position.x + 30, position.y + 100, app.mouseX + position.x - 500, app.mouseY);
            } else {
                //app.line(position.x + 190, position.y + 100, app.mouseX + position.x - 500, app.mouseY);
            }
            app.popMatrix();
            app.strokeWeight(1);

            app.line(position.x + 190-position.x + 500, position.y + 100, app.mouseX, app.mouseY);
            //Fire the bullet
            bullets--;
            Ray hitscanRay = new Ray(new PVector(position.x + 190-position.x + 500, position.y + 100), angle, app);
            return hitscanRay;

        }

        return null;
    }

    public void duck(boolean toggle) {
        if (onGround && toggle){
           ducking = true;
        } else {
            ducking = false;
        }
    }
}
