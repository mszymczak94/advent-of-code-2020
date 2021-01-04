package com.szymczak.day16;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DaySixteen {
    private static String example =
            "class: 0-1 or 4-19\n" +
                    "row: 0-5 or 8-19\n" +
                    "seat: 0-13 or 16-19\n" +
                    "\n" +
                    "your ticket:\n" +
                    "11,12,13\n" +
                    "\n" +
                    "nearby tickets:\n" +
                    "3,9,18\n" +
                    "15,1,5\n" +
                    "5,14,9";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day16/day16"));
        DataManager dataManager = prepareData(read);
        System.out.println(dataManager.summedInvalidValues);
        Map<String, Integer> fields = dataManager.findFields();
        printDepartureValues(fields, dataManager);
    }

    private static void printDepartureValues(Map<String, Integer> fields, DataManager dataManager) {
        int[] departures = fields.entrySet()
                .stream()
                .filter($ -> $.getKey().startsWith("departure"))
                .mapToInt(Map.Entry::getValue)
                .toArray();
        long counter = 1;
        String[] myTicket = dataManager.validTickets.get(0);
        for (int departure : departures) {
            int value = Integer.parseInt(myTicket[departure]);
            counter *= value;
        }
        System.out.println(counter);
    }

    private static void validateExample() throws Exception {
        String[] split = example.split("\n");
        DataManager dataManager = prepareData(split);
        if (dataManager.summedInvalidValues != 0)
            throw new Exception("Invalid implementation result is " + dataManager.summedInvalidValues);
        dataManager.findFields();
    }

    private static DataManager prepareData(String[] lines) {
        DataManager dataManager = new DataManager();
        for (String line : lines) {
            if (line.isEmpty() || line.equals("your ticket:") || line.equals("nearby tickets:")) {
                continue;
            }
            if (line.contains(":")) {
                dataManager.addRule(line);
                continue;
            }
            boolean valid = dataManager.isValid(line);

            if (valid) {
                dataManager.validTickets.add(line.split(","));
            }
        }
        return dataManager;
    }

    private static class DataManager {
        private Map<String, Set<String>> validValues = new HashMap<>();
        private int summedInvalidValues = 0;
        private static Pattern pattern = Pattern.compile("[0-9]*-[0-9]*");
        private List<String[]> validTickets = new ArrayList<>();

        public void addRule(String line) {
            String[] descAndRule = line.split(":");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String[] range = matcher.group().split("-");
                Set<String> strings = validValues.computeIfAbsent(descAndRule[0], $ -> new HashSet<>());
                addRule(strings, Integer.parseInt(range[0]), Integer.parseInt(range[1]));
            }
        }

        private void addRule(Set<String> rules, int start, int end) {
            for (int i = start; i <= end; i++) {
                rules.add(String.valueOf(i));
            }
        }

        private Map<String, Boolean> cache = new HashMap<>();

        public boolean isValid(String line) {
            Set<String> validValuesFlatMap = validValues
                    .values()
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            boolean hasChanged = false;
            for (String value : line.split(",")) {
                Boolean isValid = cache.computeIfAbsent(value, s -> validValuesFlatMap.contains(value));
                if (!isValid) {
                    hasChanged = true;
                    summedInvalidValues += Integer.parseInt(value);
                }
            }
            return !hasChanged;
        }

        public Map<String, Integer> findFields() {
            int index = 0;
            return findField(new HashSet<>(validValues.keySet()), index, new HashMap<>(), false);
        }

        private Map<String, Integer> findField(Set<String> keys, int index, HashMap<String, Integer> fields, boolean isCalledInside) {

            if (keys.isEmpty()) {
                return fields;
            }
            int finalIndex = index;
            List<String> collect = validTickets
                    .stream()
                    .map($ -> $[finalIndex])
                    .collect(Collectors.toList());

            while (!keys.isEmpty()) {
                List<String> keysMatch = validValues.entrySet().stream()
                        .filter($ -> keys.contains($.getKey()))
                        .filter($ -> $.getValue().containsAll(collect))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                if (keysMatch.size() == 1) {
                    fields.put(keysMatch.get(0), finalIndex);
                    keys.remove(keysMatch.get(0));
                    if (isCalledInside) {
                        return fields;
                    }
                }
                findField(keys, index + 1, fields, true);
                if (isCalledInside) {
                    return fields;
                }
            }
            return fields;
        }

    }
}
