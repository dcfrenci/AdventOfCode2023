package org.days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Day6 {
    private List<Long> numbersLong(String string) {
        List<Long> numbers = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            }
            else {
                if (!number.isEmpty()) numbers.add(Long.parseLong(number.toString()));
                number = new StringBuilder();
            }
        }
        if (!number.isEmpty()) numbers.add(Long.parseLong(number.toString()));
        return numbers;
    }
    private int getWaysToWin(long time, long distance) {
        long hold = 0;
        int counter = 0;
        while (hold <= time) {
            if ((time - hold) * hold > distance) counter++;
            hold++;
        }
        return counter;
    }
    private int makeRaces(List<String> lines){
        List<Long> times = numbersLong(lines.get(0));
        List<Long> distances = numbersLong(lines.get(1));
        int ret = 1;
        for (int i = 0; i < times.size(); i++) {
            ret *= getWaysToWin(times.get(i), distances.get(i));
        }
        return ret;
    }
    private long makeOneRace(List<String> lines) {
        List<Long> times = numbersLong(lines.get(0).replace(" ", ""));
        List<Long> distances = numbersLong(lines.get(1).replace(" ", ""));
        return getWaysToWin(times.get(0), distances.get(0));
    }

    public static void main(String[] args) throws IOException {
        URL url = Day4.class.getClassLoader().getResource("input/Day6.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day6 day6 = new Day6();
        int ret = day6.makeRaces(input);
        System.out.println("The return value first part: " + ret);
        long retTwo = day6.makeOneRace(input);
        System.out.println("The return value second part: " + retTwo);
    }
}
