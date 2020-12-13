package com.szymczak.day2;

import com.szymczak.utils.FileReader;

import java.io.File;

public class DayTwo {
    public static void main(String[] args) throws Exception {
        validateTestExample();
        String[] fileLines = FileReader.read(new File("src/main/java/com/szymczak/day2/day2"));
        System.out.println(getResult(fileLines));
    }

    private static void validateTestExample() throws Exception {
        String example = "1-3 a: abcde\n" +
                "1-3 b: cdefg\n" +
                "2-9 c: ccccccccc";
        String[] lines = example.split("\n");
        int occurrenceOfValidPasswords = getResult(lines);
        // Part one
//        if (occurrenceOfValidPasswords != 2) throw new Exception("Invalid implementation");
        // Part two
        if (occurrenceOfValidPasswords != 1) throw new Exception("Invalid implementation");
    }

    private static int getResult(String[] lines) {
        int validPasswords = 0;
        for (String line : lines) {
            if (isValidPassword(line)) {
                validPasswords++;
            }
        }
        return validPasswords;
    }

    private static boolean isValidPassword(String line) {
        if (line == null) {
            return false;
        }
        PasswordValidator passwordValidator = buildPasswordValidator(line);
        return passwordValidator.isValid();
    }

    private static PasswordValidator buildPasswordValidator(String line) {
        String[] policyAndPassword = line.split(":");
        String[] policy = policyAndPassword[0].split("-|\\s+");
        String password = policyAndPassword[1].trim();

        /* Part one
        return new PasswordValidatorPartOne(
                Integer.parseInt(policy[0]),
                Integer.parseInt(policy[1]),
                policy[2],
                password);
        */

        return new PasswordValidatorPartTwo(
                Integer.parseInt(policy[0]),
                Integer.parseInt(policy[1]),
                policy[2].charAt(0),
                password);
    }
}
