import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Клас для підрахунку частоти тегів на веб-сторінці
 */
public class TagCounter {

    public static Map<String, Integer> countTags(String urlString) {
        Map<String, Integer> tagFreq = new HashMap<>();
        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            Pattern tagPattern = Pattern.compile("<\\s*([a-zA-Z0-9]+)\\b");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = tagPattern.matcher(line);
                while (matcher.find()) {
                    String tag = matcher.group(1).toLowerCase();
                    tagFreq.put(tag, tagFreq.getOrDefault(tag, 0) + 1);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading URL: " + e.getMessage());
        }
        return tagFreq;
    }

    public static void printTagsLex(Map<String, Integer> tagFreq) {
        List<String> tags = new ArrayList<>(tagFreq.keySet());
        Collections.sort(tags);
        System.out.println("\nTags in lexicographic order:");
        for (String tag : tags) {
            System.out.println(tag + " : " + tagFreq.get(tag));
        }
    }

    public static void printTagsByFrequency(Map<String, Integer> tagFreq) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(tagFreq.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        System.out.println("\nTags sorted by frequency (ascending):");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}


