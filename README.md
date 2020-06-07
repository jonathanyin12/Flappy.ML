# Flappy.ML
### A genetic algorithm based reinforcement learning project that plays Flappy Bird. 

<p align="center">
    <img src="src/data/Training Mode.PNG" width=600></br>
</p>

### Project Demo: https://youtu.be/9ZNViB0XizM

### Project Details:
At the start of training, a random popultation of birds is created. Each bird consists of unique genetic material (weights) that determines when it jumps relative to the distance from the pipes and the ground. 

After each generation, the birds with the highest Darwinian fitness are selected to produce offspring that have a combination of their parents’ “DNA”. Thus, each successive generation improves until a bird eventually learns to beat the game.  

### Implementation Details:
This program was written in Java and uses the Processing library for visuals.


### How to run this project:
Clone the repository and run the "FlappyBird.java" file, which is found in the "src" folder.
