package analyzer;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class WorkerCallable implements Callable<String> {
    private final File file;
    private final List<Pattern> patterns;

    public WorkerCallable(File file, List<Pattern> patterns) {
        this.file = file;
        this.patterns = patterns;
    }

    @Override
    public String call() throws Exception {
        StringBuilder text = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) { // reading file
            while (scanner.hasNext()) {
                text.append(scanner.nextLine());
            }
        }
        for (Pattern p : patterns) {
            String pattern = p.getPattern();
            boolean match = StringSearch.rabinKarpContains(text.toString(), pattern);
            if (match) {
                return file.getName() + ": " + p.getResult();
            }
        }
        String answer = "Unknown file type";
        return file.getName() + ": " + answer;
    }
}
