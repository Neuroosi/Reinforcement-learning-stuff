import java.util.Random;
import java.util.ArrayList;

public class MazeGen {

    private int size;
    private int paths;
    private int recursionMax;
    private long seed;
    private char[][] level;
    private int posX;
    private int posY;
    private int coins = 0;
    private int maxCoins = 0;
    private ArrayList<Point> mainPath = new ArrayList<Point>();
    private int startPosX;
    private int startPosY;
    private int goalPosX;
    private int goalPosY;
    private int pathLength;
    private int tries;
    private int minBlocks;
    private int minPathLength;
    private int coinChance;
    private int trapChance;
    private boolean generateGoal = false;

    Random rng = new Random();

    //level char matrix contains blocks '#', paths and traps 't' and coins 'c'
    //starting point is denoted by 'S'
    //goal is marked by 'G'
    public MazeGen(int size, int paths, int recursionMax, int minBlocks, int minPathLength, long seed, int trapChance, int coinChance) {
        this.size = size;
        this.paths = paths * (int) (Math.exp(0.025 * size));
        this.recursionMax = recursionMax;
        this.seed = seed;
        this.minBlocks = minBlocks;
        this.minPathLength = minPathLength * (int) (Math.exp(0.1 * size));
        this.trapChance = trapChance;
        this.coinChance = coinChance;
        rng.setSeed(seed);
    }

    public MazeGen(long seed) {
        this.size = 48;
        this.paths = 15;
        this.recursionMax = 21;
        this.minBlocks = 4;
        this.minPathLength = 20;
        this.seed = seed;
        this.trapChance = 4;
        this.coinChance = 2;
        rng.setSeed(seed);
    }

    public void printLevel() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(level[j][i] + " ");
            }
            System.out.println("");
        }
    }

    //returns a char matrix, the level
    public char[][] generate() {
        Point p;
        level = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                level[j][i] = '#';
            }
        }

        startPosX = rng.nextInt(size - 2) + 1;
        startPosY = rng.nextInt(size - 2) + 1;
        level[startPosX][startPosY] = 'S';
        pathLength = rng.nextInt(10) + 10 * (int) (Math.exp(0.08 * size)); 		//path length for the path between start and goal

        //generate path to goal
        generateGoal = true;
        generatePath(startPosX, startPosY);

        //generate other paths
        for (int i = 0; i < paths; i++) {
            do {
                p = new Point(rng.nextInt(size - 2) + 1, rng.nextInt(size - 2) + 1);
            } while (level[p.getX()][p.getY()] != '#');

            generatePath(p.getX(), p.getY());
        }

        //insert the S label if missing
        if (level[startPosX][startPosY] != 'S') {
            level[startPosX][startPosY] = 'S';
        }

        //insert the G label if missing
        if (level[goalPosX][goalPosY] != 'G') {
            level[goalPosX][goalPosY] = 'G';
        }

        //remove traps from the main path
        for (int i = 0; i < mainPath.size(); i++) {
            if (level[mainPath.get(i).getX()][mainPath.get(i).getY()] == 't' || level[mainPath.get(i).getX()][mainPath.get(i).getY()] == '#') {
                level[mainPath.get(i).getX()][mainPath.get(i).getY()] = ' ';
            }
        }
        System.out.println("Main path length: " + pathLength);
        System.out.println("Starting position " + level[startPosX][startPosY] + " x: " + startPosX + " y: " + startPosY);
        System.out.println("Goal position " + level[goalPosX][goalPosY] + " x: " + goalPosX + " y: " + goalPosY);
        return level;

    }

    public char[][] generateTrainingLevel() {
        coins = 0;
        level = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                level[j][i] = ' ';
            }
        }
        for (int j = 0; j < size; j++) {
            level[j][0] = '#';
            level[j][size - 1] = '#';
            level[0][j] = '#';
            level[size - 1][j] = '#';
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (rng.nextInt(100) < trapChance) {
                    level[rng.nextInt(size - 2) + 1][rng.nextInt(size - 2) + 1] = 't';
                }

                if (rng.nextInt(100) < coinChance) {
                    level[rng.nextInt(size - 2) + 1][rng.nextInt(size - 2) + 1] = 'c';
                    coins++;
                }
            }
        }
        startPosX = rng.nextInt(size - 2) + 1;
        startPosY = rng.nextInt(size - 2) + 1;
        if (level[startPosX][startPosY] == 'c') {
            level[startPosX][startPosY] = 'S';
            coins--;
        } else {
            level[startPosX][startPosY] = 'S';
        }
        maxCoins = coins;
        return level;
    }

    private void generatePath(int x, int y) {

        posX = x;
        posY = y;
        int direction;

        if (generateGoal == true) {
            tries = pathLength;
            System.out.println("called generatePath(main) with length " + tries);
        } else {
            tries = rng.nextInt(25) + minPathLength;
            System.out.println("called generatePath with length " + tries);

        }

        while (tries > 0) {
            direction = (int) Math.round(4 * (10000 * Math.sin(seed / 12.5) - Math.floor(10000 * Math.sin(seed / 12.5))));

            seed = seed + rng.nextInt(100);

            switch (direction) {

                case 0: //left
                    mover(-1, 0);
                    break;
                case 1: //down
                    mover(0, 1);
                    break;
                case 2: //right
                    mover(1, 0);
                    break;
                case 3: //up
                    mover(0, -1);
                    break;
            }

            if (!generateGoal && paths > 0 && rng.nextInt(recursionMax) == 0) {
                paths--;
                generatePath(rng.nextInt(size - 2) + 1, rng.nextInt(size - 2) + 1);
            }

            if (generateGoal && tries == 1) {
                if (level[posX + 1][posY] == 'S' || level[posX - 1][posY] == 'S' || level[posX][posY + 1] == 'S' || level[posX][posY - 1] == 'S'
                        || level[posX + 1][posY + 1] == 'S' || level[posX - 1][posY - 1] == 'S' || level[posX + 1][posY - 1] == 'S' || level[posX - 1][posY + 1] == 'S') {
                    generatePath(rng.nextInt(size - 2) + 1, rng.nextInt(size - 2) + 1);
                }
                goalPosX = posX;
                goalPosY = posY;
                level[goalPosX][goalPosY] = 'G';
                generateGoal = false;
            }

            if (!generateGoal && rng.nextInt(100) < trapChance && level[posX][posY] == ' ') {
                level[posX][posY] = 't';
            }

            if (!generateGoal && rng.nextInt(100) < coinChance && level[posX][posY] == ' ') {
                level[posX][posY] = 'c';
            }

            tries--;
        }

    }

    public void mover(int dx, int dy) {
        boolean flag = false;
        double b;

        for (int i = 0; i < rng.nextInt(5) + minBlocks; i++) {
            if (posX + dx < size - 1 && posX + dx > 0 && posY + dy < size - 1 && posY + dy > 0) {

                if (recursionMax >= 90) {
                    b = 2 / (1 + Math.exp(1)) - 1;
                } else {
                    b = Math.sin(posX + posY + dx + dy);
                }

                if (dx != 0) {
                    flag = true;

                    if (b > -1.0 && b < -0.33) {
                        if (level[posX + dx * 2][posY] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posX += dx;
                            flag = false;
                        }

                    } else if (b >= -0.33 && b < 0.33) {
                        if (level[posX + dx][posY + 1] == '#' && level[posX + dx][posY - 1] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posX += dx;
                            flag = false;

                        }
                    } else if (b >= 0.33 && b < 1.0) {
                        if (level[posX + dx][posY + 1] == '#' && level[posX + dx][posY - 1] == '#' && level[posX + dx * 2][posY] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posX += dx;
                            flag = false;

                        }
                    } else if (b == 1.0) {
                        level[posX + dx][posY + dy] = ' ';
                        posX += dx;
                        flag = false;
                    }

                } else {
                    flag = true;

                    if (b > -1.0 && b < -0.33) {
                        if (level[posX][posY + dy * 2] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posY += dy;
                            flag = false;
                        }
                    } else if (b >= -0.33 && b < 0.33) {
                        if (level[posX + 1][posY + dy] == '#' && level[posX - 1][posY + dy] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posY += dy;
                            flag = false;
                        }
                    } else if (b >= 0.33 && b < 1.0) {
                        if (level[posX + 1][posY + dy] == '#' && level[posX - 1][posY + dy] == '#' && level[posX][posY + dy * 2] == '#') {
                            level[posX + dx][posY + dy] = ' ';
                            posY += dy;
                            flag = false;
                        }
                    } else if (b == 1.0) {
                        level[posX + dx][posY + dy] = ' ';
                        posX += dx;
                        flag = false;
                    }

                }
                if (flag) {
                    break;
                }

            }

            if (generateGoal) {
                mainPath.add(new Point(posX, posY));
            }
        }
    }

    public int getPathLength() {
        return pathLength;
    }

    public int getGoalX() {
        return goalPosX;
    }

    public int getGoalY() {
        return goalPosY;
    }

    public char[][] getLevel() {
        return level;
    }

    public int getStartX() {
        return startPosX;
    }

    public int getStartY() {
        return startPosY;
    }

    public int getSize() {
        return size;
    }

    public int getCoins() {
        return coins;
    }

    public int getMaxcoins() {
        return maxCoins;
    }

    public void decreaseCoins() {
        coins--;
    }
}

class Point {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
