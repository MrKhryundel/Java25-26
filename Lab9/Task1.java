import java.util.*;
import java.util.concurrent.*;

// Account 
class Account {
    private final int id;          // Унікальний ID рахунку
    private int balance;           // Баланс грошей

    public Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    // Зняття грошей з рахунку
    public void withdraw(int amount) {
        balance -= amount;
    }

    // Поповнення рахунку
    public void deposit(int amount) {
        balance += amount;
    }
}

// Bank 
class Bank {

    // Потокобезпечний переказ грошей
    public void transfer(Account from, Account to, int amount) {

        // Уникаємо deadlock, блокуючи рахунки за порядком їх ID
        Account first = from.getId() < to.getId() ? from : to;
        Account second = from.getId() < to.getId() ? to : from;

        synchronized (first) {        // Блокуємо перший рахунок
            synchronized (second) {   // Потім другий рахунок
                // Тіло транзакції (аналог atomic {}) 
                if (from.getBalance() >= amount) { // Перевірка, щоб не піти в мінус
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            }
        }
    }
}

// Main (Task 1 Test) 
public class Task1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting bank transfer stress test...");

        Random rand = new Random();
        Bank bank = new Bank();

        // Створення рахунків 
        int accountCount = 200;   // кілька сотень
        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < accountCount; i++) {
            accounts.add(new Account(i, rand.nextInt(1000) + 500));
        }

        // Підрахунок початкової суми 
        int initialSum = accounts.stream().mapToInt(Account::getBalance).sum();
        System.out.println("Initial total money in bank: " + initialSum);

        // Створюємо багато потоків-трансферів 
        int threadCount = 5000;
        ExecutorService executor = Executors.newFixedThreadPool(50);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {

                // Генеруємо випадковий переказ 
                Account from = accounts.get(rand.nextInt(accountCount));
                Account to = accounts.get(rand.nextInt(accountCount));

                // Якщо однаковий — пропускаємо
                if (from == to) return;

                int amount = rand.nextInt(50);

                // Виконуємо переказ
                bank.transfer(from, to, amount);
            });
        }

        // Чекаємо завершення потоків 
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Підрахунок фінальної суми 
        int finalSum = accounts.stream().mapToInt(Account::getBalance).sum();
        System.out.println("Final total money in bank: " + finalSum);

        // Перевірка коректності 
        if (initialSum == finalSum) {
            System.out.println("SUCCESS: Total money is consistent.");
        } else {
            System.out.println("ERROR: Money mismatch! Something is wrong!");
        }

        System.out.println("Test completed.");
    }
}

