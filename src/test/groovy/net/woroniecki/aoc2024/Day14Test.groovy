package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day14Test extends Specification {

    def "test"() {
        given:
        def input = """
                        p=0,4 v=3,-3
                        p=6,3 v=-1,-3
                        p=10,3 v=-1,2
                        p=2,0 v=2,-1
                        p=0,0 v=1,3
                        p=3,0 v=-2,-2
                        p=7,6 v=-1,-3
                        p=3,0 v=-1,-2
                        p=9,3 v=2,3
                        p=7,3 v=-1,2
                        p=2,4 v=2,-3
                        p=9,5 v=-3,-3
                        """.stripIndent().strip()

        when:
        def day = new Day14(input, 11, 7)

        then:
        day.part1() == 12
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day14.txt")

        expect:
        new Day14(input, 101, 103).part1() == 226179492
        new Day14(input, 101, 103).part2() == 7502
    }

}
