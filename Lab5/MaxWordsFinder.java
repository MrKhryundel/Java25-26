import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Клас для знаходження рядка з максимальною кількістю слів у файлі
 */
public class MaxWordsFinder {

    /**
     * Метод повертає рядок з найбільшою кількістю слів
     */
    public static String findMaxWordsLine(String filename) {
        String maxLine = "";
        int maxWords = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int wordCount = line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
                if (wordCount > maxWords) {
                    maxWords = wordCount;
                    maxLine = line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return maxLine;
    }
}


