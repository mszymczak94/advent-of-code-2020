package com.szymczak.day17;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DaySeventeen {
    public static final String example = ".#.\n" +
            "..#\n" +
            "###";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day17/day17"));
        Set<Cube> cubes = prepareDataCube(read);
        System.out.println(getScorePartOne(cubes));
        System.out.println(getScorePartTwo(cubes));
    }

    private static void validateExample() throws Exception {
        Set<Cube> cubes = prepareDataCube(example.split("\n"));
        int score3d = getScorePartOne(cubes);
        int score4d = getScorePartTwo(cubes);
        if (112 != score3d) throw new Exception("Invalid implementation result is " + score3d);
        if (848 != score4d) throw new Exception("Invalid implementation result is " + score4d);
    }


    private static int getScorePartOne(Set<Cube> cubes) {
        for (int i = 0; i < 6; i++) {
            Set<Cube> newPosCubes = new HashSet<>();
            Set<Cube> checkPosCubes = new HashSet<>();
            for (Cube cube : cubes) {
                checkNeighboursForPartOne(cubes, newPosCubes, checkPosCubes, cube, true);
            }
            cubes = newPosCubes;
        }
        return cubes.size();
    }

    private static int getScorePartTwo(Set<Cube> cubes) {
        for (int i = 0; i < 6; i++) {
            Set<Cube> newPosCubes = new HashSet<>();
            Set<Cube> checkPosCubes = new HashSet<>();
            for (Cube cube : cubes) {
                checkNeighboursForPartTwo(cubes, newPosCubes, checkPosCubes, cube, true);
            }
            cubes = newPosCubes;
        }
        return cubes.size();
    }

    private static void checkNeighboursForPartTwo(Set<Cube> cubes, Set<Cube> newPosCubes, Set<Cube> checkPosCubes, Cube cube, boolean isActive) {
        if (!checkPosCubes.contains(cube)) {
            int amountOfNeighbours = 0;
            checkPosCubes.add(cube);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        for (int w = -1; w <= 1; w++) {

                            Cube currCube = new CubeImpl(cube.getX() + x, cube.getY() + y, cube.getZ() + z, cube.getW() + w);
                            if (cubes.contains(currCube)) {
                                if (currCube.equals(cube)) {
                                    continue;
                                }
                                amountOfNeighbours++;
                            } else if (isActive) {
                                checkNeighboursForPartTwo(cubes, newPosCubes, checkPosCubes, currCube, false);
                            }
                        }
                    }
                }
            }

            if (amountOfNeighbours == 3 || (isActive && amountOfNeighbours == 2)) {
                newPosCubes.add(cube);
            }
        }
    }

    private static void checkNeighboursForPartOne(Set<Cube> cubes, Set<Cube> newPosCubes, Set<Cube> checkPosCubes, Cube cube, boolean isActive) {
        if (!checkPosCubes.contains(cube)) {
            int amountOfNeighbours = 0;
            checkPosCubes.add(cube);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Cube currCube = new CubeImpl(cube.getX() + x, cube.getY() + y, cube.getZ() + z);
                        if (cubes.contains(currCube)) {
                            if (currCube.equals(cube)) {
                                continue;
                            }
                            amountOfNeighbours++;
                        } else if (isActive) {
                            checkNeighboursForPartOne(cubes, newPosCubes, checkPosCubes, currCube, false);
                        }
                    }
                }
            }

            if (amountOfNeighbours == 3 || (isActive && amountOfNeighbours == 2)) {
                newPosCubes.add(cube);
            }
        }
    }

    private static Set<Cube> prepareDataCube(String[] example) {
        Set<Cube> cubes = new HashSet<>();
        for (int x = 0; x < example.length; x++) {
            String s = example[x];
            for (int y = 0; y < s.length(); y++) {
                char c = s.charAt(y);
                if (c == '#') {
                    cubes.add(new CubeImpl(x, y, 0));
                }
            }
        }
        return cubes;
    }

    private static class CubeImpl implements Cube {
        private int x;
        private int y;
        private int z;
        private int w;

        public CubeImpl(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public CubeImpl(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CubeImpl cube = (CubeImpl) o;
            return x == cube.x &&
                    y == cube.y &&
                    z == cube.z &&
                    w == cube.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public int getZ() {
            return z;
        }

        @Override
        public int getW() {
            return w;
        }
    }

    private interface Cube {
        int getX();

        int getY();

        int getZ();

        int getW();
    }
}
