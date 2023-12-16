package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Day16 {
    //north = 0, east = 1, south = 2, west = 3
    private Map<Integer, Map<Integer, Boolean>> voidMap(Map<Integer, Map<Integer, Boolean>> oldMap) {
        Map<Integer, Map<Integer, Boolean>> voidMap = new HashMap<>();
        for (int line = 0; line < oldMap.keySet().size(); line++) {
            voidMap.put(line, new HashMap<>());
            for (int index = 0; index < oldMap.get(0).size(); index++) {
                voidMap.get(line).put(index, false);
            }
        }
        return voidMap;
    }
    private Map<Integer, Map<Integer, Boolean>> energizedTiles(Map<Integer, Map<Integer, Boolean>> energized, Map<Integer, Map<Integer, Character>> charMap, int line, int index, int direction, int crossing) {
        if (line < 0 || line > energized.keySet().size() - 1 || index < 0 || index > energized.get(0).values().size() - 1 || (crossing > 10)) {
            return energized;
        }
        if (energized.get(line).get(index)) {
            crossing++;
        } else crossing = 0;
        char character = charMap.get(line).get(index);
        Map<Integer, Boolean> newLine = energized.get(line);
        newLine.put(index, true);
        energized.put(line, newLine);
        if (character == '-') {
            if (direction == 0 || direction == 2) {
                energized = energizedTiles(energized, charMap, line, index + 1, 1, crossing);
                energized = energizedTiles(energized, charMap, line, index - 1, 3, crossing);
            } else {
                energized = energizedTiles(energized, charMap, line, direction == 1 ? index + 1 : index - 1, direction, crossing);
            }
        }
        if (character == '|') {
            if (direction == 1 || direction == 3) {
                energized = energizedTiles(energized, charMap, line + 1, index, 2, crossing);
                energized = energizedTiles(energized, charMap, line - 1, index, 0, crossing);
            } else {
                energized = energizedTiles(energized, charMap, direction == 0 ? line - 1 : line + 1, index, direction, crossing);
            }
        }
        if (character == '/') {
            if (direction == 0) energized = energizedTiles(energized, charMap, line, index + 1, 1, crossing);
            if (direction == 1) energized = energizedTiles(energized, charMap, line - 1, index, 0, crossing);
            if (direction == 2) energized = energizedTiles(energized, charMap, line, index - 1, 3, crossing);
            if (direction == 3) energized = energizedTiles(energized, charMap, line + 1, index, 2, crossing);
        }
        if (character == '\\') {
            if (direction == 0) energized = energizedTiles(energized, charMap, line, index - 1, 3, crossing);
            if (direction == 1) energized = energizedTiles(energized, charMap, line + 1, index, 2, crossing);
            if (direction == 2) energized = energizedTiles(energized, charMap, line, index + 1, 1, crossing);
            if (direction == 3) energized = energizedTiles(energized, charMap, line - 1, index, 0, crossing);
        }
        if (character == '.') {
            if (direction == 0) energized = energizedTiles(energized, charMap, line - 1, index, 0, crossing);
            if (direction == 1) energized = energizedTiles(energized, charMap, line, index + 1, 1, crossing);
            if (direction == 2) energized = energizedTiles(energized, charMap, line + 1, index, 2, crossing);
            if (direction == 3) energized = energizedTiles(energized, charMap, line, index - 1, 3, crossing);
        }
        return energized;
    }
    private long createMap(List<String> stringList, boolean optionTwo) {
        Map<Integer, Map<Integer, Boolean>> energized = new HashMap<>();
        Map<Integer, Map<Integer, Character>> charMap = new HashMap<>();
        for (int line = 0; line < stringList.size(); line++) {
            energized.put(line, new HashMap<>());
            charMap.put(line, new HashMap<>());
            for (int index = 0; index < stringList.get(0).length(); index++) {
                energized.get(line).put(index, false);
                charMap.get(line).put(index, stringList.get(line).charAt(index));
            }
        }
        AtomicLong ret = new AtomicLong();
        if (!optionTwo) {
            energized = energizedTiles(energized, charMap, 0, 0, 1, 0);
            AtomicLong finalRet = ret;
            energized.forEach((key, value) -> finalRet.addAndGet(value.entrySet().stream().filter(Map.Entry::getValue).count()));
            return ret.get();
        }
        for (int index = 0; index < stringList.get(0).length(); index++) {
            Map<Integer, Map<Integer, Boolean>> newEnergized = energizedTiles(voidMap(energized), charMap, 0, index, 2, 0);
            AtomicLong newRet = new AtomicLong();
            newEnergized.forEach((key, value) -> newRet.addAndGet(value.entrySet().stream().filter(Map.Entry::getValue).count()));
            if (newRet.get() > ret.get()) ret = newRet;
        }
        for (int index = 0; index < stringList.get(0).length(); index++) {
            Map<Integer, Map<Integer, Boolean>> newEnergized= energizedTiles(voidMap(energized), charMap, stringList.size() - 1, index, 0, 0);
            AtomicLong newRet = new AtomicLong();
            newEnergized.forEach((key, value) -> newRet.addAndGet(value.entrySet().stream().filter(Map.Entry::getValue).count()));
            if (newRet.get() > ret.get()) ret = newRet;
        }
        for (int line = 0; line < stringList.size(); line++) {
            Map<Integer, Map<Integer, Boolean>> newEnergized= energizedTiles(voidMap(energized), charMap, line, 0, 1, 0);
            AtomicLong newRet = new AtomicLong();
            newEnergized.forEach((key, value) -> newRet.addAndGet(value.entrySet().stream().filter(Map.Entry::getValue).count()));
            if (newRet.get() > ret.get()) ret = newRet;
        }
        for (int line = 0; line < stringList.size(); line++) {
            Map<Integer, Map<Integer, Boolean>> newEnergized= energizedTiles(voidMap(energized), charMap, line, stringList.get(0).length(), 3, 0);
            AtomicLong newRet = new AtomicLong();
            newEnergized.forEach((key, value) -> newRet.addAndGet(value.entrySet().stream().filter(Map.Entry::getValue).count()));
            if (newRet.get() > ret.get()) ret = newRet;
        }
        return ret.get();
    }
    public static void main(String[] args) throws IOException {
        URL url = Day16.class.getClassLoader().getResource("input/Day16.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day16 day16 = new Day16();
        long ret = day16.createMap(input, false);
        System.out.println("The return value first part: " + ret);
        long retTwo = day16.createMap(input, true);
        System.out.println("The return value second part: " + retTwo);
    }
}
