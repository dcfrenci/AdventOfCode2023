package org.days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class Day3 {
    public List<String> findNumbers(String string) {
        List<String> numbers = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            }
            else {
                if (!number.isEmpty()) numbers.add(number.toString());
                number = new StringBuilder();
            }
        }
        if (!number.isEmpty()) numbers.add(number.toString());
        return numbers;
    }
    public String toZero(String string, String number) {
        StringBuilder stringBuilder = new StringBuilder("0000000000");
        string = string.replaceFirst(number, stringBuilder.substring(0, number.length()));
        return string;
    }
    public boolean check(String number, String string, String line){
        //check diagonally before and after
        char c;
        if (string.indexOf(number) - 1 > 0) {
            c = line.charAt(string.indexOf(number) - 1);
            if (c != '.' && !Character.isDigit(c)) return true;
        }
        int index = string.indexOf(number) + number.length();
        if (index < string.length()) {
            c = line.charAt(index);
            if (c != '.' && !Character.isDigit(c)) return true;
        }
        //check vertically
        for (int i = 0; i < number.length(); i++) {
            c = line.charAt(string.indexOf(number) + i);
            if (c != '.' && !Character.isDigit(c)) return true;
        }
        return false;
    }

    public ArrayList<Integer> checkSymbol(String number, String string, String line) {
        ArrayList<Integer> index = new ArrayList<>();
        //check diagonally before and after
        if (string.indexOf(number) - 1 > 0) {
            if (line.charAt(string.indexOf(number) - 1) == '*') index.add(string.indexOf(number) - 1);
        }
        int i = string.indexOf(number) + number.length();
        if (i < string.length()) {
            if (line.charAt(i) == '*') index.add(i);
        }
        //check vertically
        for (int k = 0; k < number.length(); k++) {
            if (line.charAt(string.indexOf(number) + k) == '*') index.add(string.indexOf(number) + k);
        }
        return index;
    }

    public int numbers(List<String> stringList) {
        int ret = 0;
        String [] lines = new String[stringList.size()];
        stringList.forEach(elem -> lines[stringList.indexOf(elem)] = elem);
        for (int i = 0; i < lines.length; i++) {
            List<String> numbers = findNumbers(lines[i]);
            for (String number: numbers) {
                boolean verify;
                //character before the number
                if (lines[i].indexOf(number) - 1 > 0) {
                    if (!Character.isDigit(lines[i].charAt(lines[i].indexOf(number) - 1)) && lines[i].charAt(lines[i].indexOf(number) - 1) != '.') {
                        lines[i] = toZero(lines[i], number);
                        ret += Integer.parseInt(number);
                        continue;
                    }
                }
                //character after the number
                if (lines[i].indexOf(number) + number.length() < lines[i].length()) {
                    if (!Character.isDigit(lines[i].charAt(lines[i].indexOf(number) + number.length())) && lines[i].charAt(lines[i].indexOf(number) + number.length()) != '.') {
                        lines[i] = toZero(lines[i], number);
                        ret += Integer.parseInt(number);
                        continue;
                    }
                }
                //check other lines
                    //line before
                if (i != 0) {
                    verify = check(number, lines[i], lines[i - 1]);
                    if (verify) {
                        lines[i] = toZero(lines[i], number);
                        ret += Integer.parseInt(number);
                        continue;
                    }
                }
                    //line after
                if (i != lines.length - 1) {
                    verify = check(number, lines[i], lines[i + 1]);
                    if (verify) {
                        ret += Integer.parseInt(number);
                    }
                }
                lines[i] = toZero(lines[i], number);
            }
        }
        return ret;
    }
    public Map<Integer, Map<Integer, List<Integer>>> getSymbol(String string) {
        Map<Integer, Map<Integer, List<Integer>>> symbol = new HashMap<>();
        String [] lines = string.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int pos = 0; pos < lines[i].length(); pos++) {
                if (lines[i].charAt(pos) == '*') {
                    if (!symbol.containsKey(i)) symbol.put(i, new HashMap<>(Map.of(pos, new ArrayList<>())));
                    else {
                        Map<Integer, List<Integer>> symbolLine = symbol.get(i);
                        if (symbolLine != null) {
                            symbolLine.put(pos, new ArrayList<>());
                            symbol.put(i, symbolLine);
                        }
                    }
                }
            }
        }
        return symbol;
    }
    public long gears(String string) {
        String [] lines = string.split("\n");
        Map<Integer, Map<Integer, List<Integer>>> symbol = getSymbol(string);
        for (int i = 0; i < lines.length; i++) {
            List<String> numbers = findNumbers(lines[i]);
            for (String number: numbers) {
                //check if close to '*'
                //character before the number
                if (lines[i].indexOf(number) - 1 > 0) {
                    if (lines[i].charAt(lines[i].indexOf(number) - 1) == '*') {
                        symbol.get(i).get(lines[i].indexOf(number) - 1).add(Integer.parseInt(number));
                    }
                }
                //character after the number
                if (lines[i].indexOf(number) + number.length() < lines[i].length()) {
                    if (lines[i].charAt(lines[i].indexOf(number) + number.length()) == '*') {
                        symbol.get(i).get(lines[i].indexOf(number) + number.length()).add(Integer.parseInt(number));
                    }
                }
                //close lines
                if (i > 0) {
                    for (Integer elem: checkSymbol(number, lines[i], lines[i - 1])) {
                        symbol.get(i - 1).get(elem).add(Integer.parseInt(number));
                    }
                }
                if (i < lines.length - 1) {
                    List<Integer> temp = checkSymbol(number, lines[i], lines[i + 1]);
                    for (Integer elem: temp) {
                        symbol.get(i + 1).get(elem).add(Integer.parseInt(number));
                    }
                }
                lines[i] = toZero(lines[i], number);
            }
        }
        long ret = 0;
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> line : symbol.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> elem : line.getValue().entrySet()) {
                if (elem.getValue().size() == 2) {
                    int mul = elem.getValue().get(0) * elem.getValue().get(1);
                    ret += mul;
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IOException {
        Day3 day3 = new Day3();
        URL url = Day3.class.getClassLoader().getResource("input/Three.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        String stringBuilder = input.stream().map(str -> str + "\n").collect(Collectors.joining());

        int ret = day3.numbers(input);
        System.out.println("Return value part one: " + ret);
        long ret2 = day3.gears(stringBuilder);
        System.out.println("Return value part two: " + ret2);
    }
}
