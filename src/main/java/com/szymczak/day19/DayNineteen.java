package com.szymczak.day19;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DayNineteen {
    public static final String example =
            "5: \"b\"\n" +
                    "0: 4 1 5\n" +
                    "1: 2 3 | 3 2\n" +
                    "2: 4 4 | 5 5\n" +
                    "3: 4 5 | 5 4\n" +
                    "4: \"a\"\n" +
                    "\n" +
                    "ababbb\n" +
                    "bababa\n" +
                    "abbbab\n" +
                    "aaabbb\n" +
                    "aaaabbb";


    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day19/day19"));
        System.out.println(getResult(read));
    }

    private static void validateExample() throws Exception {
        long result = getResult(example.split("\n"));
        if (result != 2) throw new Exception("Invalid implementation result is " + result);
    }

    private static long getResult(String[] lines) {
        List<String> rules = Arrays.stream(lines).filter($ -> $.contains(":")).collect(Collectors.toList());
        List<String> rulesToCheck = Arrays.stream(lines).filter($ -> $.matches("[ab]*")).collect(Collectors.toList());
        Result result = new Result();
//        result.addRulesPartOne(rules);
        result.addRulesPartTwo(rules);
        rulesMap = result.rules;
        return rulesToCheck.stream().filter($ -> isValid($, new ArrayList<>(rulesMap.get("0").rules.get(0)))).count();
    }

    private static Map<String, HelperDay19> rulesMap;

    private static boolean isValid(String line, List<String> rules) {
        if (line.length() < rules.size()) {
            return false;
        } else if (line.length() == 0 || rules.size() == 0) {
            return line.length() == 0 && rules.size() == 0;
        }

        HelperDay19 key = rulesMap.get(rules.remove(0));
        if (key.value != null) {
            if (line.charAt(0) == key.value.charAt(0)) {
                return isValid(line.substring(1), new ArrayList<>(rules));
            }
        } else {
            for (List<String> r : rulesMap.get(key.id).rules) {
                List<String> nextList = new ArrayList<>(r);
                nextList.addAll(rules);
                if (isValid(line, nextList)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Result {
        Map<String, HelperDay19> rules = new HashMap<>();

        public void addRulesPartOne(List<String> rulesAsStrings) {
            for (String rule : rulesAsStrings) {
                String[] idWithRulesOfId = rule.split(":");
                HelperDay19 helperDay19 = new HelperDay19(idWithRulesOfId[0]);
                String[] splitRules = idWithRulesOfId[1].split("[|]");
                for (int i = 0; i < splitRules.length; i++) {
                    String trimRules = splitRules[i].trim();
                    if (trimRules.contains("a") || trimRules.contains("b")) {
                        String value = trimRules.replaceAll("\"", "");
                        helperDay19.value = value;
                        break;
                    }
                    String[] s = splitRules[i].trim().split(" ");
                    helperDay19.rules.add(new ArrayList<>(Arrays.asList(s)));
                }
                rules.put(helperDay19.id, helperDay19);
            }
        }


        private Map<String, Set<String>> possibilities = new HashMap<>();

        private Set<String> concatenatePossibilities(List<String> rules, Set<String> pos, int depth, int max, String currValue) {
            if (depth == max) {
                pos.add(currValue);
                return pos;
            }

            Set<String> strings = possibilities.get(rules.get(depth));
            for (String string : strings) {
                String newCurrValue = currValue + string;
                concatenatePossibilities(rules, pos, depth + 1, max, newCurrValue);

            }
            return pos;
        }

        public void addRulesPartTwo(List<String> rulesAsStrings) {
            for (String rule : rulesAsStrings) {
                String[] idWithRulesOfId = rule.split(":");
                HelperDay19 helperDay19 = new HelperDay19(idWithRulesOfId[0]);
                if (helperDay19.id.equals("8")) {
                    idWithRulesOfId[1] = "42 | 42 8";
                } else if (helperDay19.id.equals("11")) {
                    idWithRulesOfId[1] = "42 31 | 42 11 31";
                }
                String[] splitRules = idWithRulesOfId[1].split("[|]");
                for (int i = 0; i < splitRules.length; i++) {
                    String trimRules = splitRules[i].trim();
                    if (trimRules.contains("a") || trimRules.contains("b")) {
                        String value = trimRules.replaceAll("\"", "");
                        helperDay19.value = value;
                        break;
                    }
                    String[] s = splitRules[i].trim().split(" ");
                    helperDay19.rules.add(new ArrayList<>(Arrays.asList(s)));
                }
                rules.put(helperDay19.id, helperDay19);
            }
        }
    }

    private static class HelperDay19 {
        private final String id;
        private List<List<String>> rules = new ArrayList<>();
        private String value = null;

        private HelperDay19(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Helper{" +
                    "id='" + id + '\'' +
                    ", rules=" + rules +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
