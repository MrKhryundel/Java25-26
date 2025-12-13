// Кільцевий буфер 
/**
 * Потокобезпечний кільцевий буфер (ring buffer) з фіксованим розміром.
 * Реалізація використовує два індекси: head (читання) та tail (запис).
 * - Якщо head == tail -> буфер порожній.
 * - Якщо (tail + 1) % capacity == head -> буфер повний.
 *
 * При додаванні елемента:
 *   tail збільшується (по колу).
 * При видобуванні елемента:
 *   head збільшується (по колу).
 *
 * Методи put()/take() блокуються:
 * - Якщо буфер порожній, потік-споживач чекає.
 * - Якщо буфер повний, потік-виробник чекає.
 */
class RingBuffer<T> {
    private final Object[] buffer;  // Масив для зберігання елементів
    private final int capacity;     // Розмір буфера
    private int head = 0;           // Індекс "голови" (читання)
    private int tail = 0;           // Індекс "кінця" (запис)

    public RingBuffer(int capacity) {
        // Мінімальний розмір 2, бо одна комірка "жертвується" для розрізнення повний/порожній
        if (capacity < 2) {
            throw new IllegalArgumentException("Capacity must be at least 2");
        }
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    // Додавання елемента в буфер (якщо повний - чекаємо)
    public synchronized void put(T value) throws InterruptedException {
        while (isFull()) {
            wait(); // Чекаємо, поки з’явиться місце
        }
        buffer[tail] = value;
        tail = (tail + 1) % capacity;
        notifyAll(); // Сповіщаємо потоки, що чекають на дані або місце
    }

    // Витягування елемента з буфера (якщо порожній - чекаємо)
    @SuppressWarnings("unchecked")
    public synchronized T take() throws InterruptedException {
        while (isEmpty()) {
            wait(); // Чекаємо, поки з’являться дані
        }
        T value = (T) buffer[head];
        buffer[head] = null; // Не обов’язково, але корисно для збирача сміття
        head = (head + 1) % capacity;
        notifyAll(); // Сповіщаємо потоки, що чекають на місце або дані
        return value;
    }

    // Перевірка, чи порожній буфер
    private boolean isEmpty() {
        return head == tail;
    }

    // Перевірка, чи повний буфер
    private boolean isFull() {
        return (tail + 1) % capacity == head;
    }
}

// Основний клас Task2 
public class Task2 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting producer-consumer ring buffer demo...");

        // Створення двох кільцевих буферів 
        final RingBuffer<String> firstBuffer = new RingBuffer<>(20);   // Перший буфер
        final RingBuffer<String> secondBuffer = new RingBuffer<>(20);  // Другий буфер

        // 5 потоків-виробників (producers) 
        int producerCount = 5;
        for (int i = 1; i <= producerCount; i++) {
            final int producerId = i;
            Thread producer = new Thread(() -> {
                int msgCounter = 1;
                try {
                    while (true) {
                        // Формат повідомлення: "Thread No ... generated message ..."
                        String message = "Thread No " + producerId +
                                         " generated message " + msgCounter++;
                        firstBuffer.put(message);
                        // Невелика затримка, щоб вивід був більш читабельним
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    // Якщо потік перервано - просто завершуємо його
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + producerId);

            // Робимо потік демоном, як вимагає завдання
            producer.setDaemon(true);
            producer.start();
        }

        // 2 потоки-перекладачі між буферами 
        int transferCount = 2;
        for (int i = 1; i <= transferCount; i++) {
            final int transferId = i;
            Thread transferThread = new Thread(() -> {
                try {
                    while (true) {
                        // Беремо повідомлення з першого буфера
                        String original = firstBuffer.take();
                        // Формат для другого буфера:
                        // "Thread No ... transferred message ..."
                        String transformed = "Thread No " + transferId +
                                             " transferred message: " + original;
                        // Кладемо в другий буфер
                        secondBuffer.put(transformed);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Transfer-" + transferId);

            // Також робимо демоном
            transferThread.setDaemon(true);
            transferThread.start();
        }

        // Основний потік: читає 100 повідомлень з другого буфера 
        int messagesToRead = 100;
        for (int i = 1; i <= messagesToRead; i++) {
            String msg = secondBuffer.take();
            System.out.println(i + ": " + msg);
        }

        System.out.println("Main thread printed 100 messages. Exiting program...");

    }
}

