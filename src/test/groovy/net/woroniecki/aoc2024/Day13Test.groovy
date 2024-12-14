package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day13Test extends Specification {

    def "example part 1"() {
        given:
        def input = """
                        Button A: X+94, Y+34
                        Button B: X+22, Y+67
                        Prize: X=8400, Y=5400
                        
                        Button A: X+26, Y+66
                        Button B: X+67, Y+21
                        Prize: X=12748, Y=12176
                        
                        Button A: X+17, Y+86
                        Button B: X+84, Y+37
                        Prize: X=7870, Y=6450
                        
                        Button A: X+69, Y+23
                        Button B: X+27, Y+71
                        Prize: X=18641, Y=10279
                        """.stripIndent().strip()

        when:
        def day = new Day13(input)

        then:
        day.part1() == 480
        day.part2() == 875318608908
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day13.txt")

        when:
        def day = new Day13(input)

        then:
        day.part1() == 36870
        day.part2() == 78101482023732
    }

}
