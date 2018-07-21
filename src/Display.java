
import processing.core.PApplet;
import processing.core.PImage;

public abstract class Display extends PApplet {

	public static PApplet p;

	public static PImage background;
	public static PImage background1;
	public static PImage background2;
	public static PImage base;
	public static PImage imgTitle;
	public static PImage imgGetReady;
	public static PImage imageGameOver;
	public static PImage imageScoreCard;
	public static PImage imageClick;
	public static PImage goldScoreCard;

	public static int baseInc = 0;
	public static int scene = 0;
	public static float BY;
	public static float BGY;
	public static int gameOverPosY;
	public static int scoreCardPosY;
	public static int brd = 0;
	public static int deg = 0;

	public static boolean trainingInfo = false;

	public static void title() { // Title Screen (scene 0)

		// Buttons
		p.rectMode(CENTER);
		p.fill(50);
		p.rect(p.width / 2, p.height / 4 + 200, 130, 50, 10);
		p.rect(p.width / 2, p.height / 4 + 300, 130, 50, 10);

		// Text on buttons
		p.textAlign(CENTER);
		p.textSize(30);
		p.fill(255);
		p.text("Play", p.width / 2, p.height / 4 + 212);
		p.text("Train", p.width / 2, p.height / 4 + 312);

		// Text for title
		p.textSize(50);
		p.fill(50, 200);
		p.text("Flappy.ML", p.width / 2, p.height / 4 + 2);
		p.text("Flappy.ML", p.width / 2, p.height / 4 - 3);
		p.text("Flappy.ML", p.width / 2 + 2, p.height / 4);
		p.text("Flappy.ML", p.width / 2 - 2, p.height / 4);
		p.fill(255);
		p.text("Flappy.ML", p.width / 2, p.height / 4);

		// Text for name
		p.textFont(p.loadFont("GillSansMT-48.vlw"));
		p.fill(50, 200);
		p.textSize(15);
		p.text("Created by \n Jonathan Yin", p.width - 50, p.height - 30);
		p.textFont(p.loadFont("FlappyFont-48.vlw"));

		flyingBird(p.width / 2, p.height / 3 + 25 + sin(radians(deg) * 10) * 5);
	}

	public static void getReady() { // "Get Ready" screen (scene 1)
		p.image(imgGetReady, p.width / 2 - imgGetReady.width / 2, p.height / 4 - 50);
		p.image(imageClick, p.width / 2 - imageClick.width / 2, p.height / 3);

		flyingBird(p.width / 4, p.height / 3 + 25 + sin(radians(deg) * 10) * 5);

		drawBase();
	}

	public static void gameOver(Bird b) { // Lose screen (scene 2)

		// Displays the stumps
		Pipe.display(p);

		// Draws stationary base
		p.image(base, baseInc, BY);

		// Draws dead bird
		p.translate(b.birdPosition.x, b.birdPosition.y);
		p.rotate(radians(90));
		p.imageMode(CENTER);
		p.image(Bird.bird[0], 0, 0);
		p.resetMatrix();

		// Animates the game over image
		if (gameOverPosY < p.height / 6) {
			gameOverPosY += 5;
		}
		p.image(imageGameOver, p.width / 2, gameOverPosY);

		// Animates the score card image
		if (scoreCardPosY > p.height / 6 + 100) {
			scoreCardPosY -= 10;
		}
		if (Scoring.score > Scoring.TopScore) {
			p.image(goldScoreCard, p.width / 2, scoreCardPosY);
		} else {
			p.image(imageScoreCard, p.width / 2, scoreCardPosY);
		}

		// Displays high score
		Scoring.printNum(Scoring.score > Scoring.TopScore ? Scoring.score : Scoring.TopScore, p.width / 2 + 90,
				scoreCardPosY + 35, 20);

		// Displays score
		Scoring.printNum(Scoring.score, p.width / 2 + 90, scoreCardPosY - 5, 20);

		p.rectMode(CENTER);
		p.fill(50);
		p.rect(p.width / 2, (float) (scoreCardPosY + 167), 130, 50, 10);
		p.rect(p.width / 2, (float) (scoreCardPosY + 267), 130, 50, 10);

		// Text on buttons
		p.textAlign(CENTER);
		p.textSize(30);
		p.fill(255);
		p.text("Play", p.width / 2, (float) (scoreCardPosY + 179));
		p.text("Exit", p.width / 2, (float) (scoreCardPosY + 279));

	}

	public static void training(Generation training) { // Training screen (scene 4)

		p.textSize(25);
		p.fill(70);
		p.textAlign(CENTER);
		p.text("Exit Training", p.width * 3 / 4, p.height - 35);

		if (trainingInfo) { // Displays details related to training
			p.text("Hide Details", p.width / 4, p.height - 35);
			p.textAlign(LEFT);
			p.textSize(30);
			p.fill(255, 200);
			p.text("Generation: " + training.generation, 20, 45);
			p.fill(255, 150);
			p.textSize(18);
			p.text("Birds Alive: " + training.numAlive(), 20, 75);
			p.text("Avg Fitness: " + training.avgFit, 20, 100);
			p.text("High Score: " + Scoring.trainingTopScore, 20, 125);
		} else {
			p.text("Show Details", p.width / 4, p.height - 35);
		}
	}

	public static void pickBackground() { // Selects a background
		p.imageMode(CORNER);
		if ((int) p.random(2) < 1) {
			background = background1;
		} else {
			background = background2;
		}
	}

	public static void drawBackground() { // Draws background
		p.imageMode(CORNER);
		p.image(background, 0, BGY);
	}

	static void drawBase() { // Draws base and shifts it
		p.imageMode(CORNER);
		p.image(base, baseInc, BY);

		baseInc -= 2;
		if (baseInc < -24) {
			baseInc = 0;
		}
	}

	static void flyingBird(float x, float y) { // Flying bird animation
		p.imageMode(CENTER);
		p.image(Bird.bird[brd / 10], x, y);

		brd += 1;
		brd %= 20;

		deg += 1;
		deg %= 360;
	}
}
