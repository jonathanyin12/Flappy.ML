
import java.util.Random;

import ddf.minim.AudioSample;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Bird extends PApplet {
	public PApplet p;

	public static PImage[] bird = new PImage[3];

	public static AudioSample wingSound;
	public static AudioSample hitSound;
	public static AudioSample dieSound;
	public static AudioSample scoreSound;

	public NN neuralNet = new NN();
	public PVector birdPosition, velocity, gravity, up;
	public boolean alive, floorHit;
	public int brd, score;
	public float fitness = 0;

	public Bird(PApplet p) { // Creates a new bird
		this.p = p;
		initORreset();
	}

	public void initORreset() { // Initializes or resets variables to default
		birdPosition = new PVector(250, height * 7 / 2 - 50);
		velocity = new PVector(0, 0);
		gravity = new PVector(0, .3f);
		up = new PVector(0, -30);
		floorHit = false;
		alive = true;
		score = 0;

	}

	public void fly() {	// Animates the flying bird
		applyForces();

		p.translate(birdPosition.x, birdPosition.y);
		if (velocity.y / 12 > 0) {
			p.rotate(velocity.y / 5);
		} else {
			p.rotate(velocity.y / 15);
		}
		display();
		p.resetMatrix();

		if (Display.scene == 4) {
			ceilingHit();
		}
		floorHit();
	}

	void display() { // Draws the bird
		p.imageMode(CENTER);
		p.image(bird[brd / 10], 0, 0);
		brd++;
		brd %= 20;
	}

	void applyForces() { // Applies gravity and other forces
		velocity.add(gravity);
		velocity.limit(8);
		birdPosition.add(velocity);
	}

	public void ceilingHit() { // Checks if bird has hit the ceiling
		if (birdPosition.y < bird[brd / 10].height / 2) {
			alive = false;
			calcFit();
		}
	}

	public void floorHit() { // Checks if bird has hit the floor
		if (birdPosition.y > Display.BY) {
			birdPosition.sub(velocity);
			velocity.set(0, 0);
			gravity.set(0, 0);
			if (alive) {
				calcFit();
				if (Display.scene == 2) {
					hitSound.trigger();
				}
			}
			alive = false;
			floorHit = true;
		}
	}

	public void calcFit() { // Calculates the fitness of the bird based on score and distance
		Pipe st = Pipe.pipes.get(Pipe.currPipe);
		float dist = (float) Math.sqrt(Math.pow(st.posX + Pipe.upperPipe.width - birdPosition.x, 2)
				+ Math.pow(st.posY + (Pipe.pipeDiffY / 2) - birdPosition.y, 2));
		fitness = (float) Math.pow(score + 1 - dist / 1000, 3);

	}

	public NN crossover(NN partner) { // Crosses over weights from bird's NN
		NN child = new NN();
		for (int r = 0; r < child.ihWeights.length; r++) {
			for (int c = 0; c < child.ihWeights[r].length; c++) {
				int rand = (int) random(0, 4);
				if (rand == 0) {
					child.ihWeights[r][c] = neuralNet.ihWeights[r][c];

				} else if (rand == 1) {
					child.ihWeights[r][c] = partner.ihWeights[r][c];
				} else {
					child.ihWeights[r][c] = (partner.ihWeights[r][c] + neuralNet.ihWeights[r][c]) / 2;
				}

			}
		}
		for (int r = 0; r < child.hoWeights.length; r++) {
			for (int c = 0; c < child.hoWeights[r].length; c++) {
				int rand = (int) random(0, 4);
				if (rand == 0) {
					child.hoWeights[r][c] = neuralNet.hoWeights[r][c];
				} else if (rand == 1) {
					child.hoWeights[r][c] = partner.hoWeights[r][c];
				} else {
					child.hoWeights[r][c] = (partner.hoWeights[r][c] + neuralNet.hoWeights[r][c]) / 2;

				}

			}
		}

		return child;
	}

	void mutate(double mutationRate) { // Mutates a random selection of weights in the NN
		for (int r = 0; r < neuralNet.ihWeights.length; r++) {
			for (int c = 0; c < neuralNet.ihWeights[r].length; c++) {
				if (Math.random() < mutationRate) {
					Random rand = new Random();
					neuralNet.ihWeights[r][c] += (float) (rand.nextGaussian() * 0.1);
				}
			}
		}
		for (int r = 0; r < neuralNet.hoWeights.length; r++) {
			for (int c = 0; c < neuralNet.hoWeights[r].length; c++) {
				if (Math.random() < mutationRate) {
					Random rand = new Random();
					neuralNet.hoWeights[r][c] += (float) (rand.nextGaussian() * 0.1);
				}
			}
		}
	}

}
