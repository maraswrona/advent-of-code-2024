package net.woroniecki.aoc2024;

import lombok.Setter;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


public class Day17 {

    final long A, B, C;
    final List<Integer> program;

    public Day17(String input) {
        Pattern pattern = Pattern.compile("Register A: (?<A>\\d+)[\\n\\r]{1,2}Register B: (?<B>\\d+)[\\n\\r]{1,2}Register C: (?<C>\\d+)[\\n\\r]{1,6}Program: (?<P>(\\d,?)+)");
        Matcher matcher = pattern.matcher(input);
        matcher.find();
        this.A = Long.parseLong(matcher.group("A").trim());
        this.B = Long.parseLong(matcher.group("B").trim());
        this.C = Long.parseLong(matcher.group("C").trim());
        this.program = Arrays.stream(matcher.group("P").trim().split(",")).map(Integer::parseInt).toList();
    }

    public String part1() {
        Computer cmp = new Computer(A, B, C);
        cmp.setProgram(program);
        cmp.execute();
        return StreamEx.of(cmp.output).joining(",");
    }

    public long part2() {
        int[] program = {2, 4, 1, 1, 7, 5, 0, 3, 4, 3, 1, 6, 5, 5, 3, 0};

        Set<Long> candidates = LongStream.range(0, 10)
                .filter(a -> fastPart2Calculation(a) == 0)
                .boxed()
                .collect(Collectors.toSet());

        for (int i = program.length - 2; i >= 0; i--) {
            int desiredOutput = program[i];
            candidates = candidates.stream()
                    .map(c -> findCandidates(c, desiredOutput))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

        return candidates.stream().mapToLong(i -> i).min().getAsLong();

    }

    public Set<Long> findCandidates(long a, int desiredOutput) {
        Set<Long> candidates = new HashSet<>();
        for (int j = 0; j < 8; j++) {
            long candidate = ((a << 3) | j);
            // Computer cmp = new Computer(candidate, 0, 0);
            // cmp.setProgram(Arrays.asList(2, 4, 1, 1, 7, 5, 0, 3, 4, 3, 1, 6, 5, 5, 3, 0));
            // cmp.execute();
            // Integer first = cmp.output.getFirst();

            int output = fastPart2Calculation(candidate);
            if (output == desiredOutput) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    private int fastPart2Calculation(long a) {
        long b = a % 8;
        b = b ^ 1;
        long c = a / (1L << b);
        a = a / (1 << 3); // a / 8
        b = b ^ c;
        b = b ^ 6;
        return (int) (b % 8);
    }

    public static class Computer {

        long A, B, C;

        Computer(long A, long B, long C) {
            this.A = A;
            this.B = B;
            this.C = C;
        }

        int pointer = 0;

        @Setter
        List<Integer> program;

        List<Integer> output = new ArrayList<>();

        boolean step() {
            try {
                Instruction instruction = readInstruction();
                switch (instruction) {
                    case adv -> adv(readComboOperand());
                    case bxl -> bxl(readLiteralOperand());
                    case bst -> bst(readComboOperand());
                    case jnz -> jnz();
                    case bxc -> bxc(readLiteralOperand());
                    case out -> out(readComboOperand());
                    case bdv -> bdv(readComboOperand());
                    case cdv -> cdv(readComboOperand());
                    default -> throw new IllegalStateException("Unexpected value: " + instruction);
                }
            } catch (HaltException e) {
                return false;
            }
            return pointer < program.size();
        }

        void execute() {
            try {
                while (pointer < program.size()) {
                    Instruction instruction = readInstruction();

                    switch (instruction) {
                        case adv -> adv(readComboOperand());
                        case bxl -> bxl(readLiteralOperand());
                        case bst -> bst(readComboOperand());
                        case jnz -> jnz();
                        case bxc -> bxc(readLiteralOperand());
                        case out -> out(readComboOperand());
                        case bdv -> bdv(readComboOperand());
                        case cdv -> cdv(readComboOperand());
                        default -> throw new IllegalStateException("Unexpected value: " + instruction);
                    }
                }
            } catch (HaltException ignored) {
            }
        }

        void checkPointer() {
            if (pointer >= program.size()) {
                throw new HaltException();
            }
        }

        Instruction readInstruction() {
            checkPointer();
            int opcode = program.get(pointer);
            Instruction instr = Instruction.from(opcode);
            pointer++;
            return instr;
        }

        int readLiteralOperand() {
            checkPointer();
            int literal = program.get(pointer);
            pointer++;
            return literal;
        }

        long readComboOperand() {
            checkPointer();
            int combo = program.get(pointer);
            pointer++;
            return switch (combo) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                case 4 -> A;
                case 5 -> B;
                case 6 -> C;
                case 7 -> throw new IllegalStateException("Reserved combo 7");
                default -> throw new IllegalStateException("Unexpected value: " + combo);
            };
        }

        void adv(long operand) {
            A = A / (1L << operand); // 2 ^ operand
        }

        void bxl(long operand) {
            B = B ^ operand;
        }

        void bst(long operand) {
            B = operand % 8;
        }

        void jnz() {
            if (A != 0) {
                pointer = readLiteralOperand();
            }
        }

        void bxc(long operand) {
            B = B ^ C;
        }

        void out(long operand) {
            output.add((int) (operand % 8));
        }

        void bdv(long operand) {
            B = A / (1L << operand); // 2 ^ operand
        }

        void cdv(long operand) {
            C = A / (1L << operand); // 2 ^ operand
        }

        enum Instruction {
            /*
            The adv instruction (opcode 0) performs division.
            The numerator is the value in the A register.
            The denominator is found by raising 2 to the power of the instruction's combo operand.
            (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
            The result of the division operation is truncated to an integer and then written to the A register.
             */
            adv(0),

            /*
            The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
             */
            bxl(1),

            /*
            The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
             */
            bst(2),

            /*
            The jnz instruction (opcode 3) does nothing if the A register is 0.
            However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand;
            if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
             */
            jnz(3),

            /*
            The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B.
            (For legacy reasons, this instruction reads an operand but ignores it.)
             */
            bxc(4),

            /*
            The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value. (If a program outputs multiple values, they are separated by commas.)
             */
            out(5),

            /*
            The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register. (The numerator is still read from the A register.)
             */
            bdv(6),

            /*
            The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
             */
            cdv(7);

            int opcode;

            Instruction(int opcode) {
                this.opcode = opcode;
            }

            static Instruction from(int opcode) {
                return map.get(opcode);
            }

            static Map<Integer, Instruction> map = new HashMap<>();

            static {
                for (Instruction instruction : Instruction.values()) {
                    map.put(instruction.opcode, instruction);
                }
            }
        }

        class HaltException extends RuntimeException {

        }
    }

}
