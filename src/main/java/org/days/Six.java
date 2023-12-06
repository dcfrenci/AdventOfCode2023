package org.days;

import java.util.ArrayList;
import java.util.List;

public class Six {
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
            if ((time - hold) * hold > distance) {
                counter++;
            }
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

    public static void main(String[] args) {
        List<String> stringInput = new ArrayList<>(List.of("Time:        53     83     72     88", "Distance:   333   1635   1289   1532"));
        Six six = new Six();
        int ret = six.makeRaces(stringInput);
        System.out.println("The return value first part: " + ret);

        long retTwo = six.makeOneRace(stringInput);
        System.out.println("The return value second part: " + retTwo);
    }
}
