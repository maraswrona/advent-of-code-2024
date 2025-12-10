package net.woroniecki.aoc2025


import net.woroniecki.Util
import spock.lang.Specification

class Day09Test extends Specification {

    def "test"() {
        given:
        def input = """
                        7,1
                        11,1
                        11,7
                        9,7
                        9,5
                        2,5
                        2,3
                        7,3
                        """.stripIndent().strip()

        when:
        def day = new Day09(input)

        then:
        day.part1() == 50
        day.part2() == 24
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day09.txt")

        when:
        def day = new Day09(input)

        then:
        day.part1() == 4749672288
        day.part2() == 0
    }

}
