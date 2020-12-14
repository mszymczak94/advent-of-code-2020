package com.szymczak.day3;

public class PositionValidator {
    private int range;

    public PositionValidator(int range) {
        this.range = range;
    }

    public int getValidPositionOnTheWorld(int x) {
        return x < range ? x : x % range;
    }


}
