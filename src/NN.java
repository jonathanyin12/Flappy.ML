
import processing.core.PApplet;

public class NN extends PApplet {
	float[][] ihWeights = new float[2][5];
	float[][] hoWeights = new float[5][1];
	float[] input = new float[2];
	float[] hidden = new float[8];

	public NN() { // Initializes with random weights ranging from -1 to 1
		for (int r = 0; r < ihWeights.length; r++) {
			for (int c = 0; c < ihWeights[r].length; c++) {
				ihWeights[r][c] = random(2) - 1;
			}
		}
		for (int r = 0; r < hoWeights.length; r++) {
			for (int c = 0; c < hoWeights[r].length; c++) {
				hoWeights[r][c] = random(2) - 1;
			}
		}
	}

	public void flap(Bird b, Pipe st) { // Determines whether to flap or not based on inputs
		input[0] = (st.posX - b.birdPosition.x) / 100;
		input[1] = (st.posY - b.birdPosition.y) / 100;

		if (calculate() > 0.7) {
			b.velocity.add(b.up);
		}
	}

	float calculate() { // Calculates output from inputs

		// Calculates hidden layer value
		for (int c = 0; c < ihWeights[0].length; c++) {
			float weightedSum = 0;
			for (int r = 0; r < ihWeights.length; r++) {
				weightedSum += input[r] * ihWeights[r][c];
			}
			hidden[c] = sigmoid(weightedSum);
		}

		// Calculates output layer value
		float weightedSum = 0;
		for (int r = 0; r < hoWeights.length; r++) {
			weightedSum += hidden[r] * hoWeights[r][0];
		}

		return sigmoid(weightedSum);
	}

	float sigmoid(float sum) { // Sigmoid activation function
		sum = 1 / (1 + exp(-sum));
		return sum;
	}
}
