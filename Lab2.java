import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// Клас, що описує один запис у журналі куратора
class JournalEntry {
    private String lastName;
    private String firstName;
    private String birthDate;
    private String phone;
    private String address;

    public JournalEntry(String lastName, String firstName, String birthDate, String phone, String address) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Last Name: " + lastName + ", First Name: " + firstName +
               ", Birth Date: " + birthDate + ", Phone: " + phone +
               ", Address: " + address;
    }
}

// Основний клас лабораторної роботи
public class Lab2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<JournalEntry> journal = new ArrayList<>();
        boolean adding = true;

        while (adding) {
            System.out.println("\n--- Add a new journal entry ---");

            String lastName = readNonEmpty(scanner, "Student's Last Name: ");
            String firstName = readNonEmpty(scanner, "Student's First Name: ");
            String birthDate = readValidDate(scanner, "Birth Date (dd.mm.yyyy): ");
            String phone = readValidPattern(scanner, "Phone (numbers only): ", "\\d+", 
                "Phone must contain only numbers. Please try again.");
            String address = readNonEmpty(scanner, "Address (street, house, apartment): ");

            journal.add(new JournalEntry(lastName, firstName, birthDate, phone, address));
            System.out.println("Entry successfully added!");

            // Запит, чи продовжувати введення
            while (true) {
                System.out.print("Do you want to add another entry? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("yes")) {
                    break; // продовжуємо цикл
                } else if (choice.equals("no")) {
                    adding = false; // виходимо з зовнішнього циклу
                    break;
                } else {
                    System.out.println("Please enter 'yes' or 'no'.");
                }
            }
        }

        // Вивід усіх записів
        System.out.println("\n--- All journal entries ---");
        if (journal.isEmpty()) {
            System.out.println("No entries found.");
        } else {
            for (int i = 0; i < journal.size(); i++) {
                System.out.println((i + 1) + ". " + journal.get(i));
            }
        }

        scanner.close();
    }

    // Метод для введення непорожнього рядка
    private static String readNonEmpty(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) break;
            System.out.println("Field cannot be empty. Please try again.");
        }
        return input;
    }

    // Метод для введення з перевіркою по шаблону
    private static String readValidPattern(Scanner scanner, String prompt, String pattern, String errorMsg) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (Pattern.matches(pattern, input)) break;
            System.out.println(errorMsg);
        }
        return input;
    }

    // Метод для перевірки дати (формат і реальність дати)
    private static String readValidDate(Scanner scanner, String prompt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false); // Забороняє неіснуючі дати
        String input;

        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                sdf.parse(input); // Перевірка чи дата існує
                break;
            } catch (ParseException e) {
                System.out.println("Invalid date format or non-existent date. Please use dd.mm.yyyy and try again.");
            }
        }
        return input;
    }
}

