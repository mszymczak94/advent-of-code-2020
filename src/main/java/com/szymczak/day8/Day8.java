package com.szymczak.day8;

import com.szymczak.utils.FileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Day8 {
    private static final String NOP = "nop";
    private static final String JMP = "jmp";
    private static String example = "nop +0\n" +
            "acc +1\n" +
            "jmp +4\n" +
            "acc +3\n" +
            "jmp -3\n" +
            "acc -99\n" +
            "acc +1\n" +
            "jmp -4\n" +
            "acc +6";

    public static void main(String[] args) throws Exception {
        validateExample();
        String[] read = FileReader.read(new File("src/main/java/com/szymczak/day8/day8"));
        System.out.println(getResult(read));
    }

    private static void validateExample() throws Exception {
        int result = getResult(example.split(("\n")));
        if (result != 8) throw new Exception("Invalid implementation result is " + result);
    }

    private static int getResult(String[] lines) {
        List<Instruction> instructions = prepareInstructions(lines);
//        return runApplication(instructions, 0, 0);
        return fixProgram(instructions);
    }

    private static int fixProgram(List<Instruction> instructions) {
        List<Instruction> tempInstructions = new ArrayList<>(instructions);
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = tempInstructions.get(i);
            String instructionsName = instruction.getName();
            if (instructionsName.equals(JMP) == instructionsName.equals(NOP)) {
                continue;
            }

            instruction.swapName();

            ProgramResult programResult = runApplication(tempInstructions, 0, 0);
            if (programResult.isOk()) {
                return programResult.getResult();
            }

            instruction.swapName();
            resetUseInformation(tempInstructions);
        }
        throw new RuntimeException("Invalid implementation");
    }

    private static void resetUseInformation(List<Instruction> tempInstructions) {
        tempInstructions.forEach($ -> $.setUsed(false));
    }

    private static List<Instruction> prepareInstructions(String[] lines) {
        List<Instruction> instructions = new ArrayList<>();
        for (String line : lines) {
            String[] splitLine = line.split(" ");
            instructions.add(new Instruction(splitLine[0], splitLine[1]));
        }
        return instructions;
    }

    private static ProgramResult runApplication(List<Instruction> instructions, int index, int accelerator) {
        if (index == instructions.size()) {
            return new ProgramResult(accelerator, true);
        }
        
        Instruction instruction = instructions.get(index);
        if (instruction.isUsed()) {
            return new ProgramResult(accelerator, false);
        }

        instruction.setUsed(true);
        String instructionName = instruction.getName();

        if (instructionName.equals(NOP)) {
            return runApplication(instructions, ++index, accelerator);
        }

        String action = instruction.getAction();
        if (instructionName.equals(JMP)) {
            index = doOperation(index, action);
            return runApplication(instructions, index, accelerator);
        }

        accelerator = doOperation(accelerator, action);
        return runApplication(instructions, ++index, accelerator);
    }

    private static int doOperation(int i, String action) {
        if (action.startsWith("+")) {
            return i + Integer.parseInt(action.substring(1));
        }
        return i - Integer.parseInt(action.substring(1));
    }

    private static class ProgramResult {
        private final boolean isOk;
        private final int result;

        public ProgramResult(int result, boolean isOk) {
            this.result = result;
            this.isOk = isOk;
        }

        public boolean isOk() {
            return isOk;
        }

        public int getResult() {
            return result;
        }
    }

    private static class Instruction {
        private String name;
        private final String action;
        private boolean isUsed = false;

        private Instruction(String name, String action) {
            this.name = name;
            this.action = action;
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void setUsed(boolean used) {
            isUsed = used;
        }

        public String getName() {
            return name;
        }

        public String getAction() {
            return action;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void swapName() {
            if (this.name.equals(JMP)) {
                this.name = NOP;
            } else {
                this.name = JMP;
            }
        }
    }
}
