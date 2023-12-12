package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day12 {
    private int getTotal(List<Integer> numbers) {
        int ret = 0;
        for (Integer elem : numbers) {
            ret += elem;
        }
        return ret;
    }
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
    private List<String> brokenSpring(String string) {
        List<String> brokenSpring = new ArrayList<>();
        return Arrays.stream(string.split("\\.")).filter(str -> str.contains("?") || str.contains("#")).toList();
    }
    private int getArrangement(String springs, String numberString) {
        int work = 0;
        int broken = 0;
        //base
        if (!springs.contains("?")) {
            //check if the solution is correct
            for (int i = 0; i < numbers(numberString).size(); i++) {
                if (brokenSpring(springs).size() != numbers(numberString).size() || brokenSpring(springs).get(i).length() != numbers(numberString).get(i)) {
                    return 0;
                }
            }
            return 1;
        } else {
            int value = springs.indexOf('?');
            String arrangementPoint = springs.substring(0, value) + '.' + springs.substring(value + 1);
            String arrangement = springs.substring(0, value) + '#' + springs.substring(value + 1);
            //check if there are other '?'
            work = getArrangement(arrangementPoint, numberString);
            broken = getArrangement(arrangement, numberString);
        }
        return work + broken;

        /*if (!springs.contains("?")) return 0;
        int value = springs.indexOf('?');
        String arrangementPoint = springs.substring(0, value) + '.' + springs.substring(value + 1);
        String arrangement = springs.substring(0, value) + '#' + springs.substring(value + 1);
        int work = 0;
        int broken = 0;
        if (arrangementPoint.contains("?")) {
            work = getArrangement(arrangementPoint, numberString, index + 1);
        }
        if (arrangement.contains("?")) {
            broken = getArrangement(arrangement, numberString, index + 1);
        }
        for (int i = 0; i < numbers(numberString).size(); i++) {
            if (brokenSpring(arrangementPoint).size() != numbers(numberString).size() || brokenSpring(arrangementPoint).get(i).length() != numbers(numberString).get(i)) work = 0;
            if (brokenSpring(arrangement).size() != numbers(numberString).size() || brokenSpring(arrangement).get(i).length() != numbers(numberString).get(i)) broken = 0;
        }
        if (getTotal(numbers(numberString)) == springs.chars().filter(elem -> elem == '#').count()) {
            return work + broken;
        }
        return 0;*/
    }
    private int allArrangement(List<String> stringList) {
        List<String> springs = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        for (String string : stringList) {
            springs.add(string.substring(0, string.indexOf(' ')));
            numbers.add(string.substring(string.indexOf(' ')));
        }
        int ret = 0;
        for (int i = 0; i < springs.size(); i++){
            ret += getArrangement(springs.get(i), numbers.get(i));
        }
        return 0;
    }
    public static void main(String[] args) throws IOException {
        URL url = Day11.class.getClassLoader().getResource("input/Day12.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day12 day12 = new Day12();
        long ret = day12.allArrangement(input);
        System.out.println("The return value first part: " + ret);
        /*long retTwo = day11.pathGalaxyTwo(linkedInput, 1000000);
        System.out.println("The return value second part: " + retTwo);*/
    }
}
