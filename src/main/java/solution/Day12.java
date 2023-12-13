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
    private int getMaxGroup(String springs) {
        int work = 0;
        int broken = 0;
        if (!springs.contains("?")) return groupNumber(brokenSpring(springs));
        else {
            int value = springs.indexOf('?');
            String arrangementPoint = springs.substring(0, value) + '.' + springs.substring(value + 1);
            String arrangement = springs.substring(0, value) + '#' + springs.substring(value + 1);
            work = getMaxGroup(arrangementPoint);
            broken = getMaxGroup(arrangement);
        }
        return Math.max(work, broken);
    }
    private int groupNumber(List<String> stringList) {
        int ret = 0;
        for (String string : stringList) {
            if (string.contains("?")) {
                ret += getMaxGroup(string);
            } else {
                ret++;
            }
        }
        return ret;
    }
    private String unfold(String string, boolean number) {
        if (number) return string + "," + string + "," + string + "," + string + "," + string;
        return string + "?" + string + "?" + string + "?" + string + "?" + string;
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
        return Arrays.stream(string.split("\\.")).filter(str -> str.contains("?") || str.contains("#")).toList();
    }
    private int getArrangement(String springs, String numberString) {
        //remove impossible cases
        if (numbers(numberString).stream().reduce(0, Integer::sum) > springs.chars().filter(elem -> elem == '?' || elem == '#').count()) {
            return 0;
        }
        //il numero di gruppi che si possono fare con i caratteri rimanenti è minore --> ret


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
            if (groupNumber(brokenSpring(springs.substring(value))) + brokenSpring(springs.substring(0, value)).size() /*- (value - 1 >= 0 && springs.charAt(value - 1) == '#' ? 1: 0)*/ < numbers(numberString).size()) {
                return 0;
            }
            String arrangementPoint = springs.substring(0, value) + '.' + springs.substring(value + 1);
            String arrangement = springs.substring(0, value) + '#' + springs.substring(value + 1);
            //check if there are other '?'
            work = getArrangement(arrangementPoint, numberString);
            broken = getArrangement(arrangement, numberString);
        }
        return work + broken;
    }
    private int allArrangement(List<String> stringList, boolean optionTwo) {
        List<String> springs = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        for (String string : stringList) {
            springs.add(string.substring(0, string.indexOf(' ')));
            numbers.add(string.substring(string.indexOf(' ')));
        }
        int ret = 0;
        for (int i = 0; i < springs.size(); i++){
            ret += getArrangement(optionTwo ? unfold(springs.get(i), false) : springs.get(i), optionTwo ? unfold(numbers.get(i), true) : numbers.get(i));
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        URL url = Day11.class.getClassLoader().getResource("input/Day12.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day12 day12 = new Day12();
        /*long ret = day12.allArrangement(input, false);
        System.out.println("The return value first part: " + ret);*/
        long retTwo = day12.allArrangement(input, true);
        System.out.println("The return value second part: " + retTwo);
    }
}
