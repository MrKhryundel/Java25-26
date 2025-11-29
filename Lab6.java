import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Основний клас програми
public class Lab6 {

    // Клас перекладача
    static class Translator {
        // Словник англійських та українських слів
        private HashMap<String, String> dictionary;

        // Конструктор
        public Translator() {
            dictionary = new HashMap<>();
        }

        // Метод додавання слова до словника з перевіркою на заміну
        public void addWord(String english, String ukrainian, Scanner scanner) {
            String key = english.toLowerCase();
            if (dictionary.containsKey(key)) {
                String currentTranslation = dictionary.get(key);
                // Запитати користувача, чи хоче замінити
                boolean replace = askYesNo(scanner, 
                    "The word \"" + english + "\" already exists with translation \"" + currentTranslation + "\".\n" +
                    "Do you want to replace it with \"" + ukrainian + "\"? (yes/no)");
                if (replace) {
                    dictionary.put(key, ukrainian);
                    System.out.println("Word replaced!");
                } else {
                    System.out.println("Word not replaced.");
                }
            } else {
                dictionary.put(key, ukrainian);
                System.out.println("Word added!");
            }
        }

        // Метод перекладу фрази
        public String translate(String phrase) {
            StringBuilder result = new StringBuilder();
            String[] words = phrase.split("\\s+");
            for (String word : words) {
                String translated = dictionary.getOrDefault(word.toLowerCase(), word);
                result.append(translated).append(" ");
            }
            return result.toString().trim();
        }

        // Метод для виводу всіх слів у словнику
        public void printDictionary() {
            if (dictionary.isEmpty()) {
                System.out.println("The dictionary is empty.");
                return;
            }
            System.out.println("Current dictionary:");
            for (Map.Entry<String, String> entry : dictionary.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        // Допоміжний метод отримання yes/no від користувача з перевіркою
        private boolean askYesNo(Scanner scanner, String message) {
            while (true) {
                System.out.println(message);
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) return true;
                if (input.equals("no")) return false;
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }

    // Метод для читання числового вибору з меню
    public static int getMenuChoice(Scanner scanner, int min, int max) {
        int choice = -1;
        while (true) {
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Translator translator = new Translator();

        // Додаємо кілька початкових слів
        translator.addWord("hello", "привіт", scanner);
        translator.addWord("world", "світ", scanner);
        translator.addWord("good", "добрий", scanner);
        translator.addWord("morning", "ранок", scanner);
        translator.addWord("back", "назад", scanner);

        System.out.println("Welcome to English-Ukrainian Translator!");

        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Use translator");
            System.out.println("2. Show current dictionary");
            System.out.println("3. Add new words");
            System.out.println("4. Exit");

            int choice = getMenuChoice(scanner, 1, 4);

            switch (choice) {
                case 1:
                    System.out.println("Enter an English phrase to translate:");
                    String phrase = scanner.nextLine();
                    String translated = translator.translate(phrase);
                    System.out.println("Translated phrase: " + translated);
                    break;

                case 2:
                    translator.printDictionary();
                    break;

                case 3:
                    boolean adding = true;
                    while (adding) {
                        System.out.print("Enter English word (or type 'back' to return to main menu): ");
                        String eng = scanner.nextLine().trim();
                        if (eng.equalsIgnoreCase("back")) {
                            adding = false;
                            continue;
                        }
                        System.out.print("Enter Ukrainian translation: ");
                        String ukr = scanner.nextLine().trim();
                        translator.addWord(eng, ukr, scanner);
                    }
                    break;

                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    running = false;
                    break;
            }
        }

        scanner.close();
    }
}





