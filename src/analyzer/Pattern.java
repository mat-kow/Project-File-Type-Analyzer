package analyzer;

import java.util.Arrays;
import java.util.List;

public class Pattern {
    private String pattern;
    private String result;
    private int priority;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Pattern() {
    }

    private Pattern getClone() {
        Pattern clone = new Pattern();
        clone.priority = this.priority;
        clone.result = this.result;
        clone.pattern = this.pattern;
        return clone;
    }
    @Override
    public String toString() {
        return priority + " " + pattern + " " + result;
    }

    public static List<Pattern> sort(List<Pattern> patterns) {
        Pattern[] array = new Pattern[patterns.size()];
        patterns.toArray(array);
        divide(array, 0, array.length);
        return Arrays.asList(array);
    }

    private static void divide(Pattern[] array, int leftIncl, int rightExl) {
        if (leftIncl >= rightExl - 1) {
            return;
        }
        int middle = leftIncl + (rightExl - leftIncl) / 2;
        divide(array, leftIncl, middle);
        divide(array, middle, rightExl);
        merge(array, leftIncl, middle, rightExl);
    }
    private static void merge(Pattern[] array, int left, int middle, int right) {
        int i = left;
        int j = middle;
        int k = 0;
        Pattern[] tempArray = new Pattern[right - left];
        while (i < middle && j < right) {
            if (array[i].priority < array[j].priority) {
                tempArray[k] = array[i].getClone();
                i++;
            } else {
                tempArray[k] = array[j].getClone();
                j++;
            }
            k++;
        }
        while (i < middle) {
            tempArray[k] = array[i];
            i++;
            k++;
        }
        while (j < right) {
            tempArray[k] = array[j].getClone();
            j++;
            k++;
        }
        for (int m = 0; m < k; m++) {
            array[m + left] = tempArray[m].getClone();
        }
    }

}
