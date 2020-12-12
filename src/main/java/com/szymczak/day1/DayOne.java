package com.szymczak.day1;

import com.szymczak.util.FileReader;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
public class DayOne {
    private final static int EXPECTED_SUM = 2020;

    public static void main(String[] args) throws Exception {
        String[] numbers = {
                "1721",
                "979",
                "366",
                "299",
                "675",
                "1456"};
        long result = getResultPartOne(numbers);
        if (result != 514579) throw new Exception("Invalid result");
        System.out.println(result);

        String[] values = FileReader.read(new File("src/main/java/com/szymczak/day1/day1"));
        System.out.println(getResultPartOne(values));
        System.out.println(getResultPartTwo(values));
    }

    private static long getResultPartOne(String[] numbers) throws Exception {
        List<Integer> numbersInInt = tryParseToNumber(numbers);
        return findTwoNumbers(numbersInInt, numbersInInt.size(), EXPECTED_SUM);
    }
    private static long getResultPartTwo(String[] numbers) throws Exception {
        List<Integer> numbersInInt = tryParseToNumber(numbers);
        return findThreeNumbers(tryParseToNumber(numbers), numbersInInt.size());
    }

    private static long findTwoNumbers(List<Integer> numbersInInt, int length, int expectedValue) {
        if (length < 2) return -1;

        Integer number = numbersInInt.get(0);
        Integer numberToFind = expectedValue - number;
        if (numbersInInt.contains(numberToFind)) {
            return number * numberToFind;
        }

        numbersInInt.remove(number);
        return findTwoNumbers(numbersInInt, length - 1, expectedValue);
    }

    private static long findThreeNumbers(List<Integer> numbersInInt, int length) throws Exception {
        if (length < 3) throw new Exception("Something went wrong!");

        Integer number = numbersInInt.get(0);
        Integer toExpectedScore = EXPECTED_SUM - number;

        List<Integer> filteredValues = numbersInInt.stream()
                .filter($ -> toExpectedScore > $).collect(Collectors.toList());

        long score = findTwoNumbers(filteredValues, filteredValues.size(), toExpectedScore);
        if (score != -1) {
            return score * number;
        }

        numbersInInt.remove(number);
        return findThreeNumbers(numbersInInt, length - 1);
    }

    private static List<Integer> tryParseToNumber(String[] numbers) {
        return Arrays.stream(numbers)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

    }
}
