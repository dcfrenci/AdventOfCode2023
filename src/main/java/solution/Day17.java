package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Day17 {
    private void print(Map<Integer, Map<Integer, Boolean>> taken) {
        for (Map<Integer, Boolean> line : taken.values()) {
            for (Boolean index : line.values()) {
                if (index) System.out.print("#");
                else System.out.print(".");
            }
            System.out.print("\n");
        }
        System.out.println("----------------------------------------------");
    }
    private Map<Integer, Map<Integer, Boolean>> voidMap(Map<Integer, Map<Integer, Boolean>> oldMap) {
        Map<Integer, Map<Integer, Boolean>> voidMap = new HashMap<>();
        for (int line = 0; line < oldMap.keySet().size(); line++) {
            voidMap.put(line, new HashMap<>());
            for (int index = 0; index < oldMap.get(0).size(); index++) {
                voidMap.get(line).put(index, oldMap.get(line).get(index));
            }
        }
        return voidMap;
    }
    private int heatLoss(Map<Integer, Map<Integer, Boolean>> taken, Map<Integer, Map<Integer, Character>> charMap, boolean getValueAnyway) {
        if (!taken.get(charMap.size() - 1).get(charMap.get(0).size() - 1) && !getValueAnyway) return 0;
        int ret = 0;
        for (Map.Entry<Integer, Map<Integer, Boolean>> line : taken.entrySet()) {
            for (Map.Entry<Integer, Boolean> index : line.getValue().entrySet()) {
                if (index.getValue()) ret += Character.getNumericValue(charMap.get(line.getKey()).get(index.getKey()));
            }
        }
        return ret;
    }
    private Map<Integer, Map<Integer, Boolean>> betterPath(Map<Integer, Map<Integer, Boolean>> oldTaken, Map<Integer, Map<Integer, Boolean>> taken, Map<Integer, Map<Integer, Character>> charMap) {
        if (taken == null) return oldTaken;
        if (heatLoss(oldTaken, charMap, false) == 0) return taken;
        return heatLoss(taken, charMap, false) < heatLoss(oldTaken, charMap, false) ? taken : oldTaken;
    }
    private Map<Integer, Map<Integer, Boolean>> findPath(Map<Integer, Map<Integer, Boolean>> taken, Map<Integer, Map<Integer, Character>> charMap, int line, int index, int heatLoss, int sameDirection, int direction) {
        if (line < 0 || index < 0 || line > charMap.size() - 1 || index > charMap.get(0).size() - 1) return null;
        if (heatLoss > 0 && heatLoss(taken, charMap, true) >= heatLoss && !taken.get(line).get(index)) return null;
        if (taken.get(line).get(index)) return null;
        taken.get(line).put(index, true);
        if (line == charMap.size() - 1 && index == charMap.get(0).size() - 1) {
            print(taken);
            return taken;
        }
        //three case
        if (sameDirection < 2) {
            //straight
            if (direction == 0) taken = betterPath(taken, findPath(voidMap(taken), charMap, line - 1, index, heatLoss(taken, charMap, false), sameDirection + 1, 0), charMap);
            if (direction == 1) taken = betterPath(taken, findPath(voidMap(taken), charMap, line, index + 1, heatLoss(taken, charMap, false), sameDirection + 1, 1), charMap);
            if (direction == 2) taken = betterPath(taken, findPath(voidMap(taken), charMap, line + 1, index, heatLoss(taken, charMap, false), sameDirection + 1, 2), charMap);
            if (direction == 3) taken = betterPath(taken, findPath(voidMap(taken), charMap, line, index - 1, heatLoss(taken, charMap, false), sameDirection + 1, 3), charMap);
        }
        //turn
        if (direction == 0 || direction == 2) {
            taken = betterPath(taken, findPath(voidMap(taken), charMap, line, index + 1, heatLoss(taken, charMap, false), 1, 1), charMap);
            taken = betterPath(taken, findPath(voidMap(taken), charMap, line, index - 1, heatLoss(taken, charMap, false), 1, 3), charMap);
        } else {
            taken = betterPath(taken, findPath(voidMap(taken), charMap, line + 1, index, heatLoss(taken, charMap, false), 1, 2), charMap);
            taken = betterPath(taken, findPath(voidMap(taken), charMap, line - 1, index, heatLoss(taken, charMap, false), 1, 0), charMap);
        }
        return taken;
    }
    private long createMap(List<String> stringList, boolean optionTwo) {
        Map<Integer, Map<Integer, Boolean>> taken = new HashMap<>();
        Map<Integer, Map<Integer, Character>> charMap = new HashMap<>();
        for (int line = 0; line < stringList.size(); line++) {
            taken.put(line, new HashMap<>());
            charMap.put(line, new HashMap<>());
            for (int index = 0; index < stringList.get(0).length(); index++) {
                taken.get(line).put(index, false);
                charMap.get(line).put(index, stringList.get(line).charAt(index));
            }
        }
        return heatLoss(Objects.requireNonNull(findPath(taken, charMap, 0, 0, 0, 0, 1)), charMap, false);
    }
    public static void main(String[] args) throws IOException {
        URL url = Day17.class.getClassLoader().getResource("input/Day17.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day17 day17 = new Day17();
        long ret = day17.createMap(input, false);
        System.out.println("The return value first part: " + ret);
        /*long retTwo = day17.createMap(input, true);
        System.out.println("The return value second part: " + retTwo);*/
    }
}
