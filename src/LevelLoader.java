import java.io.*;

public class LevelLoader {
    public static Level loadLevel(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] dimensions = br.readLine().split(" ");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);
            return new Level(rows, cols);
        }
    }
}
