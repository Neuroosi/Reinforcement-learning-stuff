
import Mazegen.MazeGen;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import neuralnetworklibary.Learning_GA.Agent;
import neuralnetworklibary.Learning_GA.Population;

public class swing {

    JFrame f = new JFrame("Labyrintti");
    JPanel cont = new JPanel();
    JPanel panelAlku = new JPanel();
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JButton alkuB1 = new JButton("Uusi");
    JButton alkuB2 = new JButton("Lataa");
    JButton menuB1 = new JButton("Taso 1");
    JButton menuB2 = new JButton("Taso 2");
    JButton menuB3 = new JButton("Taso 3");
    JButton menuB4 = new JButton("Taso 4");
    JButton menuB5 = new JButton("Taso 5");
    JButton menuB6 = new JButton("Harjoittelutaso");
    JButton menuB7 = new JButton("Tallenna");
    JButton mazeB1 = new JButton("Poistu");
    CardLayout c1 = new CardLayout();
    Gui gui;
    JSlider slider;
    JSlider slider2;
    JPanel guiPanel;
    private Population pop = new Population(2000);
    private MazeGen m;
    private boolean training = false;
    private int seed;

    // Kutsutaan aina kun mennään labyrinttinäkymään
    private void luoGui() {
        // Luodaan uusi Gui
        gui = new Gui(pop, m, training, seed);

        // Luodaan Koko-slider
        slider = new JSlider(SwingConstants.HORIZONTAL, 5, 80, 20);
        slider.setMajorTickSpacing(15);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(15));

        // Luodaan Viive-slider
        slider2 = new JSlider(SwingConstants.HORIZONTAL, 0, 2000, 1000);
        slider2.setMajorTickSpacing(200);
        slider2.setMinorTickSpacing(100);
        slider2.setPaintTicks(true);
        slider2.setSnapToTicks(true);
        slider2.setPaintLabels(true);
        slider2.setLabelTable(slider2.createStandardLabels(500));

        //Asetetaan sliderien toiminnot
        slider.addChangeListener((ChangeEvent e) -> {
            gui.setPituus(slider.getValue());
        });
        slider2.addChangeListener((ChangeEvent e) -> {
            gui.setNopeus(slider2.getValue());
        });
        //Tehdään paneeli johon lisätään sliderit ja tekstit
        guiPanel = new JPanel();
        guiPanel.add(new JLabel("Gen:  " + pop.getGeneration()));
        guiPanel.add(new JLabel("Seed: " + gui.getSeed() + "   "));
        guiPanel.add(new JLabel("Koko"));
        guiPanel.add(slider);
        guiPanel.add(new JLabel("Viive"));
        guiPanel.add(slider2);
        guiPanel.add(mazeB1);
    }

    // Sisältää kaikkien painikkeiden toiminnot
    private class ButtonPress implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            training = false;
            // Aloitusruudun painikkeet
            Random r = new Random();
            seed = r.nextInt(1000);
            if (e.getSource() == alkuB1 || e.getSource() == alkuB2) {
                c1.show(cont, "1"); //siirrytään aloitusruutuun
            } // Labyrintin painike
            else if (e.getSource() == mazeB1) {
                c1.show(cont, "1"); //siirrytään aloitusruutuun
                //poistetaan labyrintti ettei se jää taustalle
                panel2.removeAll();
                gui.poista();
                gui.removeAll();
                gui = null;
            } // Tasovalikon painikkeet
            else {
                // Taso 1
                if (e.getSource() == menuB1) {
                    m = new MazeGen(15, 52, 90, 13, 15, seed, 0, 0);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Taso 2
                else if (e.getSource() == menuB2) {
                    m = new MazeGen(15, 52, 75, 13, 15, seed, 0, 0);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Taso 3
                else if (e.getSource() == menuB3) {
                    m = new MazeGen(24, 7, 75, 3, 15, seed, 3, 0);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Taso 4
                else if (e.getSource() == menuB4) {
                    m = new MazeGen(32, 15, 75, 5, 15, seed, 3, 2);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Taso 5
                else if (e.getSource() == menuB5) {
                    m = new MazeGen(42, 75, 75, 5, 15, seed, 3, 1);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Harjoittelutaso
                else if (e.getSource() == menuB6) {
                    training = true;
                    m = new MazeGen(15, 120, 90, 8, 10, seed, 14, 9);
                    setEnvironment();
                    luoGui(); //luodaan uusi gui ja labyrintti
                } // Tallenna
                else if (e.getSource() == menuB7) {
                    luoGui(); //luodaan uusi gui ja labyrintti
                }
                c1.show(cont, "2"); //siirrytään labyrinttinäkymään
                panel2.add(gui);
                panel2.add(guiPanel, BorderLayout.NORTH);
            }
        }

        public void setEnvironment() {
            setLevel();
            pop.setGoal(m.getGoalY(), m.getGoalX());
            pop.setStart(m.getStartY(), m.getStartX());
            pop.createPopulation();
        }

        public void setLevel() {
            if (training) {
                pop.setMap(m.generateTrainingLevel());
            } else {
                pop.setMap(m.generate());
            }
        }
    }

    public swing() {
        cont.setLayout(c1);

        panel1.setBackground(Color.white);
        panelAlku.setBackground(Color.white);

        panel1.setLayout(new GridBagLayout());
        panelAlku.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panelAlku.add(alkuB1, gbc);
        panelAlku.add(alkuB2, gbc);
        panel1.add(menuB6, gbc);
        panel1.add(menuB1, gbc);
        panel1.add(menuB2, gbc);
        panel1.add(menuB3, gbc);
        panel1.add(menuB4, gbc);
        panel1.add(menuB5, gbc);
        panel1.add(menuB7, gbc);

        ButtonPress bp = new ButtonPress();
        alkuB1.addActionListener(bp);
        alkuB2.addActionListener(bp);
        menuB1.addActionListener(bp);
        menuB2.addActionListener(bp);
        menuB3.addActionListener(bp);
        menuB4.addActionListener(bp);
        menuB5.addActionListener(bp);
        menuB6.addActionListener(bp);
        menuB7.addActionListener(bp);
        mazeB1.addActionListener(bp);

        int xx = 210;
        int yy = 100;
        alkuB1.setPreferredSize(new Dimension(xx, yy));
        alkuB2.setPreferredSize(new Dimension(xx, yy));
        menuB1.setPreferredSize(new Dimension(xx, yy));
        menuB2.setPreferredSize(new Dimension(xx, yy));
        menuB3.setPreferredSize(new Dimension(xx, yy));
        menuB4.setPreferredSize(new Dimension(xx, yy));
        menuB5.setPreferredSize(new Dimension(xx, yy));
        menuB6.setPreferredSize(new Dimension(xx, yy));
        menuB7.setPreferredSize(new Dimension(xx, 50));
        menuB7.setBackground(Color.green);

        panel2.setLayout(new BorderLayout());

        cont.add(panelAlku, "0");
        cont.add(panel1, "1");
        cont.add(panel2, "2");

        c1.show(cont, "0");

        f.add(cont);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();

        //f.add(gui);
        f.setSize(740, 480);
        f.setVisible(true);
    }

    public static void main(String[] args) {

        new swing();
        //m.printLevel();

    }

}

// Labyrintin piirto
class Gui extends JPanel implements ActionListener {

    private MazeGen m;
    private char[][] list; // sisältää labyrintin taulukkona
    private Timer timer;
    private int alkuX = 50; //x-koordinaatti mistä labyrintti alkaa
    private int alkuY = 50; //y-koordinaatti mistä labyrintti alkaa
    private int pituus = 20; //ruudukon neliön sivun pituus
    private int nopeus = 1000; // timerin delay
    private boolean pause = false; // kun true niin ei päivitä grafiikoita
    private ImageIcon iconPath;
    private ImageIcon iconTrap;
    private ImageIcon iconAgent;
    private ImageIcon iconCoin;
    private ImageIcon iconWall;
    private ImageIcon iconStart;
    private ImageIcon iconGoal;
    private int seed;
    private int netNUM = 0;
    private Population pop;
    private Agent a;
    private boolean training;

    public Gui(Population p, MazeGen maz, boolean training, int seed) {
        this.training = training;
        this.seed = seed;
        pop = p;
        m = maz;
        list = maz.getLevel();
        a = pop.getInvidual(netNUM);
        setVariables();
        this.setLayout(new FlowLayout());
        this.timer = new Timer(nopeus, this); // 1000 = 1 sekunti
        timer.start(); //aloittaa timerin
        Mouse mouse = new Mouse();
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        asetaKuvat();
    }

    //Poistaa timerin ja mazen, käytetään kun poistutaan labyrinttinäkymästä
    public void poista() {
        this.m = null;
        this.timer = null;
    }

    // Setterit
    public void setPituus(int p) {
        this.pituus = p;
        asetaKuvat(); //muuttaa kuvien kokoa
        repaint();
    }

    public void setNopeus(int n) {
        if (n == 0 && pop.getGeneration() >= 500) {
            n = 1;
        }
        nopeus = n;
        if (n == 2000) {
            pause = true;
            return;
        } else {
            pause = false;
        }
        timer.setDelay(n);
        System.out.println(timer.getDelay());
    }

    // Asettaa kuvat ja skaalaa ne oikean kokoisiksi
    private void asetaKuvat() {
        iconPath = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/path.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconTrap = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/trap.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconAgent = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/agent.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconCoin = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/coin.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconWall = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/wall.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconStart = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/start.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
        iconGoal = new ImageIcon(new ImageIcon(getClass().getResource("/Simulation/images/goal.png")).getImage().getScaledInstance(pituus, pituus, Image.SCALE_DEFAULT));
    }

    // Getterit
    public int getSeed() {
        return seed;
    }

    // Graafisen labyrintin siirtäminen hiirellä
    private class Mouse implements MouseListener, MouseMotionListener {

        // Clicked, Entered, Exited ja Moved ei tarvita
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        // Released, Pressed ja Dragged käytetään siirtämään labyrintin sijaintia ruudulla
        @Override
        public void mouseReleased(MouseEvent e) {
            if (nopeus != 0) {
                pause = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            pause = true;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int xx = e.getX();
            int yy = e.getY();
            alkuX = xx - list.length / 2 * pituus;
            alkuY = yy - list.length / 2 * pituus;
            repaint();
        }

    }

    // Timerin tapahtuma
    @Override
    public void actionPerformed(ActionEvent e) {
        if (pause) {
            return;
        }
        if (e.getSource() == timer && pop.getGeneration() >= 500) {
            //m.printLevel(); //printtaa labyrintin
            repaint();
        } else {
            update();
        }
    }

    // Labyrintin piirto
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        for (int j = 0; j < list.length; j++) {
            for (int i = 0; i < list[0].length; i++) {
                g.setColor(Color.red);
                switch (list[j][i]) {
                    case ' ':
                        //g.drawRect(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconPath.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    case '#':
                        //g.setColor(Color.black);
                        //g.fillRect(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconWall.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    case 'c':
                        //g.setColor(Color.yellow);
                        //g.fillOval(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconPath.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        iconCoin.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    case 't':
                        //g.setColor(Color.blue);
                        //g.fillRect(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconPath.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        iconTrap.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    case 'S':
                        //g.setColor(Color.green);
                        //g.fillRect(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconStart.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    case 'A':
                        //g.setColor(Color.pink);
                        //g.fillRect(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconAgent.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                    default:
                        //g.fillOval(alkuX+pituus*j,alkuY+pituus*i,pituus,pituus);
                        iconPath.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        iconGoal.paintIcon(this, g, alkuX + pituus * j, alkuY + pituus * i);
                        break;
                }
            }
        }
        update();
    }

    //päivittää tilan
    public void update() {
        if (a != null) {
            if (m.getCoins() == 0 || a.getHealth() <= 0 || (!training && a.getX() == m.getGoalX() && a.getY() == m.getGoalY())) {
                netNUM++;
                a = pop.getInvidual(netNUM);
                generateLevel();
                setVariables();
                return;
            }
        }
        if (a == null) {
            pop.updateGeneration();
            netNUM = 0;
            a = pop.getInvidual(netNUM);
            generateLevel();
            setVariables();
        } else {
            makeMove();
            a.addStep();
        }
    }

    //tehdään siirto
    public void makeMove() {
        double netOutput[] = a.computeNextState();
        int dir[] = new int[]{1, -1, 1, -1};
        double bestMove = 0;
        int dirIndex = dir[0];
        for (int i = 0; i < netOutput.length; i++) {
            if (bestMove < netOutput[i]) {
                bestMove = netOutput[i];
                dirIndex = i;
            }
        }
        legality(dirIndex, dir);
    }

    //onko siirto laillinen
    public void legality(int i, int dir[]) {
        if (i == 0 && list[a.getX()][a.getY() + dir[i]] != '#') {
            list[a.getX()][a.getY()] = ' ';
            a.setY(a.getY() + dir[i]);
            checkSquare();
        } else if (i == 1 && list[a.getX()][a.getY() + dir[i]] != '#') {
            list[a.getX()][a.getY()] = ' ';
            a.setY(a.getY() + dir[i]);
            checkSquare();
        } else if (i == 2 && list[a.getX() + dir[i]][a.getY()] != '#') {
            list[a.getX()][a.getY()] = ' ';
            a.setX(a.getX() + dir[i]);
            checkSquare();
        } else if (i == 3 && list[a.getX() + dir[i]][a.getY()] != '#') {
            list[a.getX()][a.getY()] = ' ';
            a.setX(a.getX() + dir[i]);
            checkSquare();
        } else {
            a.decreaseHealth(false, true);
        }
    }

    // tarkastaa onko ruudussa ansaa tai kolikkoa.
    public void checkSquare() {
        if (list[a.getX()][a.getY()] == 't') {
            a.increaseMines();
            a.decreaseHealth(true, false);
        } else if (list[a.getX()][a.getY()] == 'c') {
            a.increaseCoins();
            a.increaseHealth();
            m.decreaseCoins();
        } else {
            a.decreaseHealth(false, false);
        }
        list[a.getX()][a.getY()] = 'A';
        a.setVisitedSquare();
        a.setMap(list);
    }

    public void setVariables() {
        if (a != null) {
            a.setX(m.getStartX());
            a.setY(m.getStartY());
            a.setGoalx(m.getGoalX());
            a.setGoaly(m.getGoalY());
            list[m.getStartX()][m.getStartY()] = 'A';
            a.setMap(list);
            a.coinsInmap(m.getMaxcoins());
        }
    }

    public void generateLevel() {
        if (training) {
            list = m.generateTrainingLevel();
        } else {
            list = m.generate();
        }
    }

}
