package net.woroniecki.aoc2025

import lombok.extern.slf4j.Slf4j
import net.woroniecki.Util
import spock.lang.Specification

@Slf4j
class Day11Test extends Specification {

    def "test1"() {
        given:
        def input = """
                        aaa: you hhh
                        you: bbb ccc
                        bbb: ddd eee
                        ccc: ddd eee fff
                        ddd: ggg
                        eee: out
                        fff: out
                        ggg: out
                        hhh: ccc fff iii
                        iii: out
                        """.stripIndent().strip()

        when:
        def day = new Day11(input)

        then:
        day.part1("you", "out") == 5
    }

    def "test2"() {
        given:
        def input = """
                        svr: aaa bbb
                        aaa: fft
                        fft: ccc
                        bbb: tty
                        tty: ccc
                        ccc: ddd eee
                        ddd: hub
                        hub: fff
                        eee: dac
                        dac: fff
                        fff: ggg hhh
                        ggg: out
                        hhh: out
                        """.stripIndent().strip()

        when:
        def day = new Day11(input)

        then:
        day.part1("svr", "out") == 8
        day.part2("svr", "out") == 2
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day11.txt")

        when:
        def day = new Day11(input)

        then:
        day.part1("you", "out") == 688
        day.part2("svr", "out") == 293263494406608
    }
}
