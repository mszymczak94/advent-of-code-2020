package com.szymczak.day6;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DaySix {
    private static String example = "abc\n" +
            "\n" +
            "a\n" +
            "b\n" +
            "c\n" +
            "\n" +
            "ab\n" +
            "ac\n" +
            "\n" +
            "a\n" +
            "a\n" +
            "a\n" +
            "a\n" +
            "\n" +
            "b";


    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day6/day6"));
        System.out.println(getResult(read));
    }

    private static void validateExample() throws Exception {
        String[] split = example.split("\n");
        int answerSum = getResult(split);
//        if (answerSum != 11) throw new Exception("Invalid implementation answerSum is " + answerSum);
        if (answerSum != 6) throw new Exception("Invalid implementation answerSum is " + answerSum);
    }

    private static int getResult(String[] lines) {
        lines = String
                .join(" ", lines)
                .replace("  ", ",")
                .split(",");

//        return new AnswerCollectorPartOne(lines).getCountsSum();
        return new AnswerCollectorPartTwo(lines).getCountsSum();
    }

    public static class AnswerCollectorPartOne implements AnswerCollector {
        private final String[] lines;

        public AnswerCollectorPartOne(String[] lines) {
            this.lines = lines;
        }

        @Override
        public int getCountsSum() {
            int sum = 0;
            for (String line : lines) {
                Set<Character> answers = new HashSet<>();
                String lineWithoutWhiteSpaces = line.replaceAll(" ", "");
                for (char c : lineWithoutWhiteSpaces.toCharArray()) {
                    answers.add(c);
                }
                sum += answers.size();
            }
            return sum;
        }
    }

    public static class AnswerCollectorPartTwo implements AnswerCollector {
        private final String[] lines;

        public AnswerCollectorPartTwo(String[] lines) {
            this.lines = lines;
        }

        @Override
        public int getCountsSum() {
            int sum = 0;
            for (String groups : lines) {
                String[] groupsInArr = groups.split(" ");
                if (groupsInArr.length == 1) {
                    sum += groupsInArr[0].length();
                    continue;
                }

                Set<Character> firstLineAnswers = groupsInArr[0]
                        .chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toSet());

                for (int i = 1; i < groupsInArr.length; i++) {
                    String group = groupsInArr[i];
                    Set<Character> currLine = group
                            .chars()
                            .mapToObj(c -> (char) c)
                            .collect(Collectors.toSet());
                    firstLineAnswers.retainAll(currLine);
                    if (firstLineAnswers.size() == 0) {
                        break;
                    }
                }
                sum += firstLineAnswers.size();
            }
            return sum;
        }
    }
}
