package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Day1 {
    public Map<Integer, Integer> findNumberName(String string, boolean normal) {
        List<String> numberName = new ArrayList<>(List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine"));
        Map<Integer, Integer> numberPosition = new HashMap<>();
        for (String number: numberName) {
            int value = normal ? string.indexOf(number) : string.lastIndexOf(number);
            numberPosition.put(numberName.indexOf(number), value);
        }
        return normal ? numberPosition.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)).stream().collect(Collectors.toMap(Map.Entry:: getKey, Map.Entry:: getValue)) : numberPosition.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).stream().collect(Collectors.toMap(Map.Entry:: getKey, Map.Entry:: getValue));
    }
    public int translate(String string) {
        int first = 0;
        int firstIndex = -1;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (Character.isDigit(c)){
                first = Character.getNumericValue(c);
                firstIndex = i;
                break;
            }
        }
        Map<Integer, Integer> firstMap = findNumberName(string, true);
        if (!firstMap.isEmpty() && firstIndex > firstMap.values().stream().findFirst().get()) {
            first = firstMap.keySet().stream().findFirst().get();
        }
            /*for (Map.Entry<String, Integer> elem: findNumberName(string, true).entrySet()){
                if ((firstIndex > elem.getValue() || firstIndex == -1) && elem.getValue() != -1){
                    if (elem.getKey().compareTo("one") == 0) first = 1;
                    if (elem.getKey().compareTo("two") == 0) first = 2;
                    if (elem.getKey().compareTo("three") == 0) first = 3;
                    if (elem.getKey().compareTo("four") == 0) first = 4;
                    if (elem.getKey().compareTo("five") == 0) first = 5;
                    if (elem.getKey().compareTo("six") == 0) first = 6;
                    if (elem.getKey().compareTo("seven") == 0) first = 7;
                    if (elem.getKey().compareTo("eight") == 0) first = 8;
                    if (elem.getKey().compareTo("nine") == 0) first = 9;
                    firstIndex = elem.getValue();
                }
            }*/

        int last = 0;
        int lastIndex = -1;
        for (int i = string.length() - 1; i >= 0; i--) {
            char c = string.charAt(i);
            if (Character.isDigit(c)){
                last = Character.getNumericValue(c);
                lastIndex = i;
                break;
            }
        }
        /*for (Map.Entry<String, Integer> elem: findNumberName(string, false).entrySet()){
            if (lastIndex < elem.getValue()){
                if (elem.getKey().compareTo("one") == 0) last = 1;
                if (elem.getKey().compareTo("two") == 0) last = 2;
                if (elem.getKey().compareTo("three") == 0) last = 3;
                if (elem.getKey().compareTo("four") == 0) last = 4;
                if (elem.getKey().compareTo("five") == 0) last = 5;
                if (elem.getKey().compareTo("six") == 0) last = 6;
                if (elem.getKey().compareTo("seven") == 0) last = 7;
                if (elem.getKey().compareTo("eight") == 0) last = 8;
                if (elem.getKey().compareTo("nine") == 0) last = 9;
                lastIndex = elem.getValue();
            }
        }*/
        return first * 10 + last;
    }
    public int allTranslation(List<String> lines) {
        int ret = 0;
        for (String str: lines) {
            ret += translate(str);
            System.out.println(ret + ": " + translate(str));
        }
        return ret;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Day1 day1 = new Day1();
        URL url = Day1.class.getClassLoader().getResource("input/One.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int ret = day1.allTranslation(bufferedReader.lines().toList());
        System.out.println("This is the return value: " + ret);
    }
}