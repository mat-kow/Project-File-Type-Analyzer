package analyzer;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        String directoryName;
        String patternsFileName;
        if (args.length == 2) {
            directoryName = args[0];
            patternsFileName = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Name of directory with files being checked:");
            directoryName = scanner.nextLine();
            System.out.println("Name of file with patterns, each on separate line.\n" +
                    "Format: priority;\"pattern\";\"format name\"\n" +
                    "Example: 1;\"%PDF-\";\"PDF document\"");
            patternsFileName = scanner.nextLine();
        }



        List<Pattern> patterns = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(patternsFileName))) {
            while (scanner.hasNext()) {
                String[] line = scanner.nextLine().trim().split(";");
                Pattern pattern = new Pattern();
                pattern.setPriority(Integer.parseInt(line[0]));
                pattern.setPattern(line[1].substring(1, line[1].length() - 1));
                pattern.setResult(line[2].substring(1, line[2].length() - 1));
                patterns.add(pattern);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        patterns = Pattern.sort(patterns);
        Collections.reverse(patterns);

        File directory = new File(directoryName);

        List<File> files = Arrays.asList(directory.listFiles());
        List<Callable<String>> callables = new ArrayList<>();
        for (File f : files) {
            callables.add(new WorkerCallable(f, patterns));
        }
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<String>> futures = null;
        try {
            futures = executor.invokeAll(callables);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Future f : futures) {
            try {
                System.out.println(f.get(10, TimeUnit.SECONDS));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
