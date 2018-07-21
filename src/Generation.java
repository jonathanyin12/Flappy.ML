import processing.core.PApplet;

public class Generation extends PApplet {

	public PApplet p;

	public Bird[] population;
	public int generation;
	public float totalFit = 0;
	public float avgFit = 0;
	public double mutationRate = 0.05;

	public Generation(int size, PApplet p) { // Creates a completely new generation
		this.p = p;
		population = new Bird[size];
		for (int i = 0; i < population.length; i++) {
			population[i] = new Bird(p);
		}
	}

	public void naturalSelection() { // Creates new generation through crossover and mutation

		// Finds total fitness in population
		totalFit = 0;
		for (Bird b : population) {
			totalFit += b.fitness;
		}

		// Calculates average fitness of the population
		avgFit = totalFit / population.length;
		avgFit = (float) (Math.round(avgFit * 100.0) / 100.0);

		// Calculates relative fitness of each bird in relation to population
		for (Bird b : population) {
			b.fitness /= totalFit;
		}

		// Generates new population
		Bird[] newPop = population;
		sort();
		for (int i = population.length / 4; i < population.length; i++) {
			newPop[i].neuralNet = select().crossover(select().neuralNet);
		}
		population = newPop;

		// Mutates population
		for (Bird b : population) {
			b.mutate(mutationRate);
		}

		genReset();

		generation++;
	}

	public Bird[] sort() { // Sorts the population of birds based on their fitness
		for (int i = 0; i < population.length - 1; i++) {
			for (int j = 0; j < population.length - i - 1; j++) {
				if (population[j].fitness < population[j + 1].fitness) {
					Bird temp = population[j];
					population[j] = population[j + 1];
					population[j + 1] = temp;
				}
			}
		}
		return population;
	}

	public void genFly() { // Controls movement of bird population
		for (Bird b : population) {
			if (b.alive) {
				b.fly();
			}
		}
	}

	public void genFlap() { // Controls flapping of bird population
		for (Bird b : population) {
			if (b.alive) {
				b.neuralNet.flap(b, Pipe.pipes.get(Pipe.currPipe));
			}
		}
	}

	public void genReset() { // Resets the population to default
		for (Bird b : population) {
			b.initORreset();
		}
	}

	public boolean genOver() { // Determines if all birds are dead
		for (Bird b : population) {
			if (b.alive) {
				return false;
			}
		}
		return true;
	}

	public int numAlive() { // Counts number of birds alive in population
		int count = 0;
		for (Bird b : population) {
			if (b.alive) {
				count++;
			}
		}
		return count;
	}

	Bird select() { // Selects a bird with probability based on relative fitness
		int index = 0;
		float r = random(1);
		while (r > 0) {
			r = r - population[index].fitness;
			index++;
		}
		return population[index - 1];
	}
}
