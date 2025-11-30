import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.ThreadLocalRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;

// Головний клас програми 
public class Lab8 {

    // Загальна кількість ітерацій (точок), які будемо "кидати" у квадрат
    private static final long ITERATIONS = 1_000_000_000L;

    public static void main(String[] args) {

        // --- Перевірка вхідних аргументів 
        // Програма повинна отримати рівно 1 аргумент - кількість потоків
        if (args.length != 1) {
            System.out.println("Usage: java Lab8 <THREADS>");
            return;
        }

        int THREADS;

        try {
            // Перетворення тексту аргумента у число
            THREADS = Integer.parseInt(args[0]);

            // Кількість потоків повинна бути більшою за 0
            if (THREADS <= 0) {
                System.out.println("THREADS must be > 0");
                return;
            }

        } catch (NumberFormatException e) {
            // Якщо аргумент не є числом
            System.out.println("THREADS must be an integer");
            return;
        }

        // Початок заміру часу
        long startTime = System.nanoTime();

        // --- Створення пулу потоків 
        // Використовуємо fixedThreadPool з кількістю потоків, рівною THREADS
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // Список задач, які виконуватимуться паралельно
        List<Callable<Long>> tasks = new ArrayList<>();

        // --- Розподіл ітерацій між потоками 
        // base - скільки ітерацій отримає кожен потік мінімум
        long base = ITERATIONS / THREADS;

        // remainder - кількість потоків, що отримають на одну ітерацію більше
        long remainder = ITERATIONS % THREADS;

        for (int i = 0; i < THREADS; i++) {

            // Кількість ітерацій для поточного потоку
            long iterationsForThread = base + (i < remainder ? 1 : 0);

            // Додаємо задачу у список (кожна задача працює незалежно)
            tasks.add(() -> {

                // Локальний лічильник влучень у коло
                long localHits = 0;

                // ThreadLocalRandom - окремий ГПВЧ для кожного потоку
                ThreadLocalRandom rnd = ThreadLocalRandom.current();

                // Основний цикл - генерація випадкових точок
                for (long j = 0; j < iterationsForThread; j++) {

                    // Випадкові координати x і y у діапазоні [0;1)
                    double x = rnd.nextDouble();
                    double y = rnd.nextDouble();

                    // Перевірка чи точка потрапила у чверть кола
                    if (x * x + y * y <= 1.0) {
                        localHits++;
                    }
                }

                // Повертаємо кількість влучень у цьому потоці
                return localHits;
            });
        }

        // Список результатів виконання потоків
        List<Future<Long>> results;

        try {
            // Запускаємо всі задачі та чекаємо на завершення
            results = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted!");
            executor.shutdown();
            return;
        }

        // --- Агрегація результатів
        long totalHits = 0;

        for (Future<Long> f : results) {
            try {
                // Додаємо результат потоку до загальної кількості
                totalHits += f.get();
            } catch (Exception e) {
                System.out.println("Error collecting results!");
                executor.shutdown();
                return;
            }
        }

        // Завершуємо роботу пулу потоків
        executor.shutdown();

        // --- Обчислення числа Пі
        double pi = 4.0 * totalHits / (double) ITERATIONS;

        // --- Кінець заміру часу
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;

        // --- Форматування чисел
        DecimalFormat piFormat = new DecimalFormat("0.00000");  // формат Пі
        NumberFormat intFormat = NumberFormat.getInstance();     // формат з розділювачами
        DecimalFormat timeFormat = new DecimalFormat("0.00");    // формат часу

        // --- Фінальний вивід
        System.out.println("PI is " + piFormat.format(pi));
        System.out.println("THREADS " + THREADS);
        System.out.println("ITERATIONS " + intFormat.format(ITERATIONS));
        System.out.println("TIME " + timeFormat.format(timeMs) + "ms");
    }
}

