package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day9 {
    private List<Integer> numbers(String string) {
        List<Integer> numbers = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c) || c == '-') {
                number.append(c);
            }
            else {
                if (!number.isEmpty()) numbers.add(Integer.parseInt(number.toString()));
                number = new StringBuilder();
            }
        }
        if (!number.isEmpty()) numbers.add(Integer.parseInt(number.toString()));
        return numbers;
    }
    private long calculatePrediction(List<Integer> numbersList, boolean partTwo) {
        long ret = 0;
        List<Integer> forPrediction = new ArrayList<>();
        List<Integer> lines = new ArrayList<>(numbersList);
        List<Integer> nextLine = new ArrayList<>();
        while (lines.stream().filter(elem -> elem == 0).count() != lines.size()) {
            for (int i = 0; i < lines.size() - 1; i++) {
                nextLine.add(lines.get(i + 1) - lines.get(i));
            }
            lines = nextLine;
            nextLine = new ArrayList<>();
            if (lines.get(lines.size() - 1) != 0) {
                if (!partTwo) forPrediction.add(lines.get(lines.size() - 1));
                else forPrediction.add(lines.get(0));
            }
        }
        Collections.reverse(forPrediction);
        for (Integer elem : forPrediction) {
            if (partTwo) ret = elem - ret;
            else ret += elem;
        }
        return partTwo ? numbersList.get(0) - ret : ret + numbersList.get(numbersList.size() - 1);
    }
    private long allPrediction(List<String> stringList, boolean optionTwo) {
        long ret = 0;
        for (String line : stringList) {
            ret += calculatePrediction(numbers(line), optionTwo);
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        URL url = Day9.class.getClassLoader().getResource("input/Day9.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day9 day9 = new Day9();
        long ret = day9.allPrediction(input, false);
        System.out.println("The return value first part: " + ret);
        long retTwo = day9.allPrediction(input, true);
        System.out.println("The return value second part: " + retTwo);
    }
}
