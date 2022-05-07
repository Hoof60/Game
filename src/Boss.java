import processing.core.PApplet;
import processing.core.PImage;

public class Boss {
    PApplet app;
    PImage face1;
    PImage face2;
    boolean speaking;
    boolean faceToggle;
    boolean calling;
    int faceCounter;
    boolean playerDead = false;
    int introTextNum = 0;
    int introTextTimer = 300;
    boolean introDone = false;
    boolean killedCEO = false;

    public Boss(PApplet app) {
        this.app = app;
        calling = true;
        speaking = true;
        face1 = app.loadImage("Assets/Boss1.png");
        face2 = app.loadImage("Assets/boss2.png");
        face1.resize(200, 200);
        face2.resize(200, 200);
    }

    void draw() {
        app.fill(200);
        app.rect(0, 0, app.displayWidth, 200);
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

        app.fill(0);
        int textx = 300;
        int texty = 100;
        switch (introTextNum){
            case 0:{
                speaking = false;
                app.fill(255,0,0);
                if (faceToggle) {
                    app.text("BOSS CALLING", textx, texty);
                }
                app.text("press [SpaceBar] to pickup", textx, texty + 70);
                break;
            }
            case 1:{
                speaking = true;
                app.text("Good, I thought I might have to activate your brain implant for a moment.", textx, texty);
                break;
            }
            case 2:{
                app.text("Your first target is a company called DP.", textx, texty);
                break;
            }
            case 3:{
                app.text("Last year they spilled 3 Trillion Gallons of oil into the ocean.", textx, texty);
                break;
            }
            case 4:{
                app.text("Since this is below their 5 trillion gallon quota...", textx, texty);
                break;
            }
            case 5:{
                app.text("We have been awarded the contract to obliterate them.", textx, texty);
                break;
            }
            case 6:{
                app.text("You need to find and kill their CEO, John Greeby.", textx, texty);
                break;
            }
            case 7:{
                app.text("Be aware that he only employs killer man-bots.", textx, texty);
                break;
            }
            case 8:{
                app.text("Something about 'keeping his HR costs down' or whatever. ", textx, texty);
                break;
            }
            case 9:{
                app.text("Also try not to die. The more I spend on cleaning up your corpse...", textx, texty);
                break;
            }
            case 10:{
                app.text("The less I can spend on Gunko-pops.", textx, texty);
                break;
            }
            case 11:{
                introDone = true;
            }
        }
        if (introTextNum != 0){
            app.textSize(20);
            app.text("[SPACEBAR] to continue", textx, texty+50);
        } else  if (killedCEO){
            app.text("Nice job, that guy was beginning to get on my nerves.", textx, texty);
        } else if (playerDead) {
            app.text("I thought he said that he was good at this on his CV.", textx, texty);
        }


    }

    void Intro(){
        introTextNum++;
    }

    void playerDied(){
        playerDead = true;
    }

}
