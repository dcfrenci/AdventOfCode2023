package org.days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
        int ret = 0;
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
    private String transformToMap(String handOne, String handTwo) {
        List<Character> value = new ArrayList<>(List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q','K', 'A'));
        /*Map<Character, Integer> strength = new HashMap<>();
        int val = 0;
        for (Character c : value) {
            strength.put(c, val);
            val++;
        }
        int ret = 0;
        for (Character c : hand.toCharArray()) {
            ret += strength.get(c);
        }
        return ret;*/
        for (int i = 0; i < handOne.length(); i++) {
            if (toCardValue(handOne.charAt(i)) > toCardValue(handTwo.charAt(i))) return handOne;
            if (toCardValue(handOne.charAt(i)) < toCardValue(handTwo.charAt(i))) return handTwo;
        }
        return handOne;
    }

    private long divideLines(List<String> stringList) {
        Map<String, Integer> hands = new HashMap<>();

        for (String line : stringList) {
            //hands.put(line, transformToMap(line.substring(0, line.indexOf(' '))));
            hands.put(line, checkCombination(line.substring(0, line.indexOf(' '))));
        }
        Map<Integer, List<String>> sorted = new HashMap<>();
        for (Map.Entry<String, Integer> elem : hands.entrySet()) {
            if (sorted.containsKey(elem.getValue())) {
                List<String> order = new ArrayList<>();
                for (String str : sorted.get(elem.getValue())) {
                    if (elem.getKey().compareTo(transformToMap(elem.getKey(), str)) == 0) {
                        order.add(elem.getKey());
                        order.add(str);
                    }
                }
                sorted.put(elem.getValue(), order);
            } else {
                Map.Entry<String, Integer> [] val = new Map.Entry[1];
                val[0] = elem;
                sorted.put(elem.getValue(), val);
            }
        }
        for (Map.Entry<Integer, Map.Entry<String, Integer>>)


        List<String> bids;
        bids = hands.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).collect(Collectors.toList());
        //hands.entrySet().stream().sorted((elem1, elem2) -> elem1.getValue() > elem2.getValue() ? 1 : 0)

        // .collect(Collectors.co)

        long ret = 0;
        for (int i = 0; i < bids.size(); i++) {
            System.out.println("Value: " + numbers(bids.get(i).substring(bids.get(i).indexOf(' '))).get(0));
            ret += (long) numbers(bids.get(i).substring(bids.get(i).indexOf(' '))).get(0) * (i + 1);
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
