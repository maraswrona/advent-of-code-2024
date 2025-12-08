package net.woroniecki.aoc2025


import net.woroniecki.Util
import spock.lang.Specification

class Day05Test extends Specification {

    def "test"() {
        given:
        def input = """
                        3-5
                        10-14
                        16-20
                        12-18
                        
                        1
                        5
                        8
                        11
                        17
                        32  
                        """.stripIndent().strip()

        when:
        def day = new Day05(input)

        then:
        day.part1() == 3
        day.part2() == 14
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day05.txt")

        when:
        def day = new Day05(input)

        then:
        day.part1() == 758
        day.part2() == 343143696885053
    }

}
