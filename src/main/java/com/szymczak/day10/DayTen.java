package com.szymczak.day10;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayTen {
    private static String example =
            "16\n" +
                    "10\n" +
                    "15\n" +
                    "5\n" +
                    "1\n" +
                    "11\n" +
                    "7\n" +
                    "19\n" +
                    "6\n" +
                    "12\n" +
                    "4";
    private static String biggerExample = "28\n" +
            "33\n" +
            "18\n" +
            "42\n" +
            "31\n" +
            "14\n" +
            "46\n" +
            "20\n" +
            "48\n" +
            "47\n" +
            "24\n" +
            "23\n" +
            "49\n" +
            "45\n" +
            "19\n" +
            "38\n" +
            "39\n" +
            "11\n" +
            "1\n" +
            "32\n" +
            "25\n" +
            "35\n" +
            "8\n" +
            "17\n" +
            "7\n" +
            "9\n" +
            "4\n" +
            "2\n" +
            "34\n" +
            "10\n" +
            "3";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day10/day10"));
        AdapterHelper adapterHelper = getResult(read);
        System.out.println(adapterHelper.getDifferenceOfJoltOne() * adapterHelper.getDifferenceOfJoltThree());
        System.out.println(adapterHelper.getAllAdapterConnectionsPossibilities());

    }

    private static void validateExample() throws Exception {
        AdapterHelper result = getResult(example.split("\n"));
        if (result.getDifferenceOfJoltOne() * result.getDifferenceOfJoltThree() != 35) {
            throw new Exception("Invalid implementation result is " + result);
        }
        if (result.getAllAdapterConnectionsPossibilities() != 8) {
            throw new Exception("Invalid implementation result is " + result.getAllAdapterConnectionsPossibilities());
        }
        AdapterHelper biggerExampleAdapter = getResult(biggerExample.split("\n"));
        if (biggerExampleAdapter.getAllAdapterConnectionsPossibilities() != 19208) {
            throw new Exception("Invalid implementation result is " + result.getAllAdapterConnectionsPossibilities());
        }

    }

    private static AdapterHelper getResult(String[] lines) {
        List<Integer> integers = prepareValuesAsInt(lines);
        AdapterHelper adapterHelper = new AdapterHelper(integers);
        adapterHelper.checkAdapters();

        return adapterHelper;
    }

    private static List<Integer> prepareValuesAsInt(String[] lines) {
        return Arrays.stream(lines).map(Integer::parseInt).sorted().collect(Collectors.toList());
    }

    private static class AdapterHelper {
        private final List<Integer> integers;
        private static int currJolt = 1;
        private static final int JOLT_ONE = 1;
        private static final int JOLT_THREE = 3;
        private int differenceOfJoltOne = 0;
        private int differenceOfJoltThree = 0;
        private final int highestAdapter;
        private final Map<Integer, Long> allAdapterConnectionsPossibilities;

        private AdapterHelper(List<Integer> integers) {
            this.integers = integers;

            this.highestAdapter = integers.get(integers.size() - 1);
            this.allAdapterConnectionsPossibilities = getDifferentPossibilities();
        }

        private Map<Integer, Long> getDifferentPossibilities() {
            Map<Integer, Long> map = new HashMap<>();
            map.put(0, 1L);
            for (Integer integer : integers) {
                map.put(integer, checkPossibilities(map, integer));
            }
            return map;
        }

        public Long getAllAdapterConnectionsPossibilities() {
            return allAdapterConnectionsPossibilities.get(integers.get(integers.size() - 1));
        }

        private Long checkPossibilities(Map<Integer, Long> map, Integer integer) {
            return map.getOrDefault(integer - 1, 0L) + map.getOrDefault(integer - 2, 0L) + map.getOrDefault(integer - 3, 0L);
        }

        public int getDifferenceOfJoltOne() {
            return differenceOfJoltOne;
        }

        public int getDifferenceOfJoltThree() {
            return differenceOfJoltThree;
        }

        public void checkAdapters() {
            int beginningJolt = 0;
            findNextAdapter(beginningJolt, currJolt);
        }

        private void findNextAdapter(int jolt, int nextJolt) {
            Integer i = integers.indexOf(jolt + nextJolt);

            if (nextJolt == JOLT_THREE && i == -1) {
                throw new RuntimeException("Invalid implementation");
            }
            if (i == -1) {
                findNextAdapter(jolt, ++currJolt);
                return;
            }
            Integer integer = integers.get(i);
            updateDifferenceOfJolt(nextJolt);
            resetCurrJolt();

            if (highestAdapter == integer) {
                differenceOfJoltThree++;
                return;
            }
            findNextAdapter(integer, currJolt);
        }

        private void resetCurrJolt() {
            currJolt = JOLT_ONE;
        }

        private void updateDifferenceOfJolt(int nextJolt) {
            switch (nextJolt) {
                case JOLT_ONE:
                    differenceOfJoltOne++;
                    break;
                case JOLT_THREE:
                    differenceOfJoltThree++;
                    break;
            }
        }
    }
}
