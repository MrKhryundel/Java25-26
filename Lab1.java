import java.util.Scanner;

public class Lab1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Введення розміру масиву
        System.out.print("Enter the number of strings: ");
        int n = sc.nextInt();
        sc.nextLine(); // зчитуємо Enter після числа

        String[] arr = new String[n];

        // Заповнення масиву рядками
        for (int i = 0; i < n; i++) {
            System.out.print("Enter string " + (i + 1) + ": ");
            arr[i] = sc.nextLine();
        }

        // Знаходимо середню довжину
        int sum = 0;
        for (String s : arr) {
            sum += s.length();
        }
        double avg = (double) sum / n;

        System.out.println("Average string length = " + avg);

        // Виводимо ті що менші за середню
        System.out.println("Strings shorter than average:");
        for (String s : arr) {
            if (s.length() < avg) {
                System.out.println(s + " (length = " + s.length() + ")");
            }
        }

        // Виводимо ті що більші за середню
        System.out.println("Strings longer than average:");
        for (String s : arr) {
            if (s.length() > avg) {
                System.out.println(s + " (length = " + s.length() + ")");
            }
        }

        sc.close();
    }
}
