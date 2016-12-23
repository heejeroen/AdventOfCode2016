package nl.jeroennijs.adventofcode2016;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class Day23 {
    private static Map<String, Integer> registers = new TreeMap<>();
    private static Pattern instrPattern = Pattern.compile("(cpy|inc|dec|jnz|tgl) +(-?[a-z0-9]+) *(-?[a-z0-9]+)?");

    public static void main(String[] args) throws IOException {
        final List<Instruction> instructions = parseInstructions();

        registers.clear();
        registers.put("a", 7);
        execute(instructions);
        System.out.println("Result of run 1:");
        print(registers);

        registers.clear();
        registers.put("a", 12);
        execute(instructions);
        System.out.println("Result of run 2:");
        print(registers);
    }

    private static List<Instruction> parseInstructions() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("AdventOfCode/src/nl/jeroennijs/adventofcode2016/input/day23.txt"))) {
            return reader.lines().map(Day23::parseLine).collect(Collectors.toList());
        }
    }

    private static Instruction parseLine(String line) {
        final Matcher matcher = instrPattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Error in line: " + line);
        }
        return new Instruction(matcher.group(1), matcher.group(2), matcher.group(3));
    }

    private static void execute(List<Instruction> originalInstructions) {
        final List<Instruction> instructions = new ArrayList<>(originalInstructions);
        int pc = 0;
        while (pc < instructions.size()) {
            Instruction instruction = instructions.get(pc);
            switch (instruction.opCode) {
                case cpy:
                    registers.put(instruction.operand2.register, getValue(instruction.operand1));
                    pc++;
                    break;
                case inc:
                    registers.put(instruction.operand1.register, getRegister(instruction.operand1.register) + 1);
                    pc++;
                    break;
                case dec:
                    registers.put(instruction.operand1.register, getRegister(instruction.operand1.register) - 1);
                    pc++;
                    break;
                case jnz:
                    if (getValue(instruction.operand1) != 0) {
                        pc = pc + getValue(instruction.operand2);
                    } else {
                        pc++;
                    }
                    break;
                case tgl:
                    final int oldInstructionIndex = pc + getValue(instruction.operand1);
                    getToggledInstructionAt(instructions, oldInstructionIndex).ifPresent(i -> instructions.set(oldInstructionIndex, i));
                    pc++;
                    break;
            }
        }
    }

    private static Optional<Instruction> getToggledInstructionAt(List<Instruction> instructions, int index) {
        if (index >= instructions.size()) {
            return Optional.empty();
        }
        final Instruction oldInstruction = instructions.get(index);
        return Optional.of(new Instruction(getToggledOpCode(oldInstruction), oldInstruction.operand1, oldInstruction.operand2));
    }

    private static Instruction.OpCode getToggledOpCode(Instruction oldInstruction) {
        if (oldInstruction.operand2 == null) {
            return oldInstruction.opCode == Instruction.OpCode.inc ? Instruction.OpCode.dec : Instruction.OpCode.inc;
        }
        return oldInstruction.opCode == Instruction.OpCode.jnz ? Instruction.OpCode.cpy : Instruction.OpCode.jnz;
    }

    private static int getValue(Operand operand) {
        return operand.isRegister() ? getRegister(operand.register) : operand.value;
    }

    private static int getRegister(String register) {
        if (!registers.containsKey(register)) {
            registers.put(register, 0);
        }
        return registers.get(register);
    }

    private static void print(Map<String, Integer> registers) {
        for (String register : registers.keySet()) {
            System.out.print(register + ":" + registers.get(register) + " ");
        }
        System.out.println();
    }

    public static class Instruction {
        public enum OpCode {
            cpy, inc, dec, jnz, tgl
        }

        Instruction(String opCode, String operand1, String operand2) {
            this.opCode = Instruction.OpCode.valueOf(opCode);
            this.operand1 = Operand.fromValue(operand1);
            if (operand2 != null) {
                this.operand2 = Operand.fromValue(operand2);
            }
        }

        Instruction(OpCode opCode, Operand operand1, Operand operand2) {
            this.opCode = opCode;
            this.operand1 = operand1;
            this.operand2 = operand2;
        }

        Instruction.OpCode opCode;
        Operand operand1;
        Operand operand2;

        @Override public String toString() {
            return opCode + " " + operand1 + (operand2 != null ? " " + operand2 : "");
        }
    }



    public static class Operand {
        String register;
        int value;

        static Operand fromValue(String value) {
            return isAlpha(value) ? new Operand(value) : new Operand(Integer.valueOf(value));
        }

        private Operand(String register) {
            this.register = register;
        }

        private Operand(Integer value) {
            this.value = value;
        }

        private static boolean isAlpha(String value) {
            for (char c : value.toCharArray()) {
                if (c < 'a' || c > 'z')
                    return false;
            }
            return true;
        }

        boolean isRegister() {
            return register != null;
        }

        @Override public String toString() {
            return isRegister() ? register : String.valueOf(value);
        }
    }
}
