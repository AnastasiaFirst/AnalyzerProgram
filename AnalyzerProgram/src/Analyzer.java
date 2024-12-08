import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class Analyzer implements Runnable {
    private final ArrayBlockingQueue<String> queue;
    private final char character;
    private final AtomicInteger maxCount = new AtomicInteger(0);
    private String maxString = "";

    public Analyzer(ArrayBlockingQueue<String> queue, char character) {
        this.queue = queue;
        this.character = character;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String text = queue.take();
                int count = countCharacter(text, character);

                if (count > maxCount.get()) {
                    maxCount.set(count);
                    maxString = text;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Анализатор для '" + character + "' завершен.");
            System.out.println("Максимальное количество символов '" + character + "': " + maxCount.get() +
                    " в строке: " + maxString);
        }
    }

    private int countCharacter(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }
}