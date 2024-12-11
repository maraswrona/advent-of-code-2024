package net.woroniecki.aoc2024

import net.woroniecki.Util
import spock.lang.Specification

class Day3Test extends Specification {

    def "test1"() {
        given:
        def input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

        expect:
        new Day3(input).part1() == 161
    }

    def "test2"() {
        given:
        def input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

        expect:
        new Day3(input).part2() == 48
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day3.txt")

        when:
        def day3 = new Day3(input)

        then:
        day3.part1() == 157621318
        day3.part2() == 79845780
    }

}
