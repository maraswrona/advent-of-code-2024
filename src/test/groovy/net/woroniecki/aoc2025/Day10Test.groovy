package net.woroniecki.aoc2025

import net.woroniecki.Util
import spock.lang.Specification

class Day10Test extends Specification {

    def "test"() {
        given:
        def input = """
                        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
                        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
                        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
                        """.stripIndent().strip()

        when:
        def day = new Day10(input)

        then:
        day.part1() == 7
        day.part2() == 33
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2025/day10.txt")

        when:
        def day = new Day10(input)

        then:
        day.part1() == 422
        day.part2() == 0
    }

    def "solver test"() {
        given:
        Day10.Solver slvr = new Day10.Solver()
        def equationsList = List.of(
                new Day10.Equation(2, 'A' as char, 'B' as char)
        )
        Day10.Equations equations = new Day10.Equations(equationsList)


        when:
        def solutions = slvr.solve(equations)

        then:
        solutions.size() == 3
        solutions.any { it.vars == Map.of('A' as char, 0, 'B' as char, 2) }
        solutions.any { it.vars == Map.of('A' as char, 1, 'B' as char, 1) }
        solutions.any { it.vars == Map.of('A' as char, 2, 'B' as char, 0) }
    }

    def "solver test 2"() {
        given:
        Day10.Solver slvr = new Day10.Solver()
        def equationsList = List.of(
                new Day10.Equation(3, 'F' as char, 'E' as char),
                new Day10.Equation(5, 'B' as char, 'F' as char),
                new Day10.Equation(4, 'C' as char, 'D' as char, 'E' as char),
                new Day10.Equation(7, 'A' as char, 'B' as char, 'D' as char)
        )
        Day10.Equations equations = new Day10.Equations(equationsList)


        when:
        def solutions = slvr.solve(equations)

        then:
        solutions.size() == 14
    }

}
