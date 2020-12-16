package com.szymczak.day7;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DaySeven {
    private static String example =
            "light red bags contain 1 bright white bag, 2 muted yellow bags.\n" +
                    "dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
                    "bright white bags contain 1 shiny gold bag.\n" +
                    "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\n" +
                    "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\n" +
                    "dark olive bags contain 3 faded blue bags, 4 dotted black bags.\n" +
                    "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\n" +
                    "faded blue bags contain no other bags.\n" +
                    "dotted black bags contain no other bags.";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day7/day7"));
        System.out.println(getResult(read));
        System.out.println(getResultPartTwo(read));
    }

    private static void validateExample() throws Exception {
        int result = getResult(example.split("\n"));
        if (result != 4) throw new Exception("Invalid implementation result is " + result);
        result = getResultPartTwo(example.split("\n"));
        if (result != 32) throw new Exception("Invalid implementation result is " + result);
    }

    private static int getResultPartTwo(String[] lines) {
        Map<String, Map<String, Integer>> preparedRules = getPreparedRules(lines);
        return bagsInsideShinyGold(preparedRules, preparedRules.get("shiny gold"));
    }

    private static int bagsInsideShinyGold(Map<String, Map<String, Integer>> preparedRules, Map<String, Integer> bag) {
        int counter = 0;
        if (bag.isEmpty()) {
            return 0;
        }
        for (Map.Entry<String, Integer> entry : bag.entrySet()) {
            Integer bagsAmount = entry.getValue();
            counter += bagsAmount + bagsAmount * bagsInsideShinyGold(preparedRules, preparedRules.get(entry.getKey()));
        }

        return counter;
    }

    private static int getResult(String[] lines) {
        Map<String, Map<String, Integer>> preparedRules = getPreparedRules(lines);
        return findShinyBagsPossibilities(preparedRules);
    }

    private static int findShinyBagsPossibilities(Map<String, Map<String, Integer>> preparedRules) {
        int counter = 0;
        for (Map.Entry<String, Map<String, Integer>> entrySet : preparedRules.entrySet()) {
            if (!entrySet.getKey().equals("shiny gold") && checkIsContainValue(entrySet.getKey(), entrySet.getValue(), preparedRules)) {
                counter++;
            }
        }
        return counter;
    }

    private static boolean checkIsContainValue(String key, Map<String, Integer> value, Map<String, Map<String, Integer>> preparedRules) {
        if (preparedRules.get(key).isEmpty()) {
            return false;
        }

        if (preparedRules.get(key).containsKey("shiny gold")) {
            return true;
        }
        boolean result = false;
        for (Map.Entry<String, Integer> entrySet : value.entrySet()) {
            String currKey = entrySet.getKey();
            Map<String, Integer> currValue = preparedRules.get(currKey);
            result = checkIsContainValue(currKey, currValue, preparedRules);
            if (result) break;
        }
        return result;
    }

    private static Map<String, Map<String, Integer>> getPreparedRules(String[] lines) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        for (String line : lines) {

            String[] groups = line.split(" bags contain ");
            String mainBag = groups[0];
            String[] bagsInside = groups[1].split(", ");
            Map<String, Integer> insideMainBag = new HashMap<>();
            for (String rule : bagsInside) {
                if (rule.matches("^[1-9].*")) {
                    String cleanedRule = rule.replaceAll("\\sbag[.|,s]*", "");
                    String[] splitCleanedRule = cleanedRule.split("\\s+", 2);
                    insideMainBag.put(splitCleanedRule[1], Integer.parseInt(splitCleanedRule[0]));
                }
            }
            map.put(mainBag, insideMainBag);
        }
        return map;
    }
}
