package MonteCarloPI;

import Utils.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MonteCarloPi {

    static final long NUM_POINTS = 50_000_000L;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Console.clear();

        Console.print("Monte Carlo Pi Approximation\n", Console.Color.LIGHT_YELLOW);

        String[] options = {
                "Calculate Pi (Single Thread)",
                "Calculate Pi (Multi Thread)",
                "Calculate Pi (Both and Compare)",
                "Exit"
        };

        while (true) {
            String choice = Console.getInputBox("Choose an option:", options, "Your choice: ");

            switch (choice) {
                case "1":
                    Console.print("\nSingle threaded calculation started:\n", Console.Color.LIGHT_GREEN);
                    long startTime = System.nanoTime();
                    double piSingle = estimatePiWithoutThreads(NUM_POINTS);
                    long endTime = System.nanoTime();
                    Console.print("Pi approximation (single thread): " + piSingle, Console.Color.CYAN);
                    Console.print("Time taken (single thread): " + (endTime - startTime) / 1_000_000 + " ms\n", Console.Color.CYAN);
                    break;
                case "2":
                    Console.print("\nMulti threaded calculation started: (Your device has " + NUM_THREADS + " logical threads)\n", Console.Color.LIGHT_GREEN);
                    startTime = System.nanoTime();
                    double piMulti = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
                    endTime = System.nanoTime();
                    Console.print("Pi approximation (multi-threaded): " + piMulti, Console.Color.CYAN);
                    Console.print("Time taken (multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms\n", Console.Color.CYAN);
                    break;
                case "3":
                    Console.print("\nSingle threaded calculation started:\n", Console.Color.LIGHT_GREEN);
                    startTime = System.nanoTime();
                    piSingle = estimatePiWithoutThreads(NUM_POINTS);
                    endTime = System.nanoTime();
                    Console.print("Pi approximation (single thread): " + piSingle, Console.Color.CYAN);
                    Console.print("Time taken (single thread): " + (endTime - startTime) / 1_000_000 + " ms\n", Console.Color.CYAN);

                    Console.print("Multi threaded calculation started: (Your device has " + NUM_THREADS + " logical threads)\n", Console.Color.LIGHT_GREEN);
                    startTime = System.nanoTime();
                    piMulti = estimatePiWithThreads(NUM_POINTS, NUM_THREADS);
                    endTime = System.nanoTime();
                    Console.print("Pi approximation (multi-threaded): " + piMulti, Console.Color.CYAN);
                    Console.print("Time taken (multi-threaded): " + (endTime - startTime) / 1_000_000 + " ms\n", Console.Color.CYAN);
                    break;
                case "4":
                    Console.print("Exiting program. Goodbye!\n", Console.Color.LIGHT_MAGENTA);
                    return;
            }
        }
    }

    public static double estimatePiWithoutThreads(long numPoints) {
        long insideCircle = 0;
        for (long i = 0; i < numPoints; i++) {
            double x = Math.random();
            double y = Math.random();
            if (x * x + y * y <= 1.0) insideCircle++;
        }
        return 4.0 * insideCircle / numPoints;
    }

    public static double estimatePiWithThreads(long numPoints, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long pointsPerThread = numPoints / numThreads;
        long remainingPoints = numPoints % numThreads;
        List<Future<Long>> results = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            long points = pointsPerThread + (i == 0 ? remainingPoints : 0);
            results.add(executor.submit(new MonteCarloTask(points)));
        }

        long insideTotal = 0;
        for (Future<Long> result : results) insideTotal += result.get();

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        return 4.0 * insideTotal / numPoints;
    }

    static class MonteCarloTask implements Callable<Long> {
        private final long numPoints;

        public MonteCarloTask(long numPoints) {
            this.numPoints = numPoints;
        }

        @Override
        public Long call() {
            long inside = 0;
            for (long i = 0; i < numPoints; i++) {
                double x = Math.random();
                double y = Math.random();
                if (x * x + y * y <= 1.0) inside++;
            }
            return inside;
        }
    }
}
