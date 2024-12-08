import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int QUEUE_CAPACITY = 100;
    private static final int TOTAL_STRINGS = 1_000_000; // общее количество строк для генерации
    private static final String LETTERS = "abc";

    public static void main(String[] args) throws InterruptedException {

        ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread analyzerA = new Thread(new Analyzer(queueA, 'a'));
        Thread analyzerB = new Thread(new Analyzer(queueB, 'b'));
        Thread analyzerC = new Thread(new Analyzer(queueC, 'c'));

        analyzerA.start();
        analyzerB.start();
        analyzerC.start();

        Random random = new Random();
        for (int i = 0; i < TOTAL_STRINGS; i++) {
            String text = generateText(LETTERS, 10 + random.nextInt(11)); // длина строки от 10 до 20
            addToQueue(queueA, queueB, queueC, text);
        }

        analyzerA.interrupt();
        analyzerB.interrupt();
        analyzerC.interrupt();

        analyzerA.join();
        analyzerB.join();
        analyzerC.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void addToQueue(ArrayBlockingQueue<String> queueA,
                                  ArrayBlockingQueue<String> queueB,
                                  ArrayBlockingQueue<String> queueC,
                                  String text) {
        try {
            queueA.put(text);
            queueB.put(text);
            queueC.put(text);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}