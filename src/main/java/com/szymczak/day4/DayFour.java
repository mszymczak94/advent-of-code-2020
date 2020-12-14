package com.szymczak.day4;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayFour {
    private static String example =
            "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                    "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
                    "\n" +
                    "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                    "hcl:#cfa07d byr:1929\n" +
                    "\n" +
                    "hcl:#ae17e1 iyr:2013\n" +
                    "eyr:2024\n" +
                    "ecl:brn pid:760753108 byr:1931\n" +
                    "hgt:179cm\n" +
                    "\n" +
                    "hcl:#cfa07d eyr:2025 pid:166559648\n" +
                    "iyr:2011 ecl:brn hgt:59in";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day4/day4"));
        System.out.println(getResult(read));
    }

    private static void validateExample() throws Exception {
        String[] passports = example.split("\n");
        int validPassports = getResult(passports);
        if (validPassports != 2) throw new Exception("Invalid implementation valid passports: " + validPassports);
    }

    private static int getResult(String[] lines) {
        List<Passport> validPassports = createValidPassports(lines);
        return validPassports.size();
    }

    private static List<Passport> createValidPassports(String[] lines) {
        List<Passport> validPassports = new ArrayList<>();
        lines = String
                .join(" ", lines)
                .replace("  ", ",")
                .split(",");

        for (String line : lines) {
            Passport currentPassword = new Passport();
            currentPassword.setFields(line);
            if (currentPassword.isValid()) {
                validPassports.add(currentPassword);
            }
        }
        return validPassports;
    }

    public static class Passport {
        private String byr;
        private String iyr;
        private String eyr;
        private String hgt;
        private String hcl;
        private String ecl;
        private String pid;
        private String cid;

        public boolean isValid() {
            return byr != null && iyr != null && hgt != null
                    && hcl != null && ecl != null && pid != null && eyr != null && areFieldsValid();
        }

        private boolean areFieldsValid() {
            return isNumberFieldValid(byr, 1920, 2002) &&
                    isNumberFieldValid(iyr, 2010, 2020) &&
                    isNumberFieldValid(eyr, 2020, 2030) &&
                    isHgtValid() &&
                    isHclValid() &&
                    isEclValid() &&
                    isPidValid();
        }

        private boolean isPidValid() {
            return pid.matches("^[0-9]{9}$");
        }

        private boolean isEclValid() {
            return ecl.matches("^amb$|blu$|brn$|gry$|grn$|hzl$|oth$");
        }

        private boolean isHclValid() {
            return hcl.matches("^#[0-9 a-f]{6}$");
        }

        private boolean isHgtValid() {
            if (hgt.endsWith("cm")) {
                return isNumberFieldValid(hgt.replace("cm", ""), 150, 193);
            }

            if (hgt.endsWith("in")) {
                return isNumberFieldValid(hgt.replace("in", ""), 59, 76);
            }
            return false;
        }

        private boolean isNumberFieldValid(String year, int left, int right) {
            int yearAsInt = -1;
            try {
                yearAsInt = Integer.parseInt(year);
            } catch (NumberFormatException e) {
                return false;
            }
            return left <= yearAsInt && yearAsInt <= right;
        }

        public void setFields(String builder) {
            Map<String, String> fields = Arrays.stream(builder.split(" "))
                    .map($ -> $.split(":"))
                    .collect(Collectors.toMap(k -> k[0], v -> v[1]));

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                try {
                    Field declaredField = Passport.class.getDeclaredField(entry.getKey());
                    declaredField.set(this, entry.getValue());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Field not found or illegal access", e);
                }
            }
        }
    }
}
