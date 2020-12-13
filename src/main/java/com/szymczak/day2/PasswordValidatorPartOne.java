package com.szymczak.day2;

public class PasswordValidatorPartOne implements PasswordValidator {
    private final int minOccurrence;
    private final int maxOccurrence;
    private final String letter;
    private final String password;

    public PasswordValidatorPartOne(int minOccurrence, int maxOccurrence, String letter, String password) {
        this.minOccurrence = minOccurrence;
        this.maxOccurrence = maxOccurrence;
        this.letter = letter;
        this.password = password;
    }

    @Override
    public boolean isValid() {
        boolean isContainLetter = password.contains(letter);
        if (!isContainLetter && minOccurrence > 0 || isContainLetter && minOccurrence == 0)
            return false;
        int occurrence = 0;
        String letters = password.replaceAll(String.format("[^%s]", letter), "");
        int length = letters.length();
        return minOccurrence <= length && length <= maxOccurrence;
    }
}
