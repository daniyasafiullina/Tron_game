import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {
    public void updateWinner(String username, int score) {
        String checkQuery = "SELECT score FROM high_scores WHERE username = ?";
        String insertQuery = "INSERT INTO high_scores (username, score) VALUES (?, ?)";
        String updateQuery = "UPDATE high_scores SET score = GREATEST(score, ?) WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                updateStmt.setInt(1, score);
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();
            } else {
                insertStmt.setString(1, username);
                insertStmt.setInt(2, score);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHighScores() {
        String query = "SELECT username, score FROM high_scores ORDER BY score DESC LIMIT 10";
        List<String> highScores = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                highScores.add(username + " - " + score);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return highScores;
    }
}
