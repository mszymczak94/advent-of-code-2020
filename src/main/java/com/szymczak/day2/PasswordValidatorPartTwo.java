package com.szymczak.day2;

public class PasswordValidatorPartTwo implements PasswordValidator {
    private final int firstPosition;
    private final int secondPosition;
    private final char letter;
    private final String password;

    public PasswordValidatorPartTwo(int firstPosition, int secondPosition, char letter, String password) {
        this.firstPosition = normalizePosition(firstPosition);
        this.secondPosition = normalizePosition(secondPosition);
        this.letter = letter;
        this.password = password;
    }

    private int normalizePosition(int position) {
        return --position;
    }

    @Override
    public boolean isValid() {
        boolean isLetterOnFirstPosition = false;
        boolean isLetterOnSecondPosition = false;

        if (!(firstPosition < 0)) {
            isLetterOnFirstPosition = password.charAt(firstPosition) == letter;
        }

        if (!(secondPosition >= password.length())) {
            isLetterOnSecondPosition = password.charAt(secondPosition) == letter;
        }

        return isLetterOnFirstPosition ^ isLetterOnSecondPosition;
    }
}
