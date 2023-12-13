package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 {
    private int calculateReflection(int number, boolean optionHorizontal) {
        return optionHorizontal ? 100 * number : number;
    }
    private boolean allMirrored(List<String> lines, int first, int second) {
        List<String> before = lines.subList(0, first + second);
        Collections.reverse(before);
        List<String> after = lines.subList(first + second, lines.size());
        for (int index = 0; index < Math.min(before.size(), after.size()); index++) {
            if (!before.get(index).equals(after.get(index))) return false;
        }
        return true;
    }
    private int findMirrored(List<String> lines) {
        for (int i = 1; i < lines.size() - 1; i++) {
            if (lines.get(i).equals(lines.get(i - 1)) && allMirrored(new ArrayList<>(lines), i, 0)) return i;
            if (lines.get(i).equals(lines.get(i + 1)) && allMirrored(new ArrayList<>(lines), i, 1)) return i + 1;
        }
        return 0;
    }
    private int differentChars(String string, String toCompare) {
        int ret = 0;
        for (int i = 0; i < string.length(); i++) {
            if (toCompare.charAt(i) != string.charAt(i)) ret++;
        }
        return ret;
    }
    private boolean allMirroredCond(List<String> lines, int first, int second, boolean withSmudge) {
        if (first == 1 && second == 0) return true;
        if (first == lines.size() - 1 && second == 1) return true;
        List<String> before = lines.subList(0, first + second - 1);
        Collections.reverse(before);
        List<String> after = lines.subList(first + second + 1, lines.size());
        for (int index = 0; index < Math.min(before.size(), after.size()); index++) {
            if (!before.get(index).equals(after.get(index))) {
                if (withSmudge) return false;
                else withSmudge = true;
            }
        }
        return true;
    }
    private int findMirroredSmudge(List<String> lines) {
        for (int i = 1; i < lines.size() - 1; i++) {
            if (lines.get(i).equals(lines.get(i - 1)) && allMirroredCond(new ArrayList<>(lines), i, 0, false)) return i;
            if (differentChars(lines.get(i), lines.get(i - 1)) == 1 && allMirroredCond(new ArrayList<>(lines), i, 0, true)) return i;
            if (lines.get(i).equals(lines.get(i + 1)) && allMirroredCond(new ArrayList<>(lines), i, 1, false)) return i + 1;
            if (differentChars(lines.get(i), lines.get(i + 1)) == 1 && allMirroredCond(new ArrayList<>(lines), i, 1, true)) return i + 1;
        }
        return 0;
    }
    private long divideMirror(List<String> stringList, boolean optionTwo) {
        List<List<String>> mirrorVertical = new ArrayList<>();
        List<List<String>> mirrorHorizontal = new ArrayList<>();
        List<String> horizontal = new ArrayList<>();
        for (String line : stringList) {
            if (line.contains("#") || line.contains(".")) {
                horizontal.add(line);
            } else {
                mirrorHorizontal.add(horizontal);
                horizontal = new ArrayList<>();
            }
        }
        if (!horizontal.isEmpty()) mirrorHorizontal.add(horizontal);
        for (List<String> mirror : mirrorHorizontal) {
            List<String> vertical = new ArrayList<>();
            for (int index = 0; index < mirror.get(0).length(); index++) {
                int finalIndex = index;
                vertical.add(mirror.stream().reduce("", (oldString, addElem) -> oldString + addElem.charAt(finalIndex)));
            }
            mirrorVertical.add(vertical);
        }

        long ret = 0;
        for (int i = 0; i < mirrorVertical.size(); i++) {
            //mirrored vertical lines
            int val = calculateReflection(optionTwo ? findMirroredSmudge(mirrorHorizontal.get(i)) : findMirrored(mirrorHorizontal.get(i)), true);
            //mirrored horizontal lines
            if (val == 0) val = calculateReflection(optionTwo ? findMirroredSmudge(mirrorVertical.get(i)) : findMirrored(mirrorVertical.get(i)), false);
            ret += val;
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
        URL url = Day13.class.getClassLoader().getResource("input/Day13.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day13 day13 = new Day13();
        long ret = day13.divideMirror(input, false);
        System.out.println("The return value first part: " + ret);
        long retTwo = day13.divideMirror(input, true);
        System.out.println("The return value second part: " + retTwo);
    }
}
