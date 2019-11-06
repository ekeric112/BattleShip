package com.company;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SetShipMenu extends JFrame {
    private final int TOTAL_SHIP_LENGTH = 17;
    private final int SIZE = 8;
    private final int TOTAL_SHIP_NUM = 5;
    private final int BUTTON_SIZE = 34;
    private final int DIM_WIDTH = 800;
    private final int DIM_HEIGHT = 400;
    private final int TIMER_DELAY = 500;
    private int shipIndex;
    private int shipCount;
    private int shipsDestroyed;
    private int count = 0;
    private Ship[] shipArray;
    private Ship[][] shipGrid;
    private Ship[][] targetGrid;
    private Dimension dim;
    private JPanel panel;
    private JPanel settingPanel;
    private JPanel buttonsMenu;
    private JPanel shipPanel;
    private JPanel targetPanel;
    private ActionListener btnListener;
    private TimerListener timerListener;
    private JButton start;
    private JButton reset;
    private JButton randomize;
    private Grid grid;
    private Point point;
    private Carrier car;
    private Battleship bat;
    private Submarine sub;
    private Destroyer des;
    private PatrolBoat pat;
    private Carrier carAI;
    private Battleship batAI;
    private Submarine subAI;
    private Destroyer desAI;
    private PatrolBoat patAI;
    private Timer timer;
    private JButton shipFieldGUI[][] = new JButton[SIZE][SIZE];
    private JButton targetFieldGUI[][] = new JButton[SIZE][SIZE];


    public SetShipMenu() throws IOException {
        createComponents();
        wireComponents();
    }

    private void createComponents() throws IOException {
        timerListener = new TimerListener();
        shipCount = 0;
        shipsDestroyed = 0;
        shipIndex = -1;
        shipPanel = new JPanel();
        targetPanel = new JPanel();
        shipArray = new Ship[5];
        panel = new JPanel(new BorderLayout());
        settingPanel = new JPanel(new GridLayout(6, 1));
        buttonsMenu = new JPanel(new GridLayout(2, 1));
        timer = new Timer(TIMER_DELAY, timerListener);
        shipGrid = new Ship[SIZE][SIZE];
        targetGrid = new Ship[SIZE][SIZE];
        start = new JButton("Deploy Ships");
        reset = new JButton("Reset Board");
        randomize = new JButton("Shuffle");

        //for putting the game in the center of the screen
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        car = new Carrier(5, grid, this);
        bat = new Battleship(4, grid, this);
        sub = new Submarine(3, grid, this);
        des = new Destroyer(3, grid, this);
        pat = new PatrolBoat(2, grid, this);

        //creates the menu for horizontal/vertical per ship
        JPanel carPanel = createShipPanel(car);
        JPanel batPanel = createShipPanel(bat);
        JPanel subPanel = createShipPanel(sub);
        JPanel desPanel = createShipPanel(des);
        JPanel patPanel = createShipPanel(pat);
        //reset count for when user clicks deploy ship
        count = 0;

        createPanel(shipPanel);
        buildFields(shipPanel, "Ship");
        createPanel(targetPanel);
        buildFields(targetPanel, "Target");

        settingPanel.add(carPanel);
        settingPanel.add(batPanel);
        settingPanel.add(subPanel);
        settingPanel.add(desPanel);
        settingPanel.add(patPanel);
        settingPanel.add(start);

        buttonsMenu.add(randomize);
        buttonsMenu.add(reset);

        panel.add(settingPanel, BorderLayout.WEST);
        panel.add(shipPanel, BorderLayout.CENTER);
        panel.add(buttonsMenu, BorderLayout.EAST);


        add(panel);
        setTitle("Battleship");
        setPreferredSize(new Dimension(DIM_WIDTH, DIM_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        //puts game in center of screen
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setResizable(false);
        //creates the target grid, but is not visible until user starts game
        createTargetGrid(targetGrid);
    }

    private void wireComponents() {
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button;
                for (int row = 0; row < SIZE; row++) {
                    for (int col = 0; col < SIZE; col++) {
                        //checks if all ships are on the grid
                        if (shipGrid[row][col] == car) {
                            shipCount++;
                        }
                        if (shipGrid[row][col] == bat) {
                            shipCount++;
                        }
                        if (shipGrid[row][col] == pat) {
                            shipCount++;
                        }
                        if (shipGrid[row][col] == des) {
                            shipCount++;
                        }
                        if (shipGrid[row][col] == sub) {
                            shipCount++;
                        }
                        //disables buttons on the user's grid
                        button = getField(row, col, shipFieldGUI);
                        button.setEnabled(false);
                    }
                }

                if (shipCount == TOTAL_SHIP_LENGTH) {
                    grid = new Grid(shipGrid, targetGrid, shipFieldGUI, shipPanel, targetPanel);
                    setVisible(false);
                    grid.setVisible(true);
                }
            }
        });

        randomize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGrid();
                try {
                    createRandomShipGrid();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGrid();
            }
        });
    }

    /**
     * resets the grid if user messes up
     */
    private void resetGrid() {
        resetShipCount();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                removeFields(row, col, true, 1);
                shipGrid[row][col] = null;
            }
        }

        panel.remove(shipPanel);
        shipPanel = new JPanel();
        createPanel(shipPanel);
        buildFields(shipPanel, "ship");
        panel.add(shipPanel, BorderLayout.CENTER);
        add(panel);
        panel.revalidate();
        panel.repaint();
        printShipGrid(shipGrid);
    }


    /**
     * resets the ship count
     */
    private void resetShipCount() {
        car.setShipCount(0);
        bat.setShipCount(0);
        des.setShipCount(0);
        pat.setShipCount(0);
        sub.setShipCount(0);
    }

    class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            grid.aiTurn();
            timer.stop();
        }
    }

    /**
     * ButtonListener for the target board
     */
    class TargetBtnListener implements ActionListener {
        Point p;
        int row;
        int col;
        JButton button;
        Ship tempShip;

        public TargetBtnListener(Point p) {
            this.p = p;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (shipsDestroyed < TOTAL_SHIP_NUM) {
                row = (int) p.getX();
                col = (int) p.getY();
                tempShip = targetGrid[row][col];
                button = getField(row, col, targetFieldGUI);
                button.setEnabled(false);
                if (tempShip == null) {
                    button.setBackground(new Color(251, 255, 255));
                } else {
                    if (tempShip.Sink()) {
                        for (int row = 0; row < SIZE; row++) {
                            for (int col = 0; col < SIZE; col++) {
                                if (tempShip == targetGrid[row][col]) {
                                    getField(row, col, targetFieldGUI).setBackground(new Color(94, 1, 0));
                                }
                            }
                        }
                        shipsDestroyed++;
                        if (shipsDestroyed == TOTAL_SHIP_NUM) {
                            grid.displayWin();
                        }
                    } else {
                        getField(row, col, targetFieldGUI).setBackground(new Color(255, 0, 0));
                    }
                }
            }
            timer.start();
        }
    }


    /**
     * Btn listener for the grid buttons
     */
    class GridBtnListener implements ActionListener {
        Point p;

        public GridBtnListener(Point p) {
            this.p = p;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                //takes the ship from the ShipArray and puts it on the grid
                if (shipIndex > -1) {
                    shipArray[shipIndex].placeShip(p, shipGrid);
                    System.out.println("Ship placed: " + shipArray[shipIndex].getType());
                    System.out.println("Button pos clicked " + p.getX() + ", " + p.getY());
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * creates a single box with combo box for ship
     *
     * @param ship given ship
     * @return a panel for the ship menu to select vertical/horizontal
     */
    private JPanel createShipPanel(Ship ship) {
        shipArray[count] = ship;
        count++;

        JPanel combo = createComboBox(ship);
        JPanel panel = new JPanel();

        panel.add(combo);
        panel.setBorder(new TitledBorder(new EtchedBorder(), ship.getType()));
        return panel;
    }

    /**
     * creates the combo box for given ship
     *
     * @param tempShip the given ship
     * @return a combo box with two options
     */
    private JPanel createComboBox(Ship tempShip) {
        JComboBox orient = new JComboBox();
        orient.addItem("");
        orient.addItem("Vertical");
        orient.addItem("Horizontal");
        orient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean vertical;
                String selectedString = (String) orient.getSelectedItem();
                vertical = selectedString.equals("Vertical");

                for (int i = 0; i < shipArray.length; i++) {
                    if (shipArray[i].getType().equals(tempShip.getType())) {
                        //so it knows which ship to put down
                        shipIndex = i;
                    }
                }

                tempShip.setVertical(vertical);
            }
        });

        JPanel panel = new JPanel();
        panel.add(orient);
        return panel;
    }

    /**
     * creates panel for which the user to put down ships
     * @param panel the panel to be created
     */
    private void createPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(BUTTON_SIZE * 10, BUTTON_SIZE * 10));
        panel.setBackground(new Color(84, 232, 37));
        panel.setBorder(BorderFactory.createLineBorder(new Color(185, 13, 19)));
    }

    /**
     * create the buttons in the grid
     *
     * @param givenPanel the panel to be made
     */
    private void buildFields(JPanel givenPanel, String type) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                point = new Point();
                point.setLocation(col, row);
                if (type.equals("Target")) {
                    btnListener = new TargetBtnListener(point);
                } else {
                    btnListener = new GridBtnListener(point);
                }

                JButton button = new JButton();

                button.setLocation(point);
                button.addActionListener(btnListener);
                button.setBackground(new Color(232, 31, 224));
                button.setBorder(BorderFactory.createLineBorder(new Color(27, 22, 185)));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setPreferredSize(new java.awt.Dimension(BUTTON_SIZE, BUTTON_SIZE));

                if (!type.equals("Target")) {
                    // add button to GUI grid-manager
                    setField(row, col, button, shipFieldGUI);
                } else {
                    setField(row, col, button, targetFieldGUI);
                }

                // set field position
                c.gridx = row;
                c.gridy = col;

                // add field to the grid
                givenPanel.add(button, c);
            }
        }
    }

    /**
     * checks if the current position is available
     *
     * @param p the point to be checked
     * @param vertical to see which direction to check
     * @param length how far to check
     * @return true if the position is available
     */
    public boolean isPositionAvailable(Point p, boolean vertical, int length, Ship[][] grid) {
        int row = (int) p.getX();
        int col = (int) p.getY();
        boolean available = true;
        Ship s;
        if (vertical) {
            for (int i = row; i < length + row; i++) {
                s = grid[i][col];
                if (s != null) {
                    available = false;
                }
            }
        } else {
            for (int i = col; i < length + col; i++) {
                s = grid[row][i];
                if (s != null) {
                    available = false;
                }
            }
        }
        return available;
    }

    /**
     * places ships randomly
     * @throws IOException error if input not found
     */
    private void createRandomShipGrid() throws IOException {
        placeShipRandom(car, shipGrid, "Ship");
        placeShipRandom(bat, shipGrid, "Ship");
        placeShipRandom(sub, shipGrid, "Ship");
        placeShipRandom(des, shipGrid, "Ship");
        placeShipRandom(pat, shipGrid, "Ship");

        printShipGrid(shipGrid);
    }

    /**
     * places ships randomly for AI
     * @param targetGrid grid to placed on
     * @throws IOException error if file not found
     */
    private void createTargetGrid(Ship[][] targetGrid) throws IOException {
        carAI = new Carrier(5, grid, this);
        batAI = new Battleship(4, grid, this);
        subAI = new Submarine(3, grid, this);
        desAI = new Destroyer(3, grid, this);
        patAI = new PatrolBoat(2, grid, this);

        placeShipRandom(carAI, targetGrid, "Target");
        placeShipRandom(batAI, targetGrid, "Target");
        placeShipRandom(subAI, targetGrid, "Target");
        placeShipRandom(desAI, targetGrid, "Target");
        placeShipRandom(patAI, targetGrid, "Target");
        printShipGrid(targetGrid);
    }

    /**
     * places  ship randomly
     * @param ship the ship to be placed
     * @param grid the grid to placed on
     * @param type which grid to be placed on
     * @throws IOException
     */
    private void placeShipRandom(Ship ship, Ship[][] grid, String type) throws IOException {
        int randRow = (int) (Math.random() * SIZE);
        int randCol = (int) (Math.random() * SIZE);
        double verticalRand = Math.random();
        Boolean vertical;
        Point p = new Point(randRow, randCol);
        vertical = verticalRand >= 0.5;

        while (!isPositionValid(p, vertical, ship.getLength()) || !isPositionAvailable(p, vertical, ship.getLength(), grid)) {
            randRow = (int) (Math.random() * SIZE);
            randCol = (int) (Math.random() * SIZE);
            p = new Point(randRow, randCol);
            verticalRand = Math.random();
            vertical = verticalRand >= 0.5;
        }
        ship.setVertical(vertical);
        if (type.equals("Target")) {
            ship.placeTargetShip(p, grid);
        } else {
            ship.placeShip(p, shipGrid);
        }
    }

    /**
     * adds given ship to the ShipGrid
     *
     * @param s the ship to be placed
     * @param p where to place the ship
     * @throws IOException error if file not found
     */
    public void add(Ship s, Point p, Ship[][] grid, String type) throws IOException {
        int row = (int) p.getX();
        int col = (int) p.getY();
        if (s.isVertical()) {
            for (int i = row; i < s.getLength() + row; i++) {
                grid[i][col] = s;
            }
            //if grid placed is not the target ship, display the ship
            if (!type.equals("Target")) {
                displayShip(p, true, s.getLength(), s);
            }

        } else {
            for (int i = col; i < s.getLength() + col; i++) {
                grid[row][i] = s;
            }
            if (!type.equals("Target")) {
                displayShip(p, false, s.getLength(), s);
            }
        }
    }

    /**
     * checks if given point is within the bounds
     * @param point the point to be checked
     * @param vertical orientation of ship
     * @param length length to check
     * @return true if the ship is within the bounds
     */
    public boolean isPositionValid(Point point, boolean vertical, int length) {
        int row = (int) point.getX();
        int col = (int) point.getY();
        boolean available = true;
        if (vertical) {
            if (row + length > SIZE) {
                available = false;
            }
        } else {
            if (col + length > SIZE) {
                available = false;
            }
        }
        return available;
    }

    /**
     * Sets the button field to given button
     * @param row row to placed
     * @param col col to be placed
     * @param btn button to be placed
     * @param field which field to place
     */
    private void setField(int row, int col, JButton btn, JButton field[][]) {
        field[row][col] = btn;
    }

    /**
     * gets button at given coordinates
     * @param col col to get
     * @param row row to get
     * @param field which field to get
     * @return the requested button
     */
    private JButton getField(int col, int row, JButton field[][]) {
        return field[row][col];
    }

    /**
     * Sets the icon for given button
     *
     * @param field which field
     * @param img given image
     */
    private void setFieldIcon(JButton field, BufferedImage img) {
        field.setIcon(new ImageIcon(img));
    }


    /**
     * displays the ship at given coordinates
     *
     * @param point the point to display ship
     * @param vertical orientation of ship
     * @param length length of ship
     * @param s the ship
     * @throws IOException error if ship file is not found
     */
    private void displayShip(Point point, boolean vertical, int length, Ship s) throws IOException {
        int row = (int) point.getX();
        int col = (int) point.getY();

        // create new button
        JButton button = new JButton();
        button.setLayout(null);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(180, 101, 0)));

        // grid prefs for the new button
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // set position
        c.gridx = col;
        c.gridy = row;

        // button dimensions
        int width = BUTTON_SIZE;
        int height = BUTTON_SIZE;

        // occupy ship field in grid
        if (vertical) {
            // vertical
            c.gridwidth = 1;
            c.gridheight = length;
            height = BUTTON_SIZE * length;

        } else {
            // horizontal
            c.gridwidth = length;
            c.gridheight = 1;
            width = BUTTON_SIZE * length;
        }

        // select the ship type's preferences
        button.setPreferredSize(new Dimension(width, height));

        // remove old elements
        removeFields(row, col, vertical, length);
        setFieldIcon(button, s.getIcon(vertical));

        // add new button and update the grid
        shipPanel.add(button, c);
        shipPanel.revalidate();
        shipPanel.repaint();
    }

    /**
     * removes buttons that lie in way of ship
     * @param row row to be removed
     * @param col col to be removed
     * @param vertical orientation of ship
     * @param length length of ship
     */
    private void removeFields(int row, int col, boolean vertical, int length) {
        for (int i = 0; i < length; i++) {
            if (vertical) {
                shipPanel.remove(getField(row + i, col, shipFieldGUI));
            } else {
                shipPanel.remove(getField(row, col + i, shipFieldGUI));
            }
        }
    }

    /**
     * prints out the ship grid
     * @param grid given grid
     */
    private void printShipGrid(Ship[][] grid) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // Your code here
        Ship tempShip;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tempShip = grid[i][j];
                if (tempShip == null) {
                    System.out.print("| ");
                } else {
                    System.out.print("|" + tempShip.getMarker());
                }
            }
            System.out.println("|");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
