package com.szymczak.day13;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

public class DayThirteen {
    private static String example = "939\n" +
            "7,13,x,x,59,x,31,19";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day13/day13"));
        System.out.println(getResult(read));
        System.out.println(part2());
    }
    //https://github.com/SimonBaars/AdventOfCode-Java/blob/master/src/main/java/com/sbaars/adventofcode/year20/days/Day13.java
    // Chinese remainder theorem is used here.
    public static long part2() throws IOException {
        String[] dayStrings = FileReader.read(new File("src/main/java/com/szymczak/day13/day13"));
        String[] s = dayStrings[1].split(",");
        long[][] nums = range(0, s.length).filter(i -> !s[i].equals("x"))
                .mapToObj(i -> new long[]{parseLong(s[i]), i})
                .toArray(long[][]::new);
        long product = stream(nums).mapToLong(a -> a[0]).reduce((a, b) -> a * b).getAsLong();
        long sum = stream(nums).mapToLong(a -> a[1] * (product / a[0]) * inverseModulo(product / a[0], a[0])).sum();
        return product - sum % product;
    }

    static long inverseModulo(long x, long y) {
        if (x != 0) {
            long modulo = y % x;
            return modulo == 0 ? 1 : y - inverseModulo(modulo, x) * y / x;
        }
        return 0;
    }

    private static void validateExample() throws Exception {
        long result = getResult(example.split("\n"));
        if (result != 295) throw new Exception("Invalid implementation result is " + result);
        BigInteger result2 = getResultPartTwo(example.split("\n"));
        if (!result2.equals(BigInteger.valueOf(1068781)))
            throw new Exception("Invalid implementation result is " + result2);
        BigInteger result3 = getResultPartTwo("\n1789,37,47,1889".split("\n"));
        if (!result3.equals(BigInteger.valueOf(1202161486)))
            throw new Exception("Invalid implementation result is " + result3);
    }

    // Never end for main example.
    private static BigInteger getResultPartTwo(String[] lines) {
        List<ResultTwo> buses = getBusesInfo(lines[1]);
        return findEarliestTimestampWithCombination(buses);
    }

    private static BigInteger findEarliestTimestampWithCombination(List<ResultTwo> buses) {
        ResultTwo resultTwo = buses.stream()
                .filter($ -> $.getT().equals(BigInteger.ZERO))
                .findFirst().orElseThrow(() -> new RuntimeException("Invalid implementation"));
        BigInteger bigInteger = resultTwo.getBusId();
        buses.remove(resultTwo);
        return findFirstCombination(buses, bigInteger);
    }

    private static BigInteger findFirstCombination(List<ResultTwo> buses, BigInteger firstTimestamp) {
        boolean isMatch = buses
                .stream()
                .allMatch($ -> $.isValid(firstTimestamp));

        if (isMatch) {
            return firstTimestamp;
        }

        ResultTwo highestIdBus = buses.stream()
                .max(Comparator.comparing(ResultTwo::getBusId))
                .orElseThrow(() -> new RuntimeException("Invalid implementation"));

        BigInteger highestBusId = highestIdBus.getBusId();
        BigInteger sequence = findFirstPossibleTimestamp(highestBusId, highestIdBus.getT(), firstTimestamp);
        BigInteger currTimestampTemp = highestBusId;
        while (!isMatch) {
            sequence = sequence.add(highestBusId);
            currTimestampTemp = getNextPossibleTimestamp(sequence, firstTimestamp);

            BigInteger finalTimeStamp = currTimestampTemp;
            isMatch = buses
                    .stream()
                    .allMatch($ -> $.isValid(finalTimeStamp));
        }
        return currTimestampTemp;
    }

    private static BigInteger findFirstPossibleTimestamp(BigInteger highestBusId, BigInteger tToFirstTimestamp, BigInteger firstTimestamp) {
        BigInteger addition = highestBusId;
        while (true) {
            highestBusId = highestBusId.add(addition);
            BigInteger subtraction = highestBusId.subtract(tToFirstTimestamp);
            if (subtraction.mod(firstTimestamp).equals(BigInteger.ZERO)) {
                return subtraction.divide(firstTimestamp);
            }
        }
    }

    private static BigInteger getNextPossibleTimestamp(BigInteger sequence, BigInteger firstTimestamp) {
        return sequence.multiply(firstTimestamp);
    }

    private static List<ResultTwo> getBusesInfo(String line) {
        String[] buses = line.split(",");
        List<ResultTwo> busesObjects = new ArrayList<>();
        BigInteger t = BigInteger.ZERO;
        for (String bus : buses) {
            if (!bus.equals("x")) {
                busesObjects.add(new ResultTwo(BigInteger.valueOf(Integer.parseInt(bus)), t));
            }
            t = t.add(BigInteger.ONE);
        }
        return busesObjects;
    }

    private static long getResult(String[] lines) {
        Result result = prepareResultObject(lines);
        return result.howLongHaveToWait() * result.getBusIdToAirport();
    }

    private static Result prepareResultObject(String[] lines) {
        long nearTimestamp = parseLong(lines[0]);
        Set<Integer> ids = stream(lines[1].split(","))
                .filter($ -> !$.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
        return new Result(nearTimestamp, ids);
    }

    public static class ResultTwo {
        private final BigInteger busId;
        private final BigInteger t;

        public ResultTwo(BigInteger busId, BigInteger t) {
            this.busId = busId;
            this.t = t;
        }

        public BigInteger getBusId() {
            return busId;
        }

        public BigInteger getT() {
            return t;
        }

        public boolean isValid(BigInteger timeStamp) {
            return timeStamp.add(t).mod(busId).equals(BigInteger.ZERO);
        }
    }

    public static class Result {
        private final long nearTimestamp;
        private final Set<Integer> busIds;
        private int busId = -1;
        private long busTimeStamp = -1;

        public Result(long nearTimestamp, Set<Integer> busIds) {
            this.nearTimestamp = nearTimestamp;
            this.busIds = busIds;
        }


        private void findBusInSchedule() {
            long nearTimestampTemp = nearTimestamp;
            while (true) {
                long finalNearTimestampTemp = nearTimestampTemp;
                Optional<Integer> first = busIds.stream().filter($ -> finalNearTimestampTemp % $ == 0).findFirst();
                if (first.isPresent()) {
                    busId = first.get();
                    busTimeStamp = finalNearTimestampTemp;
                    return;
                } else {
                    nearTimestampTemp++;
                }
            }
        }

        public long howLongHaveToWait() {
            if (isNotScheduleRead()) {
                findBusInSchedule();
            }
            return busTimeStamp - nearTimestamp;
        }

        private boolean isNotScheduleRead() {
            return busTimeStamp == -1 && busId == -1;
        }

        public int getBusIdToAirport() {
            if (isNotScheduleRead()) {
                findBusInSchedule();
            }
            return busId;
        }
    }
}
