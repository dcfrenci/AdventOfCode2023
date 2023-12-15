package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {
    private int toHash(String string) {
        int ret = 0;
        for (Character c : string.toCharArray()) {
            ret = ((ret + c.hashCode()) * 17) % 256;
        }
        return ret;
    }
    private int divideToString(String string) {
        int ret = 0;
        for (String str : string.split(",")) {
            ret += toHash(str);
        }
        return ret;
    }
    private int divideToBoxes(String string) {
        Map<Integer, List<String>> boxes = new HashMap<>();
        for (String str : string.split(",")) {
            if (str.contains("=")) {
                List<String> lenses = boxes.containsKey(toHash(str.substring(0, str.indexOf('=')))) ? boxes.get(toHash(str.substring(0, str.indexOf('=')))) : new ArrayList<>();
                if (!lenses.isEmpty() && lenses.stream().anyMatch(lens -> lens.substring(0, lens.indexOf('=')).equals(str.substring(0, str.indexOf('='))))) lenses.set(lenses.indexOf(lenses.stream().filter(lens -> lens.substring(0, lens.indexOf('=')).equals(str.substring(0, str.indexOf('=')))).toList().get(0)), str);
                else lenses.add(str);
                boxes.put(toHash(str.substring(0, str.indexOf('='))), lenses);
            } else {
                List<String> lenses = boxes.containsKey(toHash(str.substring(0, str.indexOf('-')))) ? boxes.get(toHash(str.substring(0, str.indexOf('-')))) : new ArrayList<>();
                if (lenses.stream().anyMatch(elem -> elem.substring(0, elem.indexOf('=')).equals(str.substring(0, str.indexOf('-'))))) {
                    lenses.remove(lenses.stream().filter(elem -> elem.substring(0, elem.indexOf('=')).equals(str.substring(0, str.indexOf('-')))).toList().get(0));
                    boxes.put(toHash(str.substring(0, str.indexOf('-'))), lenses);
                }
            }
        }
        int ret = 0;
        for (Map.Entry<Integer, List<String>> box : boxes.entrySet()) {
            for (String str : box.getValue()) {
                ret += (box.getKey() + 1) * (box.getValue().indexOf(str) + 1) * Integer.parseInt(str.substring(str.indexOf('=') + 1));
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IOException {
        URL url = Day15.class.getClassLoader().getResource("input/Day15.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        StringBuilder stringInput = new StringBuilder();
        input.forEach(stringInput::append);
        Day15 day15 = new Day15();
        long ret = day15.divideToString(stringInput.toString());
        System.out.println("The return value first part: " + ret);
        long retTwo = day15.divideToBoxes(stringInput.toString());
        System.out.println("The return value second part: " + retTwo);
    }
}
