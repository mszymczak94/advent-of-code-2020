package com.szymczak.day14;


import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class DayFourteen {
    private static String example = "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\n" +
            "mem[8] = 11\n" +
            "mem[7] = 101\n" +
            "mem[8] = 0";

    private static String example2 = "mask = 000000000000000000000000000000X1001X\n" +
            "mem[42] = 100\n" +
            "mask = 00000000000000000000000000000000X0XX\n" +
            "mem[26] = 1";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day14/day14"));
        Program result = getResult(read);
        System.out.println(result.getSumOfValuesInMemory());
        result.memory = new HashMap<>();
        result.readVerTwo(read);
        System.out.println(result.getSumOfValuesInMemory());
    }

    private static void validateExample() throws Exception {
        Program program = getResult(example.split("\n"));
        if (program.getSumOfValuesInMemory() != 165) {
            throw new Exception("Invalid implementation result is " + program.getSumOfValuesInMemory());
        }
        program.memory = new HashMap<>();
        program.readVerTwo(example2.split("\n"));
        if (program.getSumOfValuesInMemory() != 208) {
            throw new Exception("Invalid implementation result is " + program.getSumOfValuesInMemory());
        }
    }

    private static Program getResult(String[] lines) {
        String mask = lines[0];
        Program program = new Program(mask);
        program.read(lines);
        return program;
    }

    private static class Mask {
        private final String value;
        private final int index;

        private Mask(String value, int index) {
            this.value = value;
            this.index = index;
        }

        public String getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }

    private static class Program {
        private Mask[] masks;
        private Mask[] masksPartOne;
        private String formatter;
        private HashMap<Long, Long> memory = new HashMap<>();

        public Program(String masks) {
        }

        public void read(String[] lines) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("mask")) {
                    setCurrMask(lines[i]);
                    continue;
                }
                int[] info = Arrays.stream(lines[i]
                        .replaceAll("mem|[\\[|\\]|\\s+]", "")
                        .split("="))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                long destination = info[0];
                long valueWithMask = getValueWithMask(info[1]);
                memory.put(destination, valueWithMask);
            }
        }

        public void readVerTwo(String[] lines) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("mask")) {
                    setCurrMask(lines[i]);
                    continue;
                }

                long[] info = Arrays.stream(lines[i]
                        .replaceAll("mem|[\\[|\\]|\\s+]", "")
                        .split("="))
                        .mapToLong(Long::parseLong)
                        .toArray();

                long destination = info[0];
                updateMemoryPossibilities(destination, info[1]);
            }
        }

        private void updateMemoryPossibilities(long destination, long memoryValue) {
            char[] binaryAsCharArray = String.format(formatter, Long.toBinaryString(destination))
                    .replace(" ", "0").toCharArray();
            for (Mask mask : masks) {
                binaryAsCharArray[mask.getIndex()] = mask.getValue().charAt(0);
            }
            Set<Long> possibilities = getAllPossibilities(binaryAsCharArray, new HashSet<>(), Arrays.stream(masks).filter($ -> $.getValue().equals("X")).toArray(Mask[]::new));
            possibilities.stream().forEach($ -> memory.put($, memoryValue));
        }

        private Set<Long> getAllPossibilities(char[] binaryAsCharArray, Set<Long> possibilities, Mask[] masks) {
            for (Mask m : masks) {
                binaryAsCharArray[m.getIndex()] = m.getValue().charAt(0);
            }

            brute(possibilities, binaryAsCharArray, masks, 0);
            return possibilities;
        }

        private void brute(Set<Long> possibilities, char[] tempBinaryASCharArray, Mask[] masks, int depth) {
            if (depth == masks.length) {
                possibilities.add(Long.parseLong(String.valueOf(tempBinaryASCharArray), 2));
                return;
            }

            for (int i = depth; i < masks.length; i++) {
                Mask mask = masks[i];
                for (int j = 0; j <= 1; j++) {
                    tempBinaryASCharArray[mask.getIndex()] = String.valueOf(j).charAt(0);
                    brute(possibilities, tempBinaryASCharArray, masks, i + 1);
                }
            }
        }

        private void setCurrMask(String line) {
            String[] s = line.replaceAll("mask = ", "").split("");
            this.masks = IntStream.range(0, s.length)
                    .filter($ -> !s[$].equals("0"))
                    .mapToObj(i -> new Mask(s[i], i))
                    .toArray(Mask[]::new);
            this.masksPartOne = IntStream.range(0, s.length)
                    .filter($ -> !s[$].equals("X"))
                    .mapToObj(i -> new Mask(s[i], i))
                    .toArray(Mask[]::new);
            this.formatter = "%" + s.length + "s";
        }

        private long getValueWithMask(int i) {
            char[] binaryAsCharArray = String.format(formatter, Long.toBinaryString(i)).replace(" ", "0").toCharArray();
            for (Mask mask : masksPartOne) {
                char value = mask.getValue().charAt(0);
                int index = mask.getIndex();
                binaryAsCharArray[index] = value;
            }
            return Long.parseLong(String.valueOf(binaryAsCharArray), 2);
        }

        public long getSumOfValuesInMemory() {
            return memory.values().stream().reduce(0L, Long::sum);
        }
    }
}
