package solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Day8 {
    private List<String> letterBlock(String string) {
        string = string.replace("(", "");
        string = string.replace(")", "");
        string = string.replace("=", "");
        string = string.replace(",", "");
        return Arrays.stream(string.split(" ")).filter(elem -> elem.length() > 1).toList();
    }
    private int divideLines(List<String> stringList) {
        String sequence = stringList.get(0);
        Map<String, Map<Character, String>> nodes = new HashMap<>();
        for (String string : stringList.subList(2, stringList.size())) {
            List<String> letterBlock = letterBlock(string);
            nodes.put(letterBlock.get(0), new HashMap<>(Map.of('L', letterBlock.get(1), 'R', letterBlock.get(2))));
        }
        //search exit
        int ret = 0;
        int pos = 0;
        String finding = "AAA";
        while (true) {
            finding = nodes.get(finding).get(sequence.charAt(pos));
            ret++;
            if (finding.compareTo("ZZZ") == 0) return ret;
            if (pos == sequence.length() - 1) pos = 0;
            else pos++;
        }
    }

    private int divideLinesSecond(List<String> stringList) {
        String sequence = stringList.get(0);
        Map<String, Map<Character, String>> nodes = new HashMap<>();
        for (String string : stringList.subList(2, stringList.size())) {
            List<String> letterBlock = letterBlock(string);
            nodes.put(letterBlock.get(0), new HashMap<>(Map.of('L', letterBlock.get(1), 'R', letterBlock.get(2))));
        }
        //search exit
        int ret = 0;
        int pos = 0;
        List<String> finding = new ArrayList<>(nodes.keySet().stream().filter(elem -> elem.endsWith("A")).toList());
        System.out.println("AKZ".endsWith("Z"));
        int [] found = new int[finding.size()];
        while (true) {
            int finalPos = pos;
            finding.replaceAll(elem -> nodes.get(elem).get(sequence.charAt(finalPos)));
            ret++;
            //if (finding.stream().filter(elem -> elem.endsWith("Z")).toList().size() == finding.size()) return ret;
            int finalRet = ret;
            finding.forEach(elem -> {
                if (elem.endsWith("Z")  && found[finding.indexOf(elem)] == 0) {
                    System.out.println(finding);
                    found[finding.indexOf(elem)] = finalRet;
                    System.out.println(Arrays.toString(found));
                }
            });
            if (Arrays.stream(found).filter(elem -> elem != 0).count() == found.length) break;
            if (pos == sequence.length() - 1) pos = 0;
            else pos++;
        }
        ret = 1;
        for (int integer : found) {
            ret *= integer;
        }
        return calcolaProdottoMCMArray(found);
    }

    private static int calcolaMCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Metodo per calcolare il MCM di una coppia di numeri
    private static int calcolaMCM(int numero1, int numero2) {
        return (numero1 * numero2) / calcolaMCD(numero1, numero2);
    }

    // Metodo per calcolare il prodotto degli MCM di ogni coppia nell'array
    private static int calcolaProdottoMCMArray(int[] numeri) {
        int prodottoMCM = 1;

        for (int i = 0; i < numeri.length - 1; i++) {
            for (int j = i + 1; j < numeri.length; j++) {
                prodottoMCM *= calcolaMCM(numeri[i], numeri[j]);
            }
        }

        return prodottoMCM;
    }

    public static void main(String[] args) throws IOException {
        URL url = Day8.class.getClassLoader().getResource("input/Day8.txt");
        assert url != null;
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        List<String> input = bufferedReader.lines().toList();
        Day8 day8 = new Day8();
        /*long ret = day8.divideLines(input);
        System.out.println("The return value first part: " + ret);*/
        int retTwo = day8.divideLinesSecond(input);
        System.out.println("The return value second part: " + retTwo);
    }
}
