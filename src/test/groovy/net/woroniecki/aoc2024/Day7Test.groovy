package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day7Test extends Specification {

    def "test"() {
        given:
        def input = """
                        190: 10 19
                        3267: 81 40 27
                        83: 17 5
                        156: 15 6
                        7290: 6 8 6 15
                        161011: 16 10 13
                        192: 17 8 14
                        21037: 9 7 18 13
                        292: 11 6 16 20
                        """.stripIndent().strip()

        when:
        def day = new Day7(input)

        then:
        day.part1() == 3749
        day.part2() == 11387
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day7.txt")

        when:
        def day = new Day7(input)

        then:
        day.part1() == 5540634308362
        day.part2() == 472290821152397
    }

}
