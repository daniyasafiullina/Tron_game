import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameBoard extends JPanel implements ActionListener {
    private final int GRID_SIZE = 20;
    private final Timer gameTimer;
    private final Timer elapsedTimeTimer;
    private final TronGame parentFrame;
    private final int rows, cols;
    private final Player player1, player2;
    private final HighScoreManager highScoreManager;
    private int elapsedSeconds;

    public GameBoard(Level level, Player player1, Player player2, TronGame parentFrame) {
        this.rows = level.getRows();
        this.cols = level.getCols();
        this.player1 = player1;
        this.player2 = player2;
        this.parentFrame = parentFrame;
        this.highScoreManager = new HighScoreManager();
        this.elapsedSeconds = 0;

        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(cols * GRID_SIZE, rows * GRID_SIZE));

        setupKeyBindings();

        gameTimer = new Timer(100, this);
        elapsedTimeTimer = new Timer(1000, e -> elapsedSeconds++);

        gameTimer.start();
        elapsedTimeTimer.start();
    }

    //Player 1
    private void setupKeyBindings() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "Player1_Up");
        getActionMap().put("Player1_Up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player1.getDirection().equals("DOWN")) player1.setDirection("UP");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "Player1_Down");
        getActionMap().put("Player1_Down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player1.getDirection().equals("UP")) player1.setDirection("DOWN");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "Player1_Left");
        getActionMap().put("Player1_Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player1.getDirection().equals("RIGHT")) player1.setDirection("LEFT");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "Player1_Right");
        getActionMap().put("Player1_Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player1.getDirection().equals("LEFT")) player1.setDirection("RIGHT");
            }
        });

        //Player 2
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "Player2_Up");
        getActionMap().put("Player2_Up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player2.getDirection().equals("DOWN")) player2.setDirection("UP");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "Player2_Down");
        getActionMap().put("Player2_Down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player2.getDirection().equals("UP")) player2.setDirection("DOWN");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "Player2_Left");
        getActionMap().put("Player2_Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player2.getDirection().equals("RIGHT")) player2.setDirection("LEFT");
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "Player2_Right");
        getActionMap().put("Player2_Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player2.getDirection().equals("LEFT")) player2.setDirection("RIGHT");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player1.move();
        player2.move();

        if (checkCollision(player1, player2) || checkCollision(player2, player1)) {
            gameTimer.stop();
            elapsedTimeTimer.stop();

            Player winner = checkCollision(player1, player2) ? player2 : player1;
            highScoreManager.updateWinner(winner.getName(), elapsedSeconds);

            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Game Over! " + winner.getName() + " wins!\nElapsed Time: " + elapsedSeconds + " seconds\nDo you want to restart?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                parentFrame.dispose();
                SwingUtilities.invokeLater(() -> new TronGame().setVisible(true));
            } else {
                System.exit(0);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x <= cols * GRID_SIZE; x += GRID_SIZE) {
            g.drawLine(x, 0, x, rows * GRID_SIZE);
        }
        for (int y = 0; y <= rows * GRID_SIZE; y += GRID_SIZE) {
            g.drawLine(0, y, cols * GRID_SIZE, y);
        }

        player1.draw(g, GRID_SIZE);
        player2.draw(g, GRID_SIZE);

        g.setColor(Color.WHITE);
        g.drawString("Elapsed Time: " + elapsedSeconds + " s", 10, 20);
    }

    private boolean checkCollision(Player currentPlayer, Player opponent) {
        if (currentPlayer.getX() < 0 || currentPlayer.getX() >= cols || currentPlayer.getY() < 0 || currentPlayer.getY() >= rows) {
            return true;
        }

        for (Point point : opponent.getLightTrace()) {
            if (currentPlayer.getX() == point.x && currentPlayer.getY() == point.y) {
                return true;
            }
        }

        return false;
    }

}
