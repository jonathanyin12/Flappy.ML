
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Pipe extends PApplet {

	public float posX, posY, height;
	public static float pipeDiffY = 150, pipeDiffX = 250;
	public static float pipeMIN, pipeMAX;
	public static PImage lowerPipe, upperPipe;
	public static ArrayList<Pipe> pipes = new ArrayList<Pipe>();
	public static int currPipe = 0;
	public static int nextPipe = 0;
	public Pipe(float x, float y) { // Creates a new pipe
		posX = x + width * 10;
		posY = y;
		currPipe = 0;
		nextPipe = 0;
	}

	public static void update() { // Moves all the pipes to the left
		for (Pipe st : pipes) {
			st.posX -= 1.84;
		}
	}

	public static void checkHit(Bird[] pop) { // Checks all birds in population for pipe hits
		for (Bird b : pop) {
			checkHit(b);
		}
	}

	public static void checkHit(Bird b) { // Checks if a given bird has hit the pipe
		Pipe st = pipes.get(currPipe);
		if (b.birdPosition.x + 17 >= st.posX && b.birdPosition.x - 17 <= st.posX + upperPipe.width) {
			if (b.birdPosition.y - 12 < st.posY || b.birdPosition.y + 12 > st.posY + pipeDiffY) {
				b.alive = false;
				b.calcFit();
				if (Display.scene == 2) {
					Bird.hitSound.trigger();
					Bird.dieSound.trigger();
				}
			}

		}
	}

	public static void checkPassed() { // Checks if pipe has traveled off screen
		for (Pipe st : pipes) {
			if (st.posX + upperPipe.width < 0) {
				st.posX = pipeDiffX + 1000 - upperPipe.width;
				st.posY = (float) (pipeMIN + Math.random() * (pipeMAX - pipeMIN));
			}
		}
	}

	public static void nextPipe() { // Iterates currPipe variable
		if (pipes.get(currPipe).posX + upperPipe.width < 230) {
			currPipe++;
			currPipe %= 5;
		}
	}
	
	public static void checkScored(Bird[] pop) { // Checks if the birds in the population have scored
		if (pipes.get(nextPipe).posX + upperPipe.width / 2 < 250) {
			for (Bird b : pop) {
				if (b.alive) {
					b.score++;
					if (Display.scene == 2) {
						Bird.scoreSound.trigger();
					}
				}
			}
			nextPipe++;
			nextPipe%= 5;
			Scoring.score++;
		}
	}
	public static void checkScored(Bird b) { // Checks if a given bird has scored
		if (pipes.get(nextPipe).posX + upperPipe.width / 2 < 250) {

			if (b.alive) {
				b.score++;
				if (Display.scene == 2) {
					Bird.scoreSound.trigger();
				}
			}
			nextPipe++;
			nextPipe%= 5;
			Scoring.score++;
		}
	}

	public static void createNew(PApplet p) { // Creates an array of new pipes
		pipes.clear();
		int pipeCount = (int) (p.width / Pipe.pipeDiffX) + 1;
		for (int k = 0; k < pipeCount; k++) {
			Pipe.pipes.add(new Pipe(Pipe.pipeDiffX * k, p.random(Pipe.pipeMIN, Pipe.pipeMAX)));

		}
	}

	public static void display(PApplet p) { // Displays all the pipes
		p.imageMode(CORNER);
		for (Pipe st : pipes) {
			p.image(upperPipe, st.posX, st.posY - upperPipe.height);
			p.image(lowerPipe, st.posX, st.posY + pipeDiffY);
		}

	}

}
