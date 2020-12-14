package com.szymczak.day3;

import com.szymczak.utils.FileReader;

import java.io.File;

/*
    ..##.........##.........##.........##.........##.........##.......  --->
    #..O#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..
    .#....X..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.
    ..#.#...#O#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#
    .#...##..#..X...##..#..#...##..#..#...##..#..#...##..#..#...##..#.
    ..#.##.......#.X#.......#.##.......#.##.......#.##.......#.##.....  --->
    .#.#.#....#.#.#.#.O..#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#
    .#........#.#........X.#........#.#........#.#........#.#........#
    #.##...#...#.##...#...#.X#...#...#.##...#...#.##...#...#.##...#...
    #...##....##...##....##...#X....##...##....##...##....##...##....#
    .#..#...#.#.#..#...#.#.#..#...X.#.#..#...#.#.#..#...#.#.#..#...#.#

    Met 7 trees.
 */
public class DayThree {
    private static final int[][] SLOPES = {{1,1}, {3,1}, {5, 1}, {7, 1}, {1,2}};
    private static String example = "..##.......\n" +
            "#...#...#..\n" +
            ".#....#..#.\n" +
            "..#.#...#.#\n" +
            ".#...##..#.\n" +
            "..#.##.....\n" +
            ".#.#.#....#\n" +
            ".#........#\n" +
            "#.##...#...\n" +
            "#...##....#\n" +
            ".#..#...#.#";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day3/day3"));
        char[][] chars = prepareTwoDimensionalTable(read);
        System.out.println(getResult(chars, 3, 1));
        int result = 1;
        for (int[] slope : SLOPES) {
            int trees = getResult(chars, slope[0], slope[1]);
            result *= trees;
        }
        System.out.println(result);
    }

    private static void validateExample() throws Exception {
        String[] lines = example.split("\n");
        char[][] map = prepareTwoDimensionalTable(lines);
        int result = 1;
        for (int[] slope : SLOPES) {
            int trees = getResult(map, slope[0], slope[1]);
            result *= trees;
        }
        if (result != 336) throw new Exception("Invalid implementation, met " + result + " trees" );
    }

    private static char[][] prepareTwoDimensionalTable(String[] lines) {
        char[][] map = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
        }
        return map;
    }

    private static int getResult(char[][] map, int x, int y) {
        Flight flight = new Flight(
                new Position(0, 0, new PositionValidator(map[0].length)),
                map.length - 1,
                new Slope(x, y));
        while (!flight.hasLanded()) {
            flight.fly(map);
        }
        return flight.getMetTrees();
    }

    public static class Flight {
        private int trees = 0;
        private final int destination;
        private final Position position;
        private final Slope slope;

        public Position getPosition() {
            return position;
        }

        public Flight(Position position, int destination, Slope slope) {
            this.position = position;
            this.destination = destination;
            this.slope = slope;
        }

        public int getMetTrees() {
            return trees;
        }

        public void addNextMeetTree(){
            this.trees++;
        }

        public boolean hasLanded() {
            return position.getY() == destination;
        }

        public void fly(char[][] map) {
            position.setX(position.getX() + slope.getX());
            position.setY(position.getY() + slope.getY());
            if (isTreeInSquare(map[position.getY()][position.getX()])){
                addNextMeetTree();
            }

        }

        private boolean isTreeInSquare(char c) {
           return '#' == c;
        }
    }

    public static class Position {
        private int x;
        private int y;
        private final PositionValidator positionValidator;

        public Position(int x, int y, PositionValidator positionValidator) {
            this.x = x;
            this.y = y;
            this.positionValidator = positionValidator;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = positionValidator.getValidPositionOnTheWorld(x);
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return this.x;
        }
    }

    private static class Slope {
        private final int x;
        private final int y;

        private Slope(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
