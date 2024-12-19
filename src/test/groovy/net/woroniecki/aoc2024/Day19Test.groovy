package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day19Test extends Specification {

    def "test"() {
        given:
        def input = """
                        r, wr, b, g, bwu, rb, gb, br

                        brwrr
                        bggr
                        gbbr
                        rrbgbr
                        ubwu
                        bwurrg
                        brgr
                        bbrgwb
                        """.stripIndent().strip()

        when:
        def day = new Day19(input)

        then:
        day.part1() == 6
        day.part2() == 16
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day19.txt")

        when:
        def day = new Day19(input)

        then:
        day.part1() == 330
        //day.part2() == 0
    }

}
