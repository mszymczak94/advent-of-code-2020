package com.szymczak.day12;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.Arrays;

public class DayTwelve {
    private static String example = "F10\n" +
            "N3\n" +
            "F7\n" +
            "R90\n" +
            "F11\n" +
            "S3\n" +
            "L180\n" +
            "F10";

    public static void main(String[] args) throws Exception {
        validateExample(example);
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day12/day12"));
        System.out.println(getDistanceToLocation(read, new ShipWithoutWayPoint()));
        System.out.println(getDistanceToLocation(read,
                new ShipWithWayPoint(0, 0, new WayPoint(1, 10, Direction.E))));
    }

    private static void validateExample(String example) throws Exception {
        int distance = getDistanceToLocation(example.split("\n"), new ShipWithoutWayPoint());
        if (distance != 18) throw new Exception("Invalid implementation result is " + distance);
        int distancePartTwo = getDistanceToLocation(
                example.split("\n"),
                new ShipWithWayPoint(0, 0, new WayPoint(1, 10, Direction.E)));
        if (distancePartTwo != 232) throw new Exception("Invalid implementation result is " + distancePartTwo);
    }

    private static int getDistanceToLocation(String[] lines, Ship ship) {
        ship.sail(lines);
        return ship.getFullDistance();
    }

    private static class ShipWithWayPoint implements Ship {
        private int latitude;
        private int longitude;
        private final WayPoint wayPoint;

        public ShipWithWayPoint(int latitude, int longitude, WayPoint wayPoint) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.wayPoint = wayPoint;
        }

        public void sail(String[] coordinates) {
            for (String coordinate : coordinates) {
                String[] directionWithDistance = coordinate.split("", 2);
                String directionAsString = directionWithDistance[0];
                int valueOfDistanceOrDegree = Integer.parseInt(directionWithDistance[1]);

                if (directionAsString.matches("[FRL]")) {
                    Action action = Action.valueOf(directionAsString);
                    ensureDirectionAndMove(action, valueOfDistanceOrDegree);
                } else {
                    Direction direction = Direction.valueOf(directionAsString);
                    wayPoint.updateWaypoint(direction, valueOfDistanceOrDegree);
                }
            }
        }

        private void ensureDirectionAndMove(Action action, int value) {
            if (action == Action.F) {
                moveShip(value);
                return;
            }
            wayPoint.setMainDirection(Direction.changeDirectionIfNecessity(wayPoint.getMainDirection(), action, value));
        }

        private void moveShip(int value) {
            int currLongitudeUnits = wayPoint.getLongitudeDirection() * value;
            int currLatitudeUnits = wayPoint.getLatitudeDirection() * value;
            if (wayPoint.mainDirection == Direction.N || wayPoint.mainDirection == Direction.S) {
                longitude += wayPoint.getMainDirection()
                        .getSingleCoordinate(currLongitudeUnits);
                latitude += wayPoint.getMainDirection()
                        .getSecondDirection()
                        .getSingleCoordinate(currLatitudeUnits);
            } else {
                longitude += wayPoint.getMainDirection()
                        .getSecondDirection()
                        .getSingleCoordinate(currLongitudeUnits);
                latitude += wayPoint.getMainDirection()
                        .getSingleCoordinate(currLatitudeUnits);
            }
        }

        public int getFullDistance() {
            return Math.abs(latitude) + Math.abs(longitude);
        }
    }

    private static class ShipWithoutWayPoint implements Ship {
        private Direction currentDirection = Direction.E;
        private int latitude = 0;
        private int longitude = 0;


        public int getFullDistance() {
            return Math.abs(latitude) + Math.abs(longitude);
        }

        public void sail(String[] coordinates) {
            for (String coordinate : coordinates) {
                String[] directionWithDistance = coordinate.split("", 2);
                String directionAsString = directionWithDistance[0];
                int valueOfDistanceOrDegree = Integer.parseInt(directionWithDistance[1]);

                if (directionAsString.matches("[FRL]")) {
                    Action action = Action.valueOf(directionAsString);
                    ensureDirectionAndMove(action, valueOfDistanceOrDegree);
                } else {
                    Direction direction = Direction.valueOf(directionAsString);
                    moveShip(direction, direction.getSingleCoordinate(valueOfDistanceOrDegree));
                }
            }
        }

        private void moveShip(Direction direction, int singleCoordinate) {
            if (direction == Direction.N || direction == Direction.S) {
                longitude += singleCoordinate;
            } else {
                latitude += singleCoordinate;
            }
        }

        private void ensureDirectionAndMove(Action action, int value) {
            if (action == Action.F) {
                moveShip(currentDirection, currentDirection.getSingleCoordinate(value));
                return;
            }
            currentDirection = Direction.changeDirectionIfNecessity(currentDirection, action, value);
        }
    }

    private static class WayPoint {
        private int longitudeDirection;
        private int latitudeDirection;
        private Direction mainDirection;

        public WayPoint(int longitudeDirection, int latitudeDirection, Direction mainDirection) {
            this.longitudeDirection = longitudeDirection;
            this.latitudeDirection = latitudeDirection;
            this.mainDirection = mainDirection;
        }

        public Direction getMainDirection() {
            return mainDirection;
        }

        public int getLongitudeDirection() {
            return longitudeDirection;
        }

        public int getLatitudeDirection() {
            return latitudeDirection;
        }

        public void setMainDirection(Direction mainDirection) {
            if (this.mainDirection != mainDirection && this.mainDirection != mainDirection.getOppositeDirection()) {
                swapLonLatValues();
            }
            this.mainDirection = mainDirection;
        }

        private void swapLonLatValues() {
            int temp = longitudeDirection;
            longitudeDirection = latitudeDirection;
            latitudeDirection = temp;
        }

        public void updateWaypoint(Direction direction, int valueOfDistanceOrDegree) {
            if (direction == Direction.N || direction == Direction.S) {
                if (mainDirection.getSecondDirection() != direction && mainDirection != direction) {
                    longitudeDirection -= valueOfDistanceOrDegree;
                } else {
                    longitudeDirection += valueOfDistanceOrDegree;
                }
            } else {
                if (mainDirection.getSecondDirection() != direction && mainDirection != direction) {
                    latitudeDirection -= valueOfDistanceOrDegree;
                } else {
                    latitudeDirection += valueOfDistanceOrDegree;
                }
            }
        }
    }

    private enum Direction {
        N(0, 0) {
            @Override
            public int getSingleCoordinate(int distance) {
                return distance;
            }

            @Override
            public Direction getSecondDirection() {
                return W;
            }

            @Override
            public Direction getOppositeDirection() {
                return S;
            }
        }, E(90, -270) {
            @Override
            public int getSingleCoordinate(int distance) {
                return distance;
            }

            @Override
            public Direction getSecondDirection() {
                return N;
            }

            @Override
            public Direction getOppositeDirection() {
                return W;
            }
        }, S(180, -180) {
            @Override
            public int getSingleCoordinate(int distance) {
                return distance *= -1;
            }

            @Override
            public Direction getSecondDirection() {
                return E;
            }

            @Override
            public Direction getOppositeDirection() {
                return N;
            }
        }, W(270, -90) {
            @Override
            public int getSingleCoordinate(int distance) {
                return distance *= -1;
            }

            @Override
            public Direction getSecondDirection() {
                return S;
            }

            @Override
            public Direction getOppositeDirection() {
                return E;
            }
        };

        private final int positiveDirection;
        private final int negativeDirection;

        Direction(int positiveDirection, int negativeDirection) {
            this.positiveDirection = positiveDirection;
            this.negativeDirection = negativeDirection;
        }

        public static Direction changeDirectionIfNecessity(Direction currentDirection, Action action, int value) {
            value = Action.getCorrectDestination(action, value);
            int foundDirection = (currentDirection.positiveDirection + value) % 360;
            return Arrays.stream(Direction.values())
                    .filter($ -> $.positiveDirection == foundDirection || $.negativeDirection == foundDirection)
                    .findFirst().orElseThrow(() -> new RuntimeException("Invalid implementation"));
        }

        public abstract int getSingleCoordinate(int distance);

        public abstract Direction getSecondDirection();

        public abstract Direction getOppositeDirection();
    }

    private enum Action {
        R, L, F;

        public static int getCorrectDestination(Action action, int value) {
            if (action == L) {
                return value *= -1;
            }
            return value;
        }
    }

    public interface Ship {
        void sail(String[] coordinates);

        int getFullDistance();
    }
}
