package com.szymczak.day15;

import java.util.*;

public class DayFifteen {
    private static String example = "0,3,6";

    public static void main(String[] args) throws Exception {
        validateExample();
        System.out.println(getResult(Arrays.stream("15,12,0,14,3,1".split(",")).mapToInt(Integer::parseInt).toArray(), 2020));
        System.out.println(getResult(Arrays.stream("15,12,0,14,3,1".split(",")).mapToInt(Integer::parseInt).toArray(), 30000000));

    }

    private static void validateExample() throws Exception {
        long result = getResult(Arrays.stream(example.split(",")).mapToInt(Integer::parseInt).toArray(), 2020);
        if (result != 436) throw new Exception("Invalid implementation result is " + result);
        long result2 = getResult(Arrays.stream(example.split(",")).mapToInt(Integer::parseInt).toArray(), 30000000);
        if (result2 != 175594) throw new Exception("Invalid implementation result is " + result2);
    }

    private static long getResult(int[] toArray, final long turnToFind) {
        long turn = 1;
        Map<Long, Turn> turns = new HashMap<>();
        Turn lastTurn = null;
        PrimitiveIterator.OfInt iterator = Arrays.stream(toArray).iterator();

        while (iterator.hasNext()) {
            int number = iterator.next();
            lastTurn = checkWhatNumberIsNext(number, turn, turns);
            turn++;
        }
        assert lastTurn != null;
        if (lastTurn.getSameLastSpokenNumber() == null) {
            Turn finalLastTurn = new Turn(0, turn, null);
            Turn turnf = turns.get(finalLastTurn.number);
            finalLastTurn.setSameLastSpokenNumber(turnf);
            turns.put(finalLastTurn.number, finalLastTurn);
            lastTurn = finalLastTurn;
            turn++;
        }
        while (turn <= turnToFind) {
            lastTurn = checkWhatNumberIsNext(lastTurn.number, turn, turns);

            if (lastTurn.getSameLastSpokenNumber() == null) {
                Turn finalLastTurn = new Turn(0, ++turn, null);
                Turn turnf = turns.get(finalLastTurn.number);
                finalLastTurn.setSameLastSpokenNumber(turnf);
                turns.put(finalLastTurn.number, finalLastTurn);
                lastTurn = finalLastTurn;
            }
            turn++;
        }
        return turns.values().stream().filter($ -> $.turn == turnToFind).findFirst().get().number;
    }

    private static Turn checkWhatNumberIsNext(long number, long t, Map<Long, Turn> turns) {
        Turn lastTurnOpt = turns.get(number);
        if (lastTurnOpt == null) {
            Turn turn = new Turn(number, t, null);
            turns.put(number, turn);
            return turn;
        }
        long newNumber = lastTurnOpt.getSameLastSpokenNumber() == null ? t - lastTurnOpt.turn : lastTurnOpt.turn - lastTurnOpt.getSameLastSpokenNumber().turn;
        Turn nextLastOpt = turns.get(newNumber);
        Turn turn = new Turn(newNumber, t, nextLastOpt);
        turns.put(newNumber, turn);
        return turn;
    }

    private static class Turn {
        private long number;
        private long turn;
        private Turn sameLastSpokenNumber;

        public Turn(long number, long turn, Turn sameLastSpokenNumber) {
            this.number = number;
            this.turn = turn;
            this.sameLastSpokenNumber = sameLastSpokenNumber;
        }

        public void setSameLastSpokenNumber(Turn sameLastSpokenNumber) {
            this.sameLastSpokenNumber = sameLastSpokenNumber;
        }

        public Turn getSameLastSpokenNumber() {
            return sameLastSpokenNumber;
        }

    }
}
