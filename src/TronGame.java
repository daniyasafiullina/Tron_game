import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TronGame extends JFrame {
    public TronGame() {
        setTitle("Tron Light-Motorcycle Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        displayMainMenu();
    }

    private void displayMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Tron Game");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start a Game");
        JButton highScoresButton = new JButton("High Scores");
        JButton exitButton = new JButton("Exit");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(e -> startGame());
        highScoresButton.addActionListener(e -> showHighScores(this));
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(highScoresButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(exitButton);

        setContentPane(panel);
    }

    private void startGame() {
        // Player 1
        String player1Name = JOptionPane.showInputDialog(this, "Player 1, enter your name:");
        if (player1Name == null || player1Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Player 1 name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Color player1Color = chooseColor("Player 1, choose your color:");
        if (player1Color == null) {
            JOptionPane.showMessageDialog(this, "Player 1 setup was canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Player 2
        String player2Name = JOptionPane.showInputDialog(this, "Player 2, enter your name:");
        if (player2Name == null || player2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Player 2 name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Color player2Color = chooseColor("Player 2, choose your color:");
        if (player2Color == null) {
            JOptionPane.showMessageDialog(this, "Player 2 setup was canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] levels = {
                "Level 1 - Easiest",
                "Level 2",
                "Level 3",
                "Level 4",
                "Level 5",
                "Level 6",
                "Level 7",
                "Level 8",
                "Level 9",
                "Level 10 - Hardest"
        };
        String selectedLevel = (String) JOptionPane.showInputDialog(
                this,
                "Choose a level:",
                "Level Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                levels,
                levels[0]
        );

        if (selectedLevel == null) {
            JOptionPane.showMessageDialog(this, "Game setup was canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int levelNumber = Integer.parseInt(selectedLevel.split(" ")[1]);
        Level level;
        try {
            level = LevelLoader.loadLevel("levels/level" + levelNumber + ".txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load level!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player player1 = new Player(player1Name, player1Color, "RIGHT", 5, 5);
        Player player2 = new Player(player2Name, player2Color, "LEFT", level.getCols() - 5, level.getRows() - 5);

        GameBoard gameBoard = new GameBoard(level, player1, player2, this);
        setContentPane(gameBoard);

        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private Color chooseColor(String message) {
        String[] colors = {"RED", "BLUE", "GREEN", "YELLOW", "CYAN", "MAGENTA", "ORANGE"};
        String selectedColor = (String) JOptionPane.showInputDialog(
                this,
                message,
                "Choose Color",
                JOptionPane.QUESTION_MESSAGE,
                null,
                colors,
                colors[0]
        );

        if (selectedColor == null) return null;

        try {
            return (Color) Color.class.getField(selectedColor).get(null);
        } catch (Exception e) {
            return Color.RED;
        }
    }


    private void showHighScores(Component parent) {
        HighScoreManager manager = new HighScoreManager();
        java.util.List<String> scores = manager.getHighScores();

        StringBuilder scoreList = new StringBuilder("Top 10 High Scores:\n\n");
        for (String score : scores) {
            scoreList.append(score).append("\n");
        }

        JOptionPane.showMessageDialog(
                parent,
                scoreList.toString(),
                "High Scores",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TronGame().setVisible(true));
    }
}
