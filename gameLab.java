import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.time.Instant;
import java.time.Duration;

public class gameLab {
    public static boolean isOngoing = false;
    public static boolean starting = true;

    // Variables
    private final Color CANVAS = Color.WHITE;
    private Color playerColor = Color.RED;
    private static final int ROWS = 300;
    private static final int COLS = 200;

    int[][] grid = new int[ROWS][COLS];
    gameDisplay display;

    // set colors
    Color[] playerColors = {Color.RED, Color.ORANGE, Color.PINK, Color.GREEN, Color.BLUE};

    // set current player
    int currentPlayer = 0;

    public gameLab(int numRows, int numCols, String topic) {
        String title = "Divide & Doodle";
        display = new gameDisplay(title, numRows, numCols, topic);
    }

    public static void main(String[] args) throws InterruptedException {
        // Creates game and calls run
        isOngoing = true;
        String topic = WordGenerator.getWord();
        gameLab lab = new gameLab(ROWS, COLS, topic);
        lab.run();
    }

    public void run() throws InterruptedException {
        // set start time
        Instant start = Instant.now();
        int timeDiff = 0;

        // set setting
        double modeTime = 0.0; // default value
        int playersNum = 0; // default value

        // While Loop
        while (isOngoing) {
            // if game just started
            if (starting) {
                // set game-mode variables here
                starting = false;

                while (modeTime == 0.0 || playersNum == 0) {
                    modeTime = display.getMode();
                    playersNum = display.getNumPlayers();
                    System.out.println(playersNum + "   " + modeTime);
                }
                Thread.sleep(5000);
                modeTime = display.getMode();
                playersNum = display.getNumPlayers();

                // reset start time
                start = Instant.now();
            }

            updateDisplay();
            display.repaint();
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null) { //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1]);
            }

            // Check Time elapsed
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            double timeElapsed = (double)duration.toSeconds();
            timeElapsed -= timeDiff;

            // Update clock
            display.updateClock(timeElapsed);

            // check if game has ended
            if (timeElapsed > modeTime) {
                isOngoing = false;
                display.gameOver();
                break;
            }

            // check if timeElapsed has been gameModeTime / numPlayers
            if (Math.abs(timeElapsed - ((modeTime / (double) playersNum) * (currentPlayer + 1))) < 0.67 && currentPlayer < playersNum-1) {
                // change player
                currentPlayer++;
                // switch color
                playerColor = playerColors[currentPlayer];
                // adjust time
                timeDiff += 5;
                // display in-change cutscene for 5 seconds (put hide-able text here TODO)
                display.changePlayers(currentPlayer);
            }
        }
    }

    public void updateDisplay() {
        // sets all the colors in the grid
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int element = grid[r][c];
                if (element == 0) {
                    display.setColor(r, c, CANVAS);
                }
                else {
                    display.setColor(r, c, playerColors[element - 1]);
                }
            }
        }
    }

    public void locationClicked(int row, int col) {
        // colors in the location clicked on the grid
        int COLORED = currentPlayer + 1;
        if (row == 0 || row == ROWS - 1 || col == 0 || col == COLS - 1) {
            grid[row][col] = COLORED;
        }
        else {
            grid[row][col] = COLORED;
            grid[row+1][col] = COLORED;
            grid[row-1][col] = COLORED;
            grid[row][col+1] = COLORED;
            grid[row][col-1] = COLORED;
            grid[row-1][col-1] = COLORED;
            grid[row+1][col+1] = COLORED;
            grid[row-1][col+1] = COLORED;
            grid[row+1][col-1] = COLORED;
        }
    }
}