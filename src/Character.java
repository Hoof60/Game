import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static java.lang.Math.max;
import static processing.core.PApplet.atan2;
import static processing.core.PConstants.PI;

final class Character {
    PVector position;
    PApplet app;
    PVector velocity;
    Level1 level;
    private float invMass = 0.05f;
    boolean onGround = true;
    static final PVector gravity = new PVector(0, 20f);
    static final float xDrag = 0.5f;
    static final float moveSpeed = 10;
    private float XAcceleration = 2f;
    boolean aiming = false;
    int height = 300;
    int width = 170;
    PImage Idle;
    PImage AimingBody;
    PImage gunArm;
    PImage crouch;
    PImage crouchShoot;
    PImage run1;
    PImage run2;
    Float orientation = 0f;
    boolean ducking;
    int health;
    int runCounter = 0;
    boolean runSwitch = false;

    int bullets;

    Character(PApplet app) {
        position = new PVector(400, 400);
        bullets = 30;
        this.app = app;
        this.velocity = new PVector(0, 0);
        Idle = app.loadImage("Assets/Idle.png");
        AimingBody = app.loadImage("Assets/Dude.png");
        gunArm = app.loadImage("Assets/gunArm.png");
        crouch = app.loadImage("Assets/crouch.png");
        crouchShoot = app.loadImage("Assets/crouchShoot.png");
        run1 = app.loadImage("Assets/Idle.png");
        run2 = app.loadImage("Assets/Idle.png");
        run1.resize(220, 280);
        run2.resize(220, 280);
        AimingBody.resize(150, 300);
        gunArm.resize(150, 40);
        Idle.resize(170, 300);
        crouch.resize(220, 250);
        crouchShoot.resize(220, 250);

        health = 5;
    }

    void startLevel(Level1 l) {
        this.level = l;
    }

    void draw() {
        app.fill(200);
        if (ducking && aiming) {
            float angle = atan2(app.mouseY - (position.y + 200), (app.mouseX - 650));
            app.pushMatrix();
            app.translate(position.x + 140, position.y + 200);
            angle = (float) max(angle, -0.5f);
            app.rotate(angle);
            app.image(gunArm, 0, -20);
            app.popMatrix();
            app.image(crouchShoot, position.x, position.y + 50);

        } else if (ducking) {
            app.image(crouch, position.x, position.y + 50);
        } else if (onGround && (velocity.x > 6 || velocity.x < -6)) {
            if (runCounter == 0) {
                runSwitch = !runSwitch;
                runCounter = 5;
            }
            if (velocity.x < 0) {
                app.pushMatrix();
                app.translate(run1.width, 0);
                app.scale(-1, 1);
            }
            if (runSwitch) {
                if (velocity.x < 0) {
                    app.image(run1, -position.x, position.y + 30);
                } else {
                    app.image(run1, position.x, position.y + 30);
                }
                runCounter--;
            } else {
                if (velocity.x < 0) {
                    app.image(run2, -position.x, position.y + 30);
                } else {
                    app.image(run2, position.x, position.y + 30);
                }
                runCounter--;
            }
            if (velocity.x < 0) {
                app.popMatrix();
            }
        } else if (aiming) {

            float angle = atan2(app.mouseY - (position.y + 90), (app.mouseX + position.x - 500) - (position.x + 140));

            if (angle < -2 || angle > 1) {
                app.pushMatrix();
                app.translate(Idle.width, 0);
                app.scale(-1, 1);
                app.image(AimingBody, -position.x, position.y);
                app.pushMatrix();
                app.translate(-position.x + 140, position.y + 90);
                app.rotate(-(PI + angle));
                app.image(gunArm, 0, -20);
                app.popMatrix();
            } else {
                app.image(AimingBody, position.x, position.y);
                app.pushMatrix();
                app.translate(position.x + 140, position.y + 90);
                app.rotate(angle);
                app.image(gunArm, 0, -20);
            }
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
        app.line(app.mouseX + position.x - 500 - 20, app.mouseY, app.mouseX + position.x - 500 - 10, app.mouseY);
        app.line(app.mouseX + position.x - 500 + 20, app.mouseY, app.mouseX + position.x - 500 + 10, app.mouseY);
        app.line(app.mouseX + position.x - 500, app.mouseY - 20, app.mouseX + position.x - 500, app.mouseY - 10);
        app.line(app.mouseX + position.x - 500, app.mouseY + 20, app.mouseX + position.x - 500, app.mouseY + 10);

        app.noStroke();
        if (position.y + height + 25 + level.groundHeight.get((int) position.x / 300) >= app.displayHeight || position.y + height + level.groundHeight.get((int) (position.x + width - 15) / 300) >= app.displayHeight) {
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
        position.y += velocity.y;
        if ((velocity.x > 0 && (position.x + width - 5) % 300 < 10 && position.y + height - 10 >= app.displayHeight - level.groundHeight.get((int) (position.x / 300) + 1)) || (
                velocity.x < 0 && (position.x - 5) % 300 < 10 && position.y + height - 10 >= app.displayHeight - level.groundHeight.get((int) (position.x / 300) - 1))) {
        } else {
            position.x += velocity.x;
        }

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
            float angle = atan2(app.mouseY - (position.y + 200), (app.mouseX - 650));
            app.pushMatrix();
            app.translate(-position.x + 500, 0);
            app.popMatrix();
            app.strokeWeight(1);
            Ray hitscanRay;

            if (ducking) {
                app.line(650, position.y + 200, app.mouseX, app.mouseY);
                hitscanRay = new Ray(new PVector(650, position.y + 200), angle, app);
            }
            else if (angle < -2 || angle > 1) {
                app.line(525, position.y + 90, app.mouseX, app.mouseY);
                hitscanRay = new Ray(new PVector(520, position.y + 90), angle, app);
            } else {
                app.line(640, position.y + 90, app.mouseX, app.mouseY);
                hitscanRay = new Ray(new PVector(640, position.y + 90), angle, app);
            }


            //Fire the bullet
            bullets--;
            return hitscanRay;

        }

        return null;
    }

    void getHit() {
        health--;
        velocity.x += -10;
        app.fill(255, 0, 0);
        app.rect(position.x, position.y, width, height);
    }

    public void duck(boolean toggle) {
        if (onGround && toggle) {
            ducking = true;
        } else {
            ducking = false;
        }
    }
}
