package org.days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day7 {
    private List<Integer> numbers(String string) {
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
    private int toCardValue(Character character) {
        List<Character> value = new ArrayList<>(List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q','K', 'A'));
        Map<Character, Integer> strength = new HashMap<>();
        int pos = 0;
        for (Character c : value) {
            strength.put(c, pos++);
        }
        return strength.get(character);
    }
    private int checkCombination(String hand) {
        List<Character> value = new ArrayList<>(List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q','K', 'A'));
        Map<Character, Integer> strength = new HashMap<>();
        for (Character c : value) {
            strength.put(c, 0);
        }
        for (Character c : hand.toCharArray()) {
            strength.put(c, strength.get(c) + 1);
        }
        List<Integer> result = new ArrayList<>();
        for (Integer elem : strength.values()) {
            if (elem > 0) result.add(elem);
        }
        //check
        if (result.size() == 1) return 6;
        if (result.size() == 2) {
            if (result.get(0) == 4 || result.get(1) == 4) return 5;
            return 4;
        }
        if (result.size() == 3) {
            if (result.get(0) == 3 || result.get(1) == 3 || result.get(2) == 3) return 3;
            if (result.get(0) == 2 && (result.get(1) == 2 || result.get(2) == 2)) return 2;
            if (result.get(1) == 2 && (result.get(0) == 2 || result.get(2) == 2)) return 2;
            if (result.get(2) == 2 && (result.get(1) == 2 || result.get(0) == 2)) return 2;
        }
        for (Integer integer : result) {
            if (integer == 2) return 1;
        }
        return 1;
    }
    private int transformToMap(String handOne, String handTwo) {
        for (int i = 0; i < handOne.length(); i++) {
            if (toCardValue(handOne.charAt(i)) > toCardValue(handTwo.charAt(i))) return 1;
            if (toCardValue(handOne.charAt(i)) < toCardValue(handTwo.charAt(i))) return -1;
        }
        return 0;
    }

    private long divideLines(List<String> stringList) {
        Map<String, Integer> hands = new HashMap<>();
        for (String line : stringList) {
            hands.put(line, checkCombination(line.substring(0, line.indexOf(' '))));
        }

        Map<Integer, List<String>> sorted = new HashMap<>();
        for (Map.Entry<String, Integer> elem : hands.entrySet()) {
            if (sorted.containsKey(elem.getValue())) {
                List<String> order = new ArrayList<>(sorted.get(elem.getValue()));
                order.add(elem.getKey());
                sorted.put(elem.getValue(), order.stream().sorted(this::transformToMap).toList());
            } else {
                List<String> order = new ArrayList<>();
                order.add(elem.getKey());
                sorted.put(elem.getValue(), order);
            }
        }

        long ret = 0;
        int strength = 1;
        for (Map.Entry<Integer, List<String>> elem : sorted.entrySet()) {
            for (String string : elem.getValue()) {
                ret += (long) strength * numbers(string.substring(string.indexOf(' '))).get(0);
                strength++;
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IOException {
        URL url = Day7.class.getClassLoader().getResource("input/Day7.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day7 day7 = new Day7();
        long ret = day7.divideLines(input);
        System.out.println("The return value first part: " + ret);
    }
}
