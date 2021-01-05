package com.szymczak.day18;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DayEighteen {
    private static String example = "1 + (2 * 3) + (4 * (5 + 6))\n" +
            "1 + 2 * 3 + 4 * 5 + 6";
    private static String example2 = "2 * 3 + (4 * 5)";
    private static String example3 = "5 + (8 * 3 + 9 + 3 * 4 * 3)";
    private static String example4 = "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))";
    private static String example5 = "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day18/day18"));
        System.out.println(getResult(read));
        System.out.println(getResult2(read));
    }

    private static void validateExample() throws Exception {
        long result = getResult(example.split("\n"));
        if (result != 122) throw new Exception("Invalid implementation result is " + result);
        result = getResult(example2.split("\n"));
        if (result != 26) throw new Exception("Invalid implementation result is " + result);
        result = getResult(example3.split("\n"));
        if (result != 437) throw new Exception("Invalid implementation result is " + result);
        result = getResult(example4.split("\n"));
        if (result != 12240) throw new Exception("Invalid implementation result is " + result);
        result = getResult(example5.split("\n"));
        if (result != 13632) throw new Exception("Invalid implementation result is " + result);
        result = getResult2(example.split("\n"));
        if (result != 282) throw new Exception("Invalid implementation result is " + result);
        result = getResult2(example2.split("\n"));
        if (result != 46) throw new Exception("Invalid implementation result is " + result);
        result = getResult2(example3.split("\n"));
        if (result != 1445) throw new Exception("Invalid implementation result is " + result);
        result = getResult2(example4.split("\n"));
        if (result != 669060) throw new Exception("Invalid implementation result is " + result);
        result = getResult2(example5.split("\n"));
        if (result != 23340) throw new Exception("Invalid implementation result is " + result);

    }

    private static long getResult(String[] lines) {
        long counter = 0;
        for (String line : lines) {
            counter += getScoreOfPartOne(line);
            if (counter < 0) {
                System.out.println(counter);
            }
        }
        return counter;
    }

    private static long getResult2(String[] lines) {
        long counter = 0;
        for (String line : lines) {
            counter += getScoreOfPartTwo(line);
        }
        return counter;
    }

    private static Set<String> signs = new HashSet<>(Arrays.asList("+", "*"));

    private static long getScoreOfPartOne(String line) {
        String lineWithoutSpaces = line.replaceAll(" ", "");
        Stack<String> stack = new Stack<>();
        for (String letter : lineWithoutSpaces.split("")) {
            if (stack.isEmpty() || signs.contains(letter) || letter.equals("(")) {
                stack.push(letter);
                continue;
            }

            if (signs.contains(stack.peek())) {
                String operator = stack.pop();
                String value = stack.pop();
                String calcResult = doCalculation(letter, value, operator);
                stack.push(calcResult);
                continue;
            }

            if (stack.peek().equals("(")) {
                stack.push(letter);
                continue;
            }

            if (letter.equals(")")) {
                StringBuilder builder = new StringBuilder();
                while (true) {
                    String pop = stack.pop();
                    if (pop.equals("(")) {
                        String calcResult = doCalculationOnString(builder.toString());
                        while (!stack.isEmpty() && !stack.peek().equals("(")) {
                            String operator = stack.pop();
                            String valueFirst = stack.pop();
                            calcResult = doCalculation(valueFirst, calcResult, operator);
                        }
                        stack.push(calcResult);
                        break;
                    }
                    builder.append(pop);
                }
            }
        }
        return Long.parseLong(stack.pop());
    }

    private static String doCalculationOnString(String values) {
        try {
            return String.valueOf(Integer.parseInt(values));
        } catch (Exception exception) {
            String[] split = values.split("[*]|[+]");
            if (values.contains("*")) {
                return doCalculation(split[0], split[1], "*");
            }
            return doCalculation(split[0], split[1], "+");
        }
    }

    private static String doMultiplicationOnString(String values) {
        try {
            return String.valueOf(Long.parseLong(values));
        } catch (Exception exception) {
            return String.valueOf(Arrays
                    .stream(values.split("[*]"))
                    .mapToLong(Long::parseLong)
                    .reduce(1, (a, b) -> a * b));
        }
    }

    private static String doCalculation(String valueOne, String valueTwo, String operator) {
        switch (operator) {
            case "+":
                return String.valueOf(Long.parseLong(valueOne) + Long.parseLong(valueTwo));
            case "*":
                return String.valueOf(Long.parseLong(valueOne) * Long.parseLong(valueTwo));
            default:
                throw new RuntimeException("Something went wrong operator is " + operator);
        }
    }

    private static long getScoreOfPartTwo(String line) {
        String lineWithoutSpaces = line.replaceAll(" ", "");
        Stack<String> stack = new Stack<>();
        for (String letter : lineWithoutSpaces.split("")) {
            if (stack.isEmpty() || signs.contains(letter) || letter.equals("(")) {
                stack.push(letter);
                continue;
            }

            if (stack.peek().equals("+")) {
                String operator = stack.pop();
                String value = stack.pop();
                String calcResult = doCalculation(letter, value, operator);
                stack.push(calcResult);
                continue;
            }

            if (stack.peek().equals("(")) {
                stack.push(letter);
                continue;
            }

            if (letter.equals(")")) {
                StringBuilder builder = new StringBuilder();
                while (true) {
                    String pop = stack.pop();
                    if (pop.equals("(")) {
                        String calcResult = doMultiplicationOnString(builder.toString());
                        while (!stack.isEmpty() && !stack.peek().equals("(") && !stack.peek().equals("*")){
                            String operator = stack.pop();
                            String valueFirst = stack.pop();
                            calcResult = doCalculation(valueFirst, calcResult, operator);
                        }
                        stack.push(calcResult);
                        break;
                    }
                    builder.append(pop);
                }
            }

            if (stack.peek().equals("*")) {
                stack.push(letter);
            }
        }
        String result = doMultiplicationOnString(String.join("", stack));
        return Long.parseLong(result);
    }
}
