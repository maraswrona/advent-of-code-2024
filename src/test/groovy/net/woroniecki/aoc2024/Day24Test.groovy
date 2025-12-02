package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day24Test extends Specification {

    def "example 1"() {
        given:
        def input = """
                        x00: 1
                        x01: 1
                        x02: 1
                        y00: 0
                        y01: 1
                        y02: 0
                        
                        x00 AND y00 -> z00
                        x01 XOR y01 -> z01
                        x02 OR y02 -> z02
                        """.stripIndent().strip()

        when:
        def day = new Day24(input)

        then:
        day.part1() == 4
    }

    def "example 2"() {
        given:
        def input = """
                        x00: 1
                        x01: 0
                        x02: 1
                        x03: 1
                        x04: 0
                        y00: 1
                        y01: 1
                        y02: 1
                        y03: 1
                        y04: 1
                        
                        ntg XOR fgs -> mjb
                        y02 OR x01 -> tnw
                        kwq OR kpj -> z05
                        x00 OR x03 -> fst
                        tgd XOR rvg -> z01
                        vdt OR tnw -> bfw
                        bfw AND frj -> z10
                        ffh OR nrd -> bqk
                        y00 AND y03 -> djm
                        y03 OR y00 -> psh
                        bqk OR frj -> z08
                        tnw OR fst -> frj
                        gnj AND tgd -> z11
                        bfw XOR mjb -> z00
                        x03 OR x00 -> vdt
                        gnj AND wpb -> z02
                        x04 AND y00 -> kjc
                        djm OR pbm -> qhw
                        nrd AND vdt -> hwm
                        kjc AND fst -> rvg
                        y04 OR y02 -> fgs
                        y01 AND x02 -> pbm
                        ntg OR kjc -> kwq
                        psh XOR fgs -> tgd
                        qhw XOR tgd -> z09
                        pbm OR djm -> kpj
                        x03 XOR y03 -> ffh
                        x00 XOR y04 -> ntg
                        bfw OR bqk -> z06
                        nrd XOR fgs -> wpb
                        frj XOR qhw -> z04
                        bqk OR frj -> z07
                        y03 OR x01 -> nrd
                        hwm AND bqk -> z03
                        tgd XOR rvg -> z12
                        tnw OR pbm -> gnj
                        """.stripIndent().strip()

        when:
        def day = new Day24(input)

        then:
        day.part1() == 2024
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day24.txt")

        when:
        def day = new Day24(input)

        then:
        day.part1() == 42049478636360
        day.part2() == "cph,gws,hgj,nnt,npf,z13,z19,z33"
    }

    def "print operations in order"() {
        given:
        def input = Util.readFileToString("/aoc2024/day24.txt")

        when:
        def day = new Day24(input)

        then:
        long x = 1
        def c = day.circuit

        for (final def op in c.sorted()) {
            if (!op.outVar.startsWith("z")) {
                print "\t"
            }

            def sorted = [op.var1, op.var2].sort()

            def trn = "${c.rn(op.outVar)} = ${c.rn(sorted[0])} ${op.op} ${c.rn(sorted[1])}"
            def org = "${op.outVar} = ${sorted[0]} ${op.op} ${sorted[1]}"
            println "${org}\t\t\t${trn}"
        }
    }

    def "should add some random large numbers"() {
        given:
        def input = Util.readFileToString("/aoc2024/day24.txt")
        def day = new Day24(input)
        def c = day.circuit
        c.swap("gws", "nnt")
        c.swap("z13", "npf")
        c.swap("cph", "z19")
        c.swap("hgj", "z33")

        when:
        c.reset()
        c.clear()
        c.setX(x)
        c.setY(y)

        then:
        c.calculate() == x + y

        where:
        x << [544123235234, 543232134543, 15421314246]
        y << [4783283718943, 128339457813, 12893123257]
    }

    def "should add bit by bit"() {
        given:
        def input = Util.readFileToString("/aoc2024/day24.txt")
        def day = new Day24(input)
        def c = day.circuit
        c.swap("gws", "nnt")
        c.swap("z13", "npf")
        c.swap("cph", "z19")
        c.swap("hgj", "z33")

        when:
        def results = (0L..<c.bits - 1).collect {
            c.clear()
            long x = 1L << it
            c.setX(x)
            c.setY(0)
            println "it {$it} x: ${x} calc: ${c.calculate()}"
            return c.calculate() - x
        }.toList()

        then:
        results.every {
            it == 0
        }

        when:
        results = (0L..<c.bits - 1).collect {
            c.clear()
            long y = 1L << it
            c.setX(0)
            c.setY(y)
            println "it {$it} y: ${y} calc: ${c.calculate()}"
            return c.calculate() - y
        }.toList()

        then:
        results.every {
            it == 0
        }

        when:
        results = (0L..<c.bits - 1).collect {
            c.clear()
            long x = 1L << it
            long y = 1L << it
            c.setX(x)
            c.setY(y)
            println "it {$it} x: ${x} y: ${y} calc: ${c.calculate()}"
            return c.calculate() - y - x
        }.toList()

        then:
        results.every {
            it == 0
        }

        when:
        results = (0L..<c.bits - 1).collect {
            c.clear()
            long x = 1L << it
            long y = 1L << it
            c.setX(x)
            c.setY(y)
            println "it {$it} x: ${x} y: ${y} calc: ${c.calculate()}"
            return c.calculate() - y - x
        }.toList()

        then:
        results.every {
            it == 0
        }
    }

}
