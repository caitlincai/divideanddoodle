import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;

public class gameDisplay extends JPanel implements MouseListener,
        MouseMotionListener, ActionListener, ChangeListener {
    //Variables
    private int cellSize;
    private Image image;
    private JFrame frame;
    private int numRows;
    private int numCols;
    private int[] mouseLoc;
    private JSlider slider;
    private int numPlayers;
    private int mode;
    private JButton[] buttons;
    private JLabel clock;

    public gameDisplay(String title, int numRows, int numCols, String topic) {
        // Set Instance Variables
        this.numRows = numRows;
        this.numCols = numCols;

        // Determine Cell Size
        cellSize = Math.max(3, 600 / Math.max(numRows, numCols));
        image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);

        // Create Frame
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        // Display topic
        JPanel topicPanel = new JPanel();
        JLabel topicLabel = new JLabel("Topic: " + topic);
        topicPanel.add(topicLabel);
        frame.add(topicPanel, BorderLayout.PAGE_START);
        topicLabel.setFont(new Font("Arial", Font.BOLD, 20)); 
        
        // Add Clock TODO
        JPanel clockPanel = new JPanel();
        clock = new JLabel("Time: " + 0.0);
        clock.setFont(new Font("Arial", Font.BOLD, 20)); 
        clockPanel.add(clock);
        frame.add(clockPanel, BorderLayout.PAGE_START);

        // Instructions
        JPanel p = new JPanel();
        JPanel p2 = new JPanel();
        p.add(new JLabel("Instructions: Players will take turns drawing on the same computer to create a picture"));
        p2.add(new JLabel("of the topic provided at the top. Select a difficulty and number of players to start."));
        frame.add(p, BorderLayout.CENTER);
        frame.add(p2, BorderLayout.CENTER);

        // Layout Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        frame.getContentPane().add(topPanel);

        // Set size and mouse trackers
        setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
        addMouseListener(this);
        addMouseMotionListener(this);
        topPanel.add(this);

        // Create bottom panel for colors, time, # of players
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        topPanel.add(bottomPanel);

        // Adding Slider for Amount of Players
        slider = new JSlider(JSlider.HORIZONTAL, 2, 5, 3);
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        for (int i = 2; i < 6; i++) {
            String x = "" + i;
            labelTable.put(new Integer(i), new JLabel(x));
        }
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        frame.getContentPane().add(slider);
        JPanel l = new JPanel();
        l.add(new JLabel("Select Number of Players"));
        frame.add(l, BorderLayout.CENTER);

        // Create Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        topPanel.add(buttonPanel);
        buttons = new JButton[3];

        buttons[0] = new JButton("Easy");
        buttons[0].setActionCommand("45");
        buttons[0].addActionListener(this);
        buttonPanel.add(buttons[0]);

        buttons[1] = new JButton("Medium");
        buttons[1].setActionCommand("30");
        buttons[1].addActionListener(this);
        buttonPanel.add(buttons[1]);

        buttons[2] = new JButton("Hard");
        buttons[2].setActionCommand("20");
        buttons[2].addActionListener(this);
        buttonPanel.add(buttons[2]);

        // Finish Creating the New Frame
        frame.pack();
        frame.setVisible(true);
    }

    private int[] toLocation(MouseEvent e)
    {
        int row = e.getY() / cellSize;
        int col = e.getX() / cellSize;
        if (row < 0 || row >= numRows || col < 0 || col >= numCols)
            return null;
        int[] loc = new int[2];
        loc[0] = row;
        loc[1] = col;
        return loc;
    }

    public void setColor(int row, int col, Color color)
    {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mode = Integer.parseInt(e.getActionCommand());
        for (JButton button : buttons)
            button.setSelected(false);
        ((JButton)e.getSource()).setSelected(true);
    }

    public int getMode() {
        return mode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseLoc = toLocation(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseLoc = toLocation(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLoc = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseLoc = toLocation(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    public void pause(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public int[] getMouseLocation() {
        return mouseLoc;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        numPlayers = slider.getValue();
    }

    //returns number of times to step between repainting and processing mouse input
    public int getNumPlayers() {
        return numPlayers;
    }

    public void updateClock(double timeElapsed) {
        clock.setText("Time: " + timeElapsed);
    }

    public void displayMessage(String message) throws InterruptedException {
        // Display message
        JPanel panelBlue = new JPanel();
        panelBlue.setBackground(new Color(195, 235, 237));
        panelBlue.setLayout(new BorderLayout()); // Layout for the panel to center the label

        // Create a label with the player change message
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); 

        // Add the label to the panel
        panelBlue.add(label, BorderLayout.CENTER);

        // Add the panel at the top of the frame (using BorderLayout.NORTH)
        frame.add(panelBlue, BorderLayout.NORTH);

        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();

        // Show the frame
        frame.setVisible(true);

        // Show the panel for 5 seconds
        Thread.sleep(5000);
        frame.remove(panelBlue);

        // Revalidate and repaint to update the frame's UI
        frame.revalidate();
        frame.repaint();
    }

    public void changePlayers(int newPlayer) throws InterruptedException {
        newPlayer++;
        displayMessage("Change to Player " + newPlayer);
    }
    
    public void gameOver() throws InterruptedException {
        displayMessage("GAME OVER");
    }
}