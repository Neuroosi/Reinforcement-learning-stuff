package neuralnetworklibary.Learning_GA;

import java.util.Arrays;
import neuralnetworklibary.Network;

public class Agent implements Comparable<Agent> {

    private int x;
    private int y;
    private int goalx;
    private int goaly;
    private double fitness = 0;
    private Network brain;
    private char map[][];
    private int time;
    private int health = 100;
    private int coins = 0;
    private int mines = 0;
    private int[][] squaresVisited;
    private int[][] explored;
    private int visitedOnce;
    private int step = 0;
    private int coinsInmap;
    private double areaExplored = 0;

    // agentti, jolla satunnaisesti generoidut painot.
    public Agent(int x, int y, int goalx, int goaly, char map[][]) {
        this.x = x;
        this.y = y;
        this.goalx = goalx;
        setMap(map);
        this.squaresVisited = new int[map.length][map.length];
        explored = new int[map.length][map.length];
        this.goaly = goaly;
        this.brain = new Network();
    }

    public Agent(double weights[][], double bias[][], char map[][]) {
        brain = new Network(weights, bias);
        setMap(map);
        this.squaresVisited = new int[map.length][map.length];
        explored = new int[map.length][map.length];
    }

    //laskee seuraavan siirron
    public double[] computeNextState() {
        return brain.compute(generateInputVector());
    }

    //generoi syötevektorin
    public double[] generateInputVector() {
        int deltaX[] = new int[]{0, 0, 1, -1, 1, -1, 1, -1};
        int deltaY[] = new int[]{1, -1, 0, 0, 1, 1, -1, -1};
        return scanSurroundingSquares(deltaY, deltaX, brain.getIntputNeurons());
    }

    public double[] scanSurroundingSquares(int deltaY[], int deltaX[], int inputNeurons) {
        double data[] = new double[inputNeurons];
        double scan[] = scanSquare(getY(), getX());
        double scan2[] = scanSquare(getX(), getY());
        data[0] = scan[0];
        data[1] = scan[1];
        data[2] = scan2[0];
        data[3] = scan2[1];
        int i = iterateDelta(deltaX, deltaY, data, 4, 'c');
        iterateDelta(deltaX, deltaY, data, i, 't');
        return data;
    }

    public double[] scanSquare(int x, int y) {
        double scan[] = new double[2];
        double mines = 1;
        int coins = 0;
        int notVisited = 0;
        for (int i = x - 3; i <= x + 3; i++) {
            if (i == x) {
                scan[0] = coins *2 + notVisited;
                mines = 1;
                coins = 0;
                notVisited = 0;
                continue;
            }
            for (int j = y - 1; j <= y + 1 && i >= 0 && i < map.length && j >= 0 && j < map.length; j++) {
                if (map[i][j] == 'c') {
                    coins++;
                } else if (map[i][j] == 't') {
                    mines++;
                } else if (explored[i][j] == 0) {
                    notVisited++;
                }
            }
        }
        scan[1] = coins*2 + notVisited;
        return scan;
    }

    public double datafromSquare(int y, int x) {
        if (map[x][y] == 't') {
            return 1;
        }
        return 0;
    }

    public int iterateDelta(int deltaX[], int deltaY[], double data[], int i, char object) {
        for (int e = 0; e < 8; i++, e++) {
            insertData(deltaY[e], deltaX[e], data, i, e, object);
        }
        return i;
    }

    public void insertData(int dy, int dx, double data[], int i, int e, char object) {
        if (getX() + dx >= 0 && getY() + dy >= 0 && getY() + dy < map[0].length && getX() + dx < map.length) {
            if (map[getX() + dx][getY() + dy] == object) {
                data[i] = 20;
            }
        }// else {
        //  data[i] = (double) '#' * 10;
        //}
    }

    public void scalarMultiply(int vector[], double scalar) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalar;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setVisitedSquare() {
        if (squaresVisited[getX()][getY()] == 0) {
            squaresVisited[getX()][getY()] = 1;
            visitedOnce++;
        } else {
            squaresVisited[getX()][getY()]++;
        }
        updateExploredArea();
    }

    public void updateExploredArea() {
        int deltaX[] = new int[]{0, 0, 1, -1, 1, -1, 1, -1};
        int deltaY[] = new int[]{1, -1, 0, 0, 1, 1, -1, -1};
        for (int i = 0; i < deltaX.length; i++) {
            if (explored[deltaX[i] + getX()][deltaY[i] + getY()] == 0) {
                areaExplored++;
                explored[deltaX[i] + getX()][deltaY[i] + getY()] = 1;
            }
        }
    }

    public double getExploredArea() {
        return areaExplored / (map.length * map.length) * 10;
    }

    public int getVisitedSquares() {
        return visitedOnce;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setGoaly(int y) {
        this.goaly = y;
    }

    public void setGoalx(int x) {
        this.goalx = x;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void increaseCoins() {
        coins++;
    }

    public void increaseMines() {
        mines++;
    }

    public int getMines() {
        return mines;
    }

    public void resetCoins() {
        coins = 0;
    }

    public void decreaseHealth(boolean large, boolean stay) {
        if (large) {
            health -= 40;
        } else if (stay) {
            health -= 100;
        } else {
            health -= 2;
        }
    }

    public void increaseHealth() {
        health += 10;
    }

    public int getCoins() {
        return coins;
    }

    public int getHealth() {
        return health;
    }

    public Network getBrain() {
        return brain;
    }

    public void setFitness(double x) {
        fitness = x;
    }

    public void resetFitness() {
        fitness = 0;
    }

    public double getFitness() {
        return fitness;
    }

    public void setMap(char map[][]) {
        this.map = map;
    }

    public void coinsInmap(int x) {
        coinsInmap = x;
    }

    public int coinsInmap() {
        return coinsInmap;
    }

    @Override
    public int compareTo(Agent t) {
        if (t.getFitness() > getFitness()) {
            return 1;
        } else if (t.getFitness() == getFitness()) {
            return 0;
        } else {
            return -1;
        }
    }

    public void addStep() {
        step++;
    }

    public int getSteps() {
        return step;
    }

}
