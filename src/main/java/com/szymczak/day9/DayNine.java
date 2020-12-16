package com.szymczak.day9;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DayNine {
    private static String example = "35\n" +
            "20\n" +
            "15\n" +
            "25\n" +
            "47\n" +
            "40\n" +
            "62\n" +
            "55\n" +
            "65\n" +
            "95\n" +
            "102\n" +
            "117\n" +
            "150\n" +
            "182\n" +
            "127\n" +
            "219\n" +
            "299\n" +
            "277\n" +
            "309\n" +
            "576";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day9/day9"));
        System.out.println(getResult(read, 25));
        System.out.println(getResultPartTwo(read, 25));
    }

    private static void validateExample() throws Exception {
        long result = getResult(example.split("\n"), 5);
        if (result != 127) throw new Exception("Invalid implementation result is " + result);

        long resultTwo = getResultPartTwo(example.split("\n"), 5);
        if (resultTwo != 62) throw new Exception("Invalid implementation result is " + result);
    }

    private static long getResultPartTwo(String[] lines, int preamble) {
        List<Long> valuesAsInt = prepareList(lines);
        long inconsistency = getIncorrectValue(valuesAsInt, preamble, preamble);
        return sumOfSmallestAndHighestValuesInSet(valuesAsInt, inconsistency, 0);
    }

    private static long sumOfSmallestAndHighestValuesInSet(List<Long> valuesAsInt, long inconsistency, int index) {
        Set<Long> valuesFounded = new HashSet<>();
        int lastIndex = valuesAsInt.indexOf(inconsistency);
        long sum = -1;
        for (int i = index; i < lastIndex && sum < inconsistency; i++) {
            valuesFounded.add(valuesAsInt.get(i));

            sum = valuesFounded
                    .stream()
                    .mapToLong(Long::longValue)
                    .sum();

            if (sum == inconsistency) {
                List<Long> sortedValues = valuesFounded.stream().sorted().collect(Collectors.toList());
                return sortedValues.get(0) + sortedValues.get(sortedValues.size() - 1);
            }
        }
        return sumOfSmallestAndHighestValuesInSet(valuesAsInt, inconsistency, ++index);
    }

    private static long getResult(String[] lines, int preamble) {
        List<Long> valuesAsInt = prepareList(lines);
        return getIncorrectValue(valuesAsInt, preamble, preamble);
    }

    private static long getIncorrectValue(List<Long> valuesAsInt, int currIndexToCheck, int preamble) {
        Long valueToFind = valuesAsInt.get(currIndexToCheck);
        boolean isValueFound = false;
        for (int i = currIndexToCheck - preamble; i < currIndexToCheck && !isValueFound; i++) {
            Long currValue = valuesAsInt.get(i);
            for (int j = i + 1; j < currIndexToCheck; j++) {
                if (currValue + valuesAsInt.get(j) == valueToFind) {
                    isValueFound = true;
                }
            }
        }

        if (!isValueFound) {
            return valueToFind;
        }

        currIndexToCheck++;
        if (valuesAsInt.size() > currIndexToCheck) {
            return getIncorrectValue(valuesAsInt, currIndexToCheck, preamble);
        }

        throw new RuntimeException("Invalid implementation");
    }

    private static List<Long> prepareList(String[] lines) {
        return Arrays.stream(lines)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
