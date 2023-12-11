package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day11 {
    //private int pathLength(Map<Integer, Map<Integer, Boolean>> taken, List<String> sky, char start, char finish) {
    private int pathLength(int startLine, int startIndex, int endLine, int endIndex) {
        return Math.abs(startLine - endLine) + Math.abs(startIndex - endIndex);
    }
    private String elemString(Map.Entry<Integer, Integer> elem) {
        return String.valueOf(elem.getKey()) + '-' + elem.getValue();
    }
    private long pathGalaxyTwo(LinkedList<String> stringList) {
        List<Map.Entry<Integer, Integer>> galaxyPosition = new ArrayList<>();
        for (int line = 0; line < stringList.size(); line++) {
            for (int index = 0; index < stringList.get(0).length(); index++) {
                if (stringList.get(line).charAt(index) == '#') {
                    galaxyPosition.add(new AbstractMap.SimpleEntry<>(line, index));
                }
            }
        }
        System.out.println("base: " + galaxyPosition);
        int addedLines = 0;
        for (int line = 0; line < stringList.size(); line++) {
            String string = stringList.get(line);
            if (!string.contains("#")) {
                int finalLine = line + addedLines;
                List<Map.Entry<Integer, Integer>> addLines = galaxyPosition.stream().filter(pos -> pos.getKey() >= finalLine).toList();
                for (Map.Entry<Integer, Integer> elem : addLines) {
                    galaxyPosition.set(galaxyPosition.indexOf(elem), new AbstractMap.SimpleEntry<>(elem.getKey() + 1000000 - 1, elem.getValue()));
                }
                addedLines += 1000000 - 1;
            }
        }
        System.out.println("line: " + galaxyPosition);
        int addedIndex = 0;
        for (int index = 0; index < stringList.get(0).length(); index++) {
            boolean noGalaxy = true;
            for (String string : stringList) {
                if (string.charAt(index) != '.') {
                    noGalaxy = false;
                    break;
                }
            }
            if (noGalaxy) {
                int finalIndex = index + addedIndex;
                List<Map.Entry<Integer, Integer>> addIndex = galaxyPosition.stream().filter(pos -> pos.getValue() >= finalIndex).toList();
                for (Map.Entry<Integer, Integer> elem : addIndex) {
                    galaxyPosition.set(galaxyPosition.indexOf(elem), new AbstractMap.SimpleEntry<>(elem.getKey(), elem.getValue() + 1000000 - 1));
                }
                addedIndex += 1000000 - 1;
            }
        }
        System.out.println("colm: " + galaxyPosition);
        List<String> couple = new ArrayList<>();
        long ret = 0;
        for (Map.Entry<Integer, Integer> firstElem : galaxyPosition) {
            for (Map.Entry<Integer, Integer> secondElem : galaxyPosition.subList(galaxyPosition.indexOf(firstElem), galaxyPosition.size())) {
                if (!elemString(firstElem).equals(elemString(secondElem)) && !couple.contains(elemString(firstElem) + " " + elemString(secondElem)) && !couple.contains(elemString(secondElem) + " " + elemString(firstElem))) {
                    ret += pathLength(firstElem.getKey(), firstElem.getValue(), secondElem.getKey(), secondElem.getValue());
                    couple.add(elemString(firstElem) + " " + elemString(secondElem));
                    couple.add(elemString(secondElem) + " " + elemString(firstElem));
                }
            }
        }

        return ret;
    }
    private long pathGalaxy(LinkedList<String> stringList, boolean optionTwo) {
        for (int i = 0; i < stringList.size(); i++) {
            String string = stringList.get(i);
            if (!string.contains("#")) {
                if (optionTwo) {
                    for (int two = i + 1; two <= 1000000; two++) {
                        stringList.add(two, string);
                    }
                    i += 1000000;
                } else {
                    stringList.add(i + 1, string);
                    i++;
                }
            }
        }
        for (int index = 0; index < stringList.get(0).length(); index++) {
            boolean noGalaxy = true;
            for (String string : stringList) {
                if (string.charAt(index) != '.') {
                    noGalaxy = false;
                    break;
                }
            }
            if (noGalaxy) {
                for (int line = 0; line < stringList.size(); line++) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (optionTwo) {
                        char [] array = new char[1000000];
                        Arrays.fill(array, '.');
                        stringList.set(line, stringBuilder.append(stringList.get(line), 0, index + 1).append(array).append(stringList.get(line).substring(index + 1)).toString());
                    } else {
                        stringList.set(line, stringBuilder.append(stringList.get(line), 0, index + 1).append('.').append(stringList.get(line).substring(index + 1)).toString());
                    }
                }
                if (optionTwo) {
                    index += 1000000;
                } else {
                    index++;
                }
            }
        }
        List<Map.Entry<Integer, Integer>> galaxyPosition = new ArrayList<>();
        for (int line = 0; line < stringList.size(); line++) {
            for (int index = 0; index < stringList.get(0).length(); index++) {
                if (stringList.get(line).charAt(index) == '#') {
                    galaxyPosition.add(new AbstractMap.SimpleEntry<>(line, index));
                }
            }
        }
        List<String> couple = new ArrayList<>();
        long ret = 0;
        for (Map.Entry<Integer, Integer> firstElem : galaxyPosition) {
            for (Map.Entry<Integer, Integer> secondElem : galaxyPosition.subList(galaxyPosition.indexOf(firstElem), galaxyPosition.size())) {
                if (!elemString(firstElem).equals(elemString(secondElem)) && !couple.contains(elemString(firstElem) + " " + elemString(secondElem)) && !couple.contains(elemString(secondElem) + " " + elemString(firstElem))) {
                    ret += pathLength(firstElem.getKey(), firstElem.getValue(), secondElem.getKey(), secondElem.getValue());
                    couple.add(elemString(firstElem) + " " + elemString(secondElem));
                    couple.add(elemString(secondElem) + " " + elemString(firstElem));
                }
            }
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        URL url = Day11.class.getClassLoader().getResource("input/Day11.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        LinkedList<String> linkedInput = new LinkedList<>(new ArrayList<>());
        linkedInput.addAll(input);
        Day11 day11 = new Day11();
        long ret = day11.pathGalaxy(linkedInput, false);
        System.out.println("The return value first part: " + ret);
        long retTwo = day11.pathGalaxyTwo(linkedInput);
        System.out.println("The return value second part: " + retTwo);
    }
}
