package org.days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day4 {
    public List<Integer> numbers(String string) {
        List<Integer> numbers = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c)) {
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
    public long winningNumbers(String string, boolean type) {
        List<Integer> winningNumbers = numbers(string.substring(0, string.indexOf('|')));
        List<Integer> cards = numbers(string.substring(string.indexOf('|')));
        long ret = 0;
        for (Integer card : cards) {
            ret += winningNumbers.stream().filter(elem -> elem.compareTo(card) == 0).count();
        }
        if (!type) return ret;
        return ret == 0 || ret == 1 || ret == 2 ? ret : (long) Math.pow(2, ret - 1);
    }
    public long divideLines(List<String> lines) {
        long ret = 0;
        for (String line : lines) {
            String checkNumbers = line.substring(line.indexOf(':'));
            ret += winningNumbers(checkNumbers, true);
        }
        return ret;
    }
    public long countAllCards(List<String> lines) {
        long ret = 0;
        Map<Integer, Integer> cardsNumber = new HashMap<>();
        for (String line : lines) {
            cardsNumber.put(numbers(line.substring(0, line.indexOf(':'))).get(0), 1);
        }
        for (String line : lines) {
            Integer index = numbers(line.substring(0, line.indexOf(':'))).get(0);
            String checkNumbers = line.substring(line.indexOf(':'));
            long val = winningNumbers(checkNumbers, false);
            for (int i = 1; i <= val; i++) {
                cardsNumber.put(index + i, cardsNumber.get(index + i) + cardsNumber.get(index));
            }
        }
        for (Integer elem : cardsNumber.values()) {
            ret += elem;
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        Day4 day4 = new Day4();
        URL url = Day4.class.getClassLoader().getResource("input/Four.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        long ret = day4.divideLines(input);
        System.out.println("Return value part one:  " + ret);
        long retTwo = day4.countAllCards(input);
        System.out.println("Return value part two: " + retTwo);
    }
}
