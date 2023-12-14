package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day14 {
    private long loadTotal(List<String> stringList) {
        return load(roll(changeOrientation(stringList), true));
    }
    private long load(List<String> stringList) {
        int ret = 0;
        for (String string : stringList) {
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == 'O')
                    ret += string.length() - i;
            }
        }
        return ret;
    }
    private List<String> roll(List<String> stringList, boolean oppose) {
        List<String> newStringList = new ArrayList<>();
        for (String vertical : stringList) {
            List<String> subLines = Arrays.stream(vertical.split("#")).toList();
            StringBuilder stringBuilder = new StringBuilder();
            long count = vertical.chars().filter(ch -> ch == '#').count();
            for (String subLine : subLines) {
                long number = subLine.chars().filter(ch -> ch == 'O').count();
                StringBuilder subString = new StringBuilder(new String(new char[(int) number]).replace('\0', 'O') + new String(new char[subLine.length() - (int) number]).replace('\0', '.'));
                stringBuilder.append(oppose ? subString : subString.reverse());
                if (count > 0) {
                    stringBuilder.append('#');
                    count--;
                }
            }
            if (count > 0)
                stringBuilder.append('#');
            newStringList.add(stringBuilder.toString());
        }
        return newStringList;
    }
    private List<String> changeOrientation(List<String> stringList) {
        List<String> newStringList = new ArrayList<>();
        for (int index = 0; index < stringList.get(0).length(); index++) {
            int finalIndex = index;
            newStringList.add(stringList.stream().reduce("", (oldString, toBeAdded) -> oldString + toBeAdded.charAt(finalIndex)));
        }
        return newStringList;
    }
    private long loadSpinTotal(List<String> stringList) {
        Map<List<String>, List<String>> cycles = new HashMap<>();
        for (int i = 0; i < 1000000000; i++) {
            if (cycles.containsKey(stringList)) {
                stringList = cycles.get(stringList);
            } else {
                List<String> oldStringList = new ArrayList<>(stringList);
                stringList = changeOrientation(roll(changeOrientation(stringList), true));    //north
                stringList = roll(stringList, true);    //west
                stringList = changeOrientation(roll(changeOrientation(stringList), false));    //south
                stringList = roll(stringList, false);   //east

                if (new HashSet<>(oldStringList).containsAll(stringList))
                    break;
                cycles.put(oldStringList, stringList);
            }
        }
        return load(changeOrientation(stringList));
    }
    public static void main(String[] args) throws IOException {
        URL url = Day14.class.getClassLoader().getResource("input/Day14.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day14 day14 = new Day14();
        long ret = day14.loadTotal(input);
        System.out.println("The return value first part: " + ret);
        long retTwo = day14.loadSpinTotal(input);
        System.out.println("The return value second part: " + retTwo);
    }
}
