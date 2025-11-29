import java.util.Arrays;
import java.util.Scanner;

public class Lab7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of strings: ");
        int n = sc.nextInt();
        sc.nextLine(); // зчитуємо Enter після числа

        String[] arr = new String[n];

        // Заповнення масиву рядками
        for (int i = 0; i < n; i++) {
            System.out.print("Enter string " + (i + 1) + ": ");
            arr[i] = sc.nextLine();
        }

        // Обчислення середньої довжини за допомогою Stream API
        // Використовуємо mapToInt, щоб взяти довжину кожного рядка
        double avg = Arrays.stream(arr)
                .mapToInt(String::length)
                .average()
                .orElse(0);

        System.out.println("\nAverage length = " + avg);

        // Отримання рядків, коротших за середню
        // filter залишає лише ті елементи, що задовольняють умову
        String[] shorter = Arrays.stream(arr)
                .filter(s -> s.length() < avg)
                .toArray(String[]::new);

        System.out.println("\nStrings shorter than average:");
        for (String s : shorter) {
            System.out.println(s + " (length = " + s.length() + ")");
        }

        // Отримання рядків, довших за середню
        String[] longer = Arrays.stream(arr)
                .filter(s -> s.length() > avg)
                .toArray(String[]::new);

        System.out.println("\nStrings longer than average:");
        for (String s : longer) {
            System.out.println(s + " (length = " + s.length() + ")");
        }

        sc.close();
    }
}
