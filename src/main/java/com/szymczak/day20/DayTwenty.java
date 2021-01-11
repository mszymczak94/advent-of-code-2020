package com.szymczak.day20;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayTwenty {

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day20/day19"));
        List<Image> images = prepareData(read);
        Image[][] foundImage = searchSquare((int) Math.sqrt(images.size()), images);
        long squareArrangement = getSum(foundImage);
        System.out.println(squareArrangement);
        UtilsDay20.getStringFromImage(foundImage);
        System.out.println(hashesWithoutMonsters(UtilsDay20.getStringFromImage(foundImage)));

    }

    private static void validateExample() throws Exception {
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day20/example"));
        List<Image> images = prepareData(read);
        Image[][] foundImage = searchSquare((int) Math.sqrt(images.size()), images);
        long squareArrangement = getSum(foundImage);
        if (squareArrangement != 20899048083289L) throw new Exception("Invalid implementation " + squareArrangement);
        long result = hashesWithoutMonsters(UtilsDay20.getStringFromImage(foundImage));

        if (result != 273) throw new Exception("Invalid implementation " + squareArrangement);
    }

    private static long hashesWithoutMonsters(String stringFromImage) {
        char[][] chars = Arrays.stream(stringFromImage.split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);

        ImageWithMonster imageWithMonster = new ImageWithMonster(chars);
        imageWithMonster.currentUsed = imageWithMonster.image;
        char[][] result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.swapSides(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }

        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.upsideDown(imageWithMonster.image);
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        imageWithMonster.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(imageWithMonster.currentUsed));
        result = getImageWithMonsters(imageWithMonster.currentUsed);
        if (result != null) {
            return getResult(result);
        }
        throw new RuntimeException("Implementation not working!");
    }

    private static char[][] getImageWithMonsters(char[][] currentUsed) {
        boolean isAnyMonster = false;
        //                  #
        //#    ##    ##    ###
        // #  #  #  #  #  #
        for (int i = 0; i < currentUsed.length; i++) { // due to monster size
            for (int j = 0; j < currentUsed.length; j++) { // due to monster size
                char c = currentUsed[i][j];
                if (c == '.' || c == 'O') {
                    continue;
                }
                if (monsterAdd(i, j, currentUsed) && !isAnyMonster) {
                    isAnyMonster = true;
                }
            }
        }
        return isAnyMonster ? currentUsed : null;
    }

    private static boolean monsterAdd(int i, int j, char[][] currentUsed) {
        for (Point point : ImageWithMonster.INDEXES_TO_CHECK) {
            int tempX = point.y + i;
            int tempY = point.x + j;
            if (tempX >= currentUsed.length || tempY >= currentUsed.length || currentUsed[tempX][tempY] != '#') {
                return false;
            }
        }
        Arrays.stream(ImageWithMonster.INDEXES_TO_CHECK)
                .forEach($ -> currentUsed[$.y + i][$.x + j] = 'O');
        return true;
    }

    private static long getResult(char[][] result) {
        UtilsDay20.print(result);
        long counter = 0;
        for (char[] chars : result) {
            for (char aChar : chars) {
                if (aChar == '#') {
                    counter++;
                }
            }
        }
        return counter;
    }

    private static long getSum(Image[][] imagesArr) {
        int size = imagesArr.length;
        return imagesArr[0][0].tile * imagesArr[0][size - 1].tile * imagesArr[size - 1][0].tile * imagesArr[size - 1][size - 1].tile;
    }

    private static Image[][] searchSquare(int size, List<Image> images) {
        boolean isFound;
        for (Image image : images) {
            Image[][] arr = new Image[size][size];
            List<Image> tempImages = new ArrayList<>(images);
            tempImages.remove(image);

            image.currentUsed = image.image;
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(image.image);
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(image.image);
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(image.image);
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.swapSides(image.image);
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.upsideDown(image.image);
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(image.currentUsed));
            arr = new Image[size][size];
            arr[0][0] = image;
            isFound = isValid(arr, tempImages);
            if (isFound) {
                return arr;
            }

            image.currentUsed = null;
        }
        throw new RuntimeException("Bad, Try again!");
    }

    private static boolean isValid(Image[][] imageArr, List<Image> tempImages) {
        List<Image> images = new ArrayList<>(tempImages);
        for (int i = 0; i < imageArr.length; i++) {
            for (int j = 0; j < imageArr.length; j++) {
                if (imageArr[i][j] != null) {
                    continue;
                }
                boolean isArrValid = false;
                for (Image tempImage : images) {
                    imageArr[i][j] = tempImage;
                    isArrValid = isArrValid(imageArr, i, j, tempImage);
                    if (isArrValid) {
                        break;
                    }
                }
                if (!isArrValid) {
                    return false;
                }
                images.remove(imageArr[i][j]);
            }
        }
        return true;
    }

    private static boolean isArrValid(Image[][] imageArr, int i, int j, Image tempImage) {
        tempImage.currentUsed = tempImage.image;
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }

        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }

        tempImage.currentUsed = UtilsDay20.upsideDown(tempImage.image);
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.swapSides(tempImage.image);
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = UtilsDay20.rotate90(UtilsDay20.copy(tempImage.currentUsed));
        if (leftOk(imageArr, i, j) && rightOk(imageArr, i, j) && topOk(imageArr, i, j) && bottomOk(imageArr, i, j)) {
            return true;
        }
        tempImage.currentUsed = null;

        return false;
    }

    private static boolean bottomOk(Image[][] imageArr, int i, int j) {
        if (i + 1 >= imageArr.length) {
            return true;
        }
        Image image = imageArr[i][j];
        Image bottomImage = imageArr[i + 1][j];
        return bottomImage == null || image.suitToBottom(bottomImage);
    }

    private static boolean topOk(Image[][] imageArr, int i, int j) {
        if (i - 1 < 0) {
            return true;
        }
        Image image = imageArr[i][j];
        Image topImage = imageArr[i - 1][j];
        return topImage == null || image.suitToTop(topImage);
    }

    private static boolean rightOk(Image[][] imageArr, int i, int j) {
        if (j + 1 >= imageArr.length) {
            return true;
        }
        Image image = imageArr[i][j];
        Image rightImage = imageArr[i][j + 1];
        return rightImage == null || image.suitToRight(rightImage);
    }

    private static boolean leftOk(Image[][] imageArr, int i, int j) {
        if ((j - 1) < 0) {
            return true;
        }
        Image image = imageArr[i][j];
        Image leftImage = imageArr[i][j - 1];
        return leftImage == null || image.suitToLeft(leftImage);
    }

    private static List<Image> prepareData(String[] example) {
        long tile = -1;
        StringBuilder builder = new StringBuilder();
        List<Image> images = new ArrayList<>();
        for (String s : example) {
            if (s.isEmpty()) {
                char[][] chars = Arrays.stream(builder.toString().split("\n"))
                        .map(String::toCharArray).toArray(char[][]::new);
                images.add(new Image(tile, chars));
                tile = -1;
                builder = new StringBuilder();
                continue;
            }
            if (tile == -1 && s.startsWith("Tile")) {
                String value = s.replaceAll("[Tile ]|[:]", "");
                tile = Long.parseLong(value);
                continue;
            }
            builder.append(s).append("\n");
        }
        char[][] chars = Arrays.stream(builder.toString().split("\n"))
                .map(String::toCharArray).toArray(char[][]::new);
        images.add(new Image(tile, chars));
        return images;
    }

    private static class Image {
        private final long tile;
        private final char[][] image;
        private char[][] currentUsed;

        public Image(long tile, char[][] image) {
            this.tile = tile;
            this.image = image;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "tile=" + tile +
                    ", image='" + getImageString(image) + '\'' +
                    '}';
        }

        private String getImageString(char[][] image) {
            StringBuilder builder = new StringBuilder();
            builder.append('\n');
            for (char[] chars : image) {
                for (char aChar : chars) {
                    builder.append(aChar);
                }
                builder.append('\n');
            }
            return builder.toString();
        }


        public boolean suitToLeft(Image leftImage) {
            int length = this.currentUsed.length - 1;
            for (int i = 0; i < this.currentUsed.length; i++) {
                if (this.currentUsed[i][0] != leftImage.currentUsed[i][length]) {
                    return false;
                }
            }

            return true;
        }

        public boolean suitToRight(Image rightImage) {
            int length = this.currentUsed.length - 1;
            for (int i = 0; i < this.currentUsed.length; i++) {
                if (this.currentUsed[i][length] != rightImage.currentUsed[i][0]) {
                    return false;
                }
            }
            return true;
        }

        public boolean suitToTop(Image topImage) {
            int length = this.currentUsed[0].length - 1;
            for (int i = 0; i < this.currentUsed.length; i++) {
                if (this.currentUsed[0][i] != topImage.currentUsed[length][i]) {
                    return false;
                }
            }
            return true;
        }

        public boolean suitToBottom(Image bottomImage) {
            int length = this.currentUsed[0].length - 1;
            for (int i = 0; i < this.currentUsed.length; i++) {
                if (this.currentUsed[length][i] != bottomImage.currentUsed[0][i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class Point {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class ImageWithMonster {
        private final char[][] image;
        private char[][] currentUsed = null;
        private final static Point[] INDEXES_TO_CHECK = {
                new Point(0, 0),
                new Point(1, 1),
                new Point(4, 1),
                new Point(5, 0),
                new Point(6, 0),
                new Point(7, 1),
                new Point(10, 1),
                new Point(11, 0),
                new Point(12, 0),
                new Point(13, 1),
                new Point(16, 1),
                new Point(17, 0),
                new Point(18, 0),
                new Point(18, -1),
                new Point(19, 0),
        };

        private ImageWithMonster(char[][] image) {
            this.image = image;
        }

    }

    private static class UtilsDay20 {

        private static char[][] swapSides(char[][] image) {
            char[][] clone = copy(image);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < image.length; i++) {
                char[] reversed = builder.append(clone[i]).reverse().toString().toCharArray();
                builder = new StringBuilder();
                clone[i] = reversed;
            }
            return clone;
        }

        private static char[][] upsideDown(char[][] image) {
            char[][] clone = copy(image);
            for (int i = image.length; 0 < i; i--) {
                clone[image.length - i] = image[i - 1];
            }
            return clone;
        }

        private static char[][] rotate90(char[][] image) {
            char[][] clone = copy(image);
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image.length; j++) {
                    clone[i][j] = image[image.length - 1 - j][i];
                }
            }
            return clone;
        }

        private static char[][] copy(char[][] image) {
            char[][] copy = new char[image.length][];
            for (int i = 0; i < image.length; i++) {
                copy[i] = Arrays.copyOf(image[i], image[i].length);
            }
            return copy;
        }

        public static String getStringFromImage(Image[][] foundImage) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < foundImage.length; i++) {
                int currIndex = 1;
                for (int j = 0; j < foundImage.length; ) {
                    Image image = foundImage[j][i];
                    for (int k = 1; k < image.currentUsed.length - 1; k++) {
                        builder.append(image.currentUsed[k][currIndex]);
                    }
                    j++;
                    if (j == foundImage.length) {
                        j = 0;
                        currIndex++;
                        builder.append('\n');
                    }

                    if (currIndex == image.currentUsed.length - 1) {
                        break;
                    }

                }
            }
            return builder.toString();
        }


        public static void print(char[][] arr) {
            StringBuilder builder = new StringBuilder();
            for (char[] chars : arr) {
                for (char aChar : chars) {
                    builder.append(aChar);
                }
                builder.append('\n');
            }
            System.out.println(builder.toString());
        }
    }
}
