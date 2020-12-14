package com.szymczak.day5;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DayFive {
    private static final String[] example = {"FBFBBFFRLR"};

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day5/day5"));
        System.out.println(getResult(read, 127, 7));
        System.out.println(getMineSeat(read, 127, 7));
    }

    private static int getMineSeat(String[] lines, int rows, int cols) {
        List<Integer> seats = getSeats(lines, rows, cols);
        System.out.println(seats);
        int checkingSeat = seats.get(0);
        for (int i = 0; i < seats.size();) {
            int currSeat = seats.get(i);
            if (!seats.contains(checkingSeat)) {
                if (seats.contains(checkingSeat + 1) && seats.contains(checkingSeat - 1)){
                    return checkingSeat;
                }
                checkingSeat++;
                continue;
            }
            i++;
            checkingSeat++;
        }
        throw new RuntimeException("Seat not found, invalid implementation");
    }

    private static void validateExample() throws Exception {
        int seatID = getResult(example, 127, 7);
        if (seatID != 357) throw new Exception("Invalid implementation: " + seatID);
    }

    private static int getResult(String[] lines, int rows, int cols) {
        List<Integer> sortedSeats = getSeats(lines, rows, cols);
        return sortedSeats.get(sortedSeats.size() - 1);
    }

    private static List<Integer> getSeats(String[] lines, int rows, int cols) {
        List<Integer> seats = new ArrayList<>();
        for (String line : lines) {
            int row = findPlace(line.substring(0, 7), 0, rows);
            int col = findPlace(line.substring(7), 0, cols);
            int seatId = row * 8 + col;
            seats.add(seatId);
        }

        return seats.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private static int findPlace(String value, int left, int right) {
        if (left == right) {
            return left;
        }
        char c = value.charAt(0);
        if ('B' == c || 'R' == c) {
            return findPlace(value.substring(1), (right - left) / 2 + 1 + left, right);
        }
        if ('F' == c || 'L' == c) {
            return findPlace(value.substring(1), left, (right - left) / 2 + left);
        }
        throw new RuntimeException("Wrong implementation");
    }
}
