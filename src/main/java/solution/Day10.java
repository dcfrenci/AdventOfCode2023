package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day10 {
    private void encloseByLoop(Map<Integer, Map<Integer, Boolean>> taken, List<char []> charMap) {
        int counter = 0;
        for (int line = 0; line < charMap.size(); line++) {
            for (int index = 0; index < charMap.get(0).length; index++){
                if (!taken.get(line).get(index)) {
                    charMap.get(line)[index] = '.';
                }
            }
        }
        //per linea
        for (int line = 0; line < charMap.size(); line++) {
            int L = 0;
            int I = 0;
            int J = 0;
            for (int index = 0; index < charMap.get(0).length; index++){
                if (!taken.get(line).get(index)) {
                    long num = L + J + I;
                    if (num % 2 != 0) {
                        counter++;
                        charMap.get(line)[index] = '1';
                    }
                } else {
                    if (charMap.get(line)[index] == 'L') L++;
                    if (charMap.get(line)[index] == '|') I++;
                    if (charMap.get(line)[index] == 'J') J++;
                }
            }
        }
        //count
        for (char[] chars : charMap) {
            System.out.println(chars);
        }
        System.out.println("enclosed: " + counter);
    }

    private static class Pair {
        private final Integer line;
        private final Integer index;
        public Pair(Integer line, Integer index) {
            this.line = line;
            this.index = index;
        }
        public Integer getLine() {
            return line;
        }
        public Integer getIndex() {
            return index;
        }

    }
    public List<Pair> addPair(List<Pair> list, Pair pair) {
        list.add(pair);
        return list;
    }

    private List<Pair> calculate(Map<String, Integer> position, Map<Integer, Map<Integer, Character>> charMap, List<Pair> taken) {
        List<Pair> linePlus;
        List<Pair> lineLess;
        List<Pair> indexPlus;
        List<Pair> indexLess;
        char character;
        try {
            linePlus = calculate(Map.of("line", position.get("line") + 1, "index", position.get("index")), charMap, addPair(taken, new Pair(position.get("line") + 1, position.get("index"))));
            lineLess = calculate(Map.of("line", position.get("line") - 1, "index", position.get("index")), charMap, addPair(taken, new Pair(position.get("line") - 1, position.get("index"))));
            indexPlus = calculate(Map.of("line", position.get("line"), "index", position.get("index") + 1), charMap, addPair(taken, new Pair(position.get("line"), position.get("index") + 1)));
            indexLess = calculate(Map.of("line", position.get("line"), "index", position.get("index") - 1), charMap, addPair(taken, new Pair(position.get("line"), position.get("index") - 1)));
            character = charMap.get(position.get("line")).get(position.get("index"));
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
        if (character == '|') {
            return linePlus.size() > lineLess.size() ? linePlus : lineLess;
        }
        if (character == '-') {
            return indexPlus.size() > indexLess.size() ? linePlus : lineLess;
        }
        if (character == 'L') {
            return lineLess.size() > indexPlus.size() ? lineLess : indexPlus;
        }
        if (character == 'J') {
            return lineLess.size() > indexLess.size() ? lineLess : indexLess;
        }
        if (character == '7') {
            return linePlus.size() > indexLess.size() ? linePlus : indexLess;
        }
        if (character == 'F') {
            return linePlus.size() > indexPlus.size() ? linePlus : indexPlus;
        }
        if (character == 'S') {
            return taken;
        }
        return new ArrayList<>();
    }
    private int maxDistance(List<String> stringList) {
        Map<Integer, Map<Integer, Character>> charMap = new HashMap<>();
        int sLine = 0;
        int sIndex = 0;
        for (int line = 0; line < stringList.size(); line++) {
            for (int index = 0; index < stringList.get(line).length(); index++) {
                Map<Integer, Character> mapLine = charMap.containsKey(line) ? charMap.get(line) : new HashMap<>();
                mapLine.put(index, stringList.get(line).charAt(index));
                charMap.put(line, mapLine);
                if (stringList.get(line).charAt(index) == 'S') {
                    sLine = line;
                    sIndex = index;
                }
            }
        }
        List<Pair> loop = calculate(Map.of("line", sLine - 1, "index", sIndex), charMap, new ArrayList<>());
        List<Pair> newLoop = calculate(Map.of("line", sLine + 1, "index", sIndex), charMap, new ArrayList<>());
        if (newLoop.size() > loop.size()) loop = newLoop;
        newLoop = calculate(Map.of("line", sLine, "index", sIndex - 1), charMap, new ArrayList<>());
        if (newLoop.size() > loop.size()) loop = newLoop;
        newLoop = calculate(Map.of("line", sLine, "index", sIndex + 1), charMap, new ArrayList<>());
        if (newLoop.size() > loop.size()) loop = newLoop;
        return loop.size() / 2;
    }
    private List<Character> findLoop(List<char []> charMap, char character, int cLine, int cIndex) {
        Map<Integer, Map<Integer, Boolean>> taken = new HashMap<>();
        List<Character> loop = new ArrayList<>();
        int line = 0;
        int sLine = 0;
        int sIndex = 0;
        for (char [] chars : charMap) {
            int index = 0;
            for (char elem : chars) {
                Map<Integer, Boolean> el = taken.containsKey(line) ? taken.get(line) : new HashMap<>();
                if (elem == 'S') {
                    el.put(index, true);
                    sIndex = index;
                    sLine = line;
                }
                else el.put(index, false);
                taken.put(line, el);
                index++;
            }
            line++;
        }
        while (true) {
            if (loop.size() == 2) {
                Map<Integer, Boolean> el = taken.get(sLine);
                el.put(sIndex, false);
                taken.put(sLine, el);
            }
            if (character == '|') {
                if (cLine - 1 >= 0 && !taken.get(cLine - 1).get(cIndex)){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cLine + 1 < charMap.size() && !taken.get(cLine + 1).get(cIndex)) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == '-') {
                if (cIndex - 1 >= 0 && !taken.get(cLine).get(cIndex - 1)){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cIndex + 1 < charMap.get(0).length && !taken.get(cLine).get(cIndex + 1)) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == 'L') {
                if (cLine - 1 >= 0 && !taken.get(cLine - 1).get(cIndex) && List.of('7', 'F', '|', 'S').contains(charMap.get(cLine - 1)[cIndex])){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cIndex + 1 < charMap.get(0).length && !taken.get(cLine).get(cIndex + 1) && List.of('7', 'J', '-', 'S').contains(charMap.get(cLine)[cIndex + 1])) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == 'J') {
                if (cLine - 1 >= 0 && !taken.get(cLine - 1).get(cIndex) && List.of('7', 'F', '|', 'S').contains(charMap.get(cLine - 1)[cIndex])){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cIndex - 1 >= 0 && !taken.get(cLine).get(cIndex - 1) && List.of('L', 'F', '-', 'S').contains(charMap.get(cLine)[cIndex - 1])){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == '7') {
                if (cLine + 1 < charMap.size() && !taken.get(cLine + 1).get(cIndex) && List.of('L', 'J', '|', 'S').contains(charMap.get(cLine + 1)[cIndex])) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cIndex - 1 >= 0 && !taken.get(cLine).get(cIndex - 1) && List.of('L', 'F', '-', 'S').contains(charMap.get(cLine)[cIndex - 1])){
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex--;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == 'F') {
                if (cLine + 1 < charMap.size() && !taken.get(cLine + 1).get(cIndex) && List.of('L', 'J', '|', 'S').contains(charMap.get(cLine + 1)[cIndex])) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cLine++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
                if (cIndex + 1 < charMap.get(0).length && !taken.get(cLine).get(cIndex + 1) && List.of('7', 'J', '-', 'S').contains(charMap.get(cLine)[cIndex + 1])) {
                    loop.add(character);
                    Map<Integer, Boolean> el = taken.get(cLine);
                    el.put(cIndex, true);
                    taken.put(cLine, el);
                    cIndex++;
                    character = charMap.get(cLine)[cIndex];
                    continue;
                }
            }
            if (character == 'S') {
                loop.add('S');
                System.out.println("num: " + loop.size() / 2);
                Map<Integer, Boolean> el = taken.get(sLine);
                el.put(sIndex, true);
                taken.put(sLine, el);
                charMap.get(sLine)[sIndex] = '|';
                encloseByLoop(taken, charMap);
                return loop;
            }
            return new ArrayList<>();
        }
    }
    private int findFurthest(List<String> stringList) {
        //create char map
        int sLine = 0;
        int sIndex = 0;
        List<char []> charMap = new ArrayList<>();
        for (String line : stringList) {
            charMap.add(line.toCharArray());
            if (line.contains("S")) {
                sLine = stringList.indexOf(line);
                sIndex = line.indexOf('S');
            }
        }

        //find loop
        List<Character> loop = new ArrayList<>();
        if (sLine - 1 >= 0 && charMap.get(sLine - 1)[sIndex] != '.') {
            loop = findLoop(charMap, charMap.get(sLine - 1)[sIndex], sLine - 1, sIndex);
        }
        if (charMap.size() > sLine + 1 && charMap.get(sLine + 1)[sIndex] != '.') {
            List<Character> newLoop = findLoop(charMap, charMap.get(sLine + 1)[sIndex], sLine + 1, sIndex);
            if (newLoop.size() > loop.size()) loop = newLoop;
        }
        if (sIndex - 1 >= 0 && charMap.get(sLine)[sIndex - 1] != '.') {
            List<Character> newLoop = findLoop(charMap, charMap.get(sLine)[sIndex], sLine, sIndex - 1);
            if (newLoop.size() > loop.size()) loop = newLoop;
        }
        if (charMap.get(0).length > sIndex + 1 && charMap.get(sLine)[sIndex + 1] != '.') {
            List<Character> newLoop = findLoop(charMap, charMap.get(sLine)[sIndex], sLine, sIndex + 1);
            if (newLoop.size() > loop.size()) loop = newLoop;
        }
        return loop.size() / 2;
    }

    public static void main(String[] args) throws IOException {
        URL url = Day10.class.getClassLoader().getResource("input/Day10.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day10 day10 = new Day10();
//        long ret = day10.findFurthest(input);
        long ret = day10.maxDistance(input);
        System.out.println("The return value first part: " + ret);
        /*long retTwo = day9.allPrediction(input, true);
        System.out.println("The return value second part: " + retTwo);*/
    }
}
