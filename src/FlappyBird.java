
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;

public class FlappyBird extends PApplet {

	public static void main(String[] args) {
		PApplet.main("FlappyBird");
	}

	Minim minim;
	Generation training;
	Bird b;
	PFont font1, font2;

	public void settings() {
		size(1000, 750);
	}

	public void setup() {

		Display.p = this;

		minim = new Minim(this);

		Bird.wingSound = minim.loadSample("wing.mp3", 512);
		Bird.hitSound = minim.loadSample("hit.mp3", 512);
		Bird.scoreSound = minim.loadSample("point.mp3", 512);
		Bird.dieSound = minim.loadSample("die.mp3", 512);

		Bird.bird[0] = loadImage("bird1.png");
		Bird.bird[1] = loadImage("bird2.png");
		Bird.bird[2] = loadImage("bird3.png");

		Display.imgTitle = loadImage("title.png");
		Display.imageClick = loadImage("click.png");
		Display.imgGetReady = loadImage("getReady.png");
		Display.background1 = loadImage("dayCity.png");
		Display.background2 = loadImage("nightCity.png");
		Display.base = loadImage("base.png");
		Display.imageGameOver = loadImage("gameOver.png");
		Display.imageScoreCard = loadImage("scoreCard.png");
		Display.goldScoreCard = loadImage("goldScoreCard.png");

		Pipe.lowerPipe = loadImage("stump.png");
		Pipe.upperPipe = loadImage("stumpi.png");

		Display.BY = height - Display.base.height;
		Display.BGY = -1 * (Display.background1.height - height + Display.base.height);
		Display.gameOverPosY = Display.imageGameOver.height * -1;
		Display.scoreCardPosY = height;

		Pipe.pipeMIN = Display.BY / 6;
		Pipe.pipeMAX = Display.BY - Pipe.pipeDiffY - Pipe.pipeMIN;

		font1 = loadFont("FlappyFont-48.vlw");

		textFont(font1);

		Scoring.topScoreFileLoader();

		training = new Generation(50, this);
		b = new Bird(this);

		Display.pickBackground();
	}

	public void draw() {
		Display.drawBackground();

		switch (Display.scene) {

		case 0: // Title screen
			frameRate(70);
			Display.drawBase();
			Display.title();
			break;

		case 1: // "Get Ready" screen
			Display.getReady();

			break;

		case 2: // Manual mode game screen
			frameRate(70);

			if (!b.floorHit) {
				Pipe.display(this);
				if (b.alive) {
					Pipe.update();
					Pipe.checkPassed();
					Pipe.checkHit(b);
					Pipe.checkScored(b);
					Pipe.nextPipe();
					Display.drawBase();
				} else {
					image(Display.base, Display.baseInc, Display.BY);
				}
				b.fly();
				Scoring.printNum(Scoring.score, width / 2, height / 6, 40);
			} else {
				Display.scene = 3;
			}
			break;

		case 3: // Manual mode lose screen
			Display.gameOver(b);
			break;

		case 4: // Training screen
			frameRate(180);
			if (training.genOver()) {
				training.naturalSelection();
				Pipe.createNew(this);
				Scoring.score = 0;
				Display.pickBackground();
			} else {
				training.genFly();
				training.genFlap();
				Pipe.update();
				Pipe.checkPassed();
				Pipe.checkHit(training.population);
				Pipe.checkScored(training.population);
				Pipe.nextPipe();
				Pipe.display(this);
				Scoring.trainingTopScore= Math.max(Scoring.trainingTopScore, Scoring.score);
				Display.drawBase();
				Display.training(training);

				Scoring.printNum(Scoring.score, width / 2, height / 6, 40);
			}
			break;
		}
	}

	public void mousePressed() {
		switch (Display.scene) {

		case 0: // Title screen buttons
			if (abs(mouseX - width / 2) < 65 && abs(mouseY - height / 4 - 200) < 25) { // Play button
				Pipe.createNew(this);
				Display.pickBackground();
				b.initORreset();
				Display.scene = 1;
				
			} else if (abs(mouseX - width / 2) < 65 && abs(mouseY - height / 4 - 300) < 25) { // Train button
				Pipe.createNew(this);
				Scoring.score = 0;
				Display.pickBackground();
				training.genReset();
				Display.scene = 4;

			}
			break;

		case 1: // Transition to manual game play
			Display.scene = 2;
			break;

		case 2: // Manual game play
			if (b.alive) {
				b.velocity.add(b.up);
				Bird.wingSound.trigger();
			}
			break;

		case 3: // Resets manual game mode
			if (Display.scoreCardPosY == 220) {
				if (abs(mouseX - width / 2) < 65 && abs(mouseY - height / 4 - 200) < 25) { // Play button
					Scoring.scoreUpdator();
					Scoring.score = 0;
					Pipe.createNew(this);
					b.initORreset();
					Display.pickBackground();
					Display.gameOverPosY = Display.imageGameOver.height * -1;
					Display.scoreCardPosY = height;
					Display.scene = 1;
				} else if (abs(mouseX - width / 2) < 65 && abs(mouseY - height / 4 - 300) < 25) { // Exit button
					Scoring.scoreUpdator();
					Scoring.score = 0;
					Pipe.createNew(this);
					b.initORreset();
					Display.pickBackground();
					Display.gameOverPosY = Display.imageGameOver.height * -1;
					Display.scoreCardPosY = height;
					Display.scene = 0;
				}
			}
			break;

		case 4: // Training screen buttons
			if (abs(mouseX - width / 4) < 100 && abs(mouseY - (height - 45)) < 25) { // Training info button
				Display.trainingInfo = !Display.trainingInfo;
			} else if (abs(mouseX - width * 3 / 4) < 100 && abs(mouseY - (height - 45)) < 25) { // Exit button
				Display.scene = 0;
			}

		}
	}
}
