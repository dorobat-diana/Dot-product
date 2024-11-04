import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ScalarProduct {
    private static final int BUFFER_SIZE = 1;
    private final List<Integer> buffer = new ArrayList<>();
    private int index = 0;

    private final Object lock = new Object();

    public void produce(int a, int b) throws InterruptedException {
        synchronized (lock) {
            while (buffer.size() == BUFFER_SIZE) {
                lock.wait();
            }
            buffer.add(a * b);
            System.out.println("Produced: " + (a * b));
            index++;
            lock.notify();
        }
    }

    public int consume() throws InterruptedException {
        synchronized (lock) {
            while (index <= 0) {
                lock.wait();
            }
            int product = buffer.remove(0);
            index--;
            lock.notify();
            return product;
        }
    }
}

class Producer extends Thread {
    private final ScalarProduct scalarProduct;
    private final int[] vectorA;
    private final int[] vectorB;

    public Producer(ScalarProduct scalarProduct, int[] vectorA, int[] vectorB) {
        this.scalarProduct = scalarProduct;
        this.vectorA = vectorA;
        this.vectorB = vectorB;
    }

    @Override
    public void run() {
        for (int i = 0; i < vectorA.length; i++) {
            try {
                scalarProduct.produce(vectorA[i], vectorB[i]);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

class Consumer extends Thread {
    private final ScalarProduct scalarProduct;
    private int scalarSum = 0;

    public Consumer(ScalarProduct scalarProduct) {
        this.scalarProduct = scalarProduct;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int product = scalarProduct.consume();
                scalarSum += product;
                System.out.println("Consumed: " + product + " | Current Sum: " + scalarSum);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getScalarSum() {
        return scalarSum;
    }
}

public class ScalarProductThreadExample {
    public static void main(String[] args) {
        Random random = new Random();

        int length = random.nextInt(10) + 1;
        int[] vectorA = new int[length];
        int[] vectorB = new int[length];

        for (int i = 0; i < length; i++) {
            vectorA[i] = random.nextInt(10) + 1;
            vectorB[i] = random.nextInt(10) + 1;
        }

        System.out.print("Vector A: ");
        for (int num : vectorA) {
            System.out.print(num + " ");
        }
        System.out.println();

        System.out.print("Vector B: ");
        for (int num : vectorB) {
            System.out.print(num + " ");
        }
        System.out.println();

        ScalarProduct scalarProduct = new ScalarProduct();
        Producer producer = new Producer(scalarProduct, vectorA, vectorB);
        Consumer consumer = new Consumer(scalarProduct);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Scalar Product: " + consumer.getScalarSum());
    }
}
