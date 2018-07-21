
public abstract class Scoring extends Display {
	public static int score = 0;
	public static int TopScore;
	public static int trainingTopScore = 0;

	public static void printNum(int n, int x, int y, int size) { // Displays the score
		p.textSize(size);
		p.fill(0, 150);
		p.text(n, x, y + 2);
		p.text(n, x, y - 2);
		p.text(n, x + 2, y);
		p.text(n, x - 2, y);
		p.fill(255);
		p.text(n, x, y);
	}

	public static void scoreUpdator() { // Updates the score
		if (score > TopScore) {
			TopScore = score;
			topScoreFileUpdator();
		}
	}

	public static void topScoreFileLoader() { // Loads high score from "data.aff" file
		String lines[] = p.loadStrings("data.aff");
		TopScore = unhex(lines[2]);
	}

	public static void topScoreFileUpdator() { // Saves high score onto "data.aff" file
		String words = "5df5745h5 @#SDG54541sfs " + hex(TopScore)
				+ " YUGYU56%^$%tgrtYTFG% HJHDS45%$%$ 8674543423&&^(DSHFJU 7451#Dd";
		String[] list = split(words, ' ');

		// Writes the strings to a file, each on a separate line
		p.saveStrings("data.aff", list);
	}
}
