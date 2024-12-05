package net.woroniecki.aoc2024

import spock.lang.Specification

class DayTemplateTest extends Specification {

    def "test"() {
        given:
        def input = """
                        
                        """.stripIndent().strip()

        when:
        def day = new DayTemplate(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day.txt")

        when:
        def day = new DayTemplate(input)

        then:
        day.part1() == 0
        day.part2() == 0
    }

}
