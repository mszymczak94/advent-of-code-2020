package com.szymczak.day11;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayEleven {
    private static String example = "L.LL.LL.LL\n" +
            "LLLLLLL.LL\n" +
            "L.L.L..L..\n" +
            "LLLL.LL.LL\n" +
            "L.LL.LL.LL\n" +
            "L.LLLLL.LL\n" +
            "..L.L.....\n" +
            "LLLLLLLLLL\n" +
            "L.LLLLLL.L\n" +
            "L.LLLLL.LL";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day11/day11"));
        System.out.println(getOccupiedSeats(prepareMapOfSeats(read)));
    }

    private static void validateExample() throws Exception {
        char[][] map = prepareMapOfSeats(example.split("\n"));
        int occupiedSeats = getOccupiedSeats(map);
        if (occupiedSeats != 26) throw new Exception("Invalid imeplementation result is " + occupiedSeats);
    }

    private static int getOccupiedSeats(char[][] map) {
        if (moveActions(map)) {
            return getOccupiedSeats(map);
        }
        return countOccupiedSeats(map);
    }

    private static int countOccupiedSeats(char[][] map) {
        int counter = 0;
        for (char[] chars : map) {
            for (char aChar : chars) {
                if (aChar == '#') counter++;
            }
        }
        return counter;
    }

    private static SeatValidator seatValidator = new SeatValidator();

    private static boolean moveActions(char[][] map) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (seatValidator.shouldChangeState(i, j, map)) {
                    moves.add(new Move(i, j, map[i][j]));
                }
            }
        }

        if (moves.isEmpty()) {
            return false;
        }

        moves.forEach($ -> map[$.getX()][$.getY()] = $.info);
        return true;
    }

    private static char[][] prepareMapOfSeats(String[] split) {
        char[][] map = new char[split.length][];
        for (int i = 0; i < split.length; i++) {
            char[] chars = split[i].toCharArray();
            map[i] = chars;
        }
        return map;
    }


    private static class Move {
        private final int x;
        private final int y;
        private final char info;

        private Move(int x, int y, char info) {
            this.x = x;
            this.y = y;
            this.info = info == 'L' ? '#' : 'L';
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public char getInfo() {
            return info;
        }
    }

    private static class SeatValidator {
        public boolean shouldChangeState(int i, int j, char[][] map) {
            char position = map[i][j];
            switch (position) {
                case 'L':
                    return emptySeatValidation(i, j, map);
                case '#':
                    return occupiedSeatValidation(i, j, map);
                case '.':
                default:
                    return false;
            }
        }

        private boolean occupiedSeatValidation(int i, int j, char[][] map) {
            return Arrays.stream(Direction.values())
                    .filter($ -> areOccupiedSeatInPersonEyes($, i, j, map))
//                    .count() >= 4; Part One
                    .count() >= 5;
        }

        private boolean emptySeatValidation(int i, int j, char[][] map) {
            return Arrays.stream(Direction.values())
//                    .noneMatch($ -> $.isValid(i, j, map) && $.getPosition(i, j, map) == '#')
                    .noneMatch($ -> areOccupiedSeatInPersonEyes($, i, j, map));
        }

        private boolean areOccupiedSeatInPersonEyes(Direction direction, int i, int j, char[][] map) {
            if (!direction.isValid(i, j, map)) {
                return false;
            }
            char position = direction.getPosition(i, j, map);
            if (position == '#') {
                return true;
            } else if (position == 'L') {
                return false;
            }
            i += direction.getXVector();
            j += direction.getYVector();
            return areOccupiedSeatInPersonEyes(direction, i, j, map);
        }
    }

    private enum Direction {
        RIGHT {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return j + 1 < map[i].length;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i][j + 1];
            }

            @Override
            public int getXVector() {
                return 0;
            }

            @Override
            public int getYVector() {
                return 1;
            }

        }, LEFT {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return j - 1 >= 0;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i][j - 1];
            }

            @Override
            public int getXVector() {
                return 0;
            }

            @Override
            public int getYVector() {
                return -1;
            }

        }, UP {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return i - 1 >= 0;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i - 1][j];
            }

            @Override
            public int getXVector() {
                return -1;
            }

            @Override
            public int getYVector() {
                return 0;
            }
        }, DOWN {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return i + 1 < map.length;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i + 1][j];
            }

            @Override
            public int getXVector() {
                return 1;
            }

            @Override
            public int getYVector() {
                return 0;
            }
        }, RIGHT_DOWN {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return j + 1 < map[i].length && i + 1 < map.length;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i + 1][j + 1];
            }

            @Override
            public int getXVector() {
                return 1;
            }

            @Override
            public int getYVector() {
                return 1;
            }
        }, LEFT_DOWN {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return i + 1 < map.length && j - 1 >= 0;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i + 1][j - 1];
            }

            @Override
            public int getXVector() {
                return 1;
            }

            @Override
            public int getYVector() {
                return -1;
            }
        }, RIGHT_UP {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return i - 1 >= 0 && j + 1 < map[i].length;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i - 1][j + 1];
            }

            @Override
            public int getXVector() {
                return -1;
            }

            @Override
            public int getYVector() {
                return 1;
            }
        }, LEFT_UP {
            @Override
            public boolean isValid(int i, int j, char[][] map) {
                return i - 1 >= 0 && j - 1 >= 0;
            }

            @Override
            public char getPosition(int i, int j, char[][] map) {
                return map[i - 1][j - 1];
            }

            @Override
            public int getXVector() {
                return -1;
            }

            @Override
            public int getYVector() {
                return -1;
            }
        };

        public abstract boolean isValid(int i, int j, char[][] map);

        public abstract char getPosition(int i, int j, char[][] map);

        public abstract int getXVector();

        public abstract int getYVector();
    }
}
