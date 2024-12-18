package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day17Test extends Specification {

    def "example part 1"() {
        given:
        def input = """
                        Register A: 729
                        Register B: 0
                        Register C: 0
                        
                        Program: 0,1,5,4,3,0
                        """.stripIndent().strip()

        expect:
        new Day17(input).part1() == "4,6,3,5,6,3,5,2,1,0"
    }

    def "example part 1 repeats"() {
        given:
        def input = """
                        Register A: 117440
                        Register B: 0
                        Register C: 0
                        
                        Program: 0,3,5,4,3,0
                        """.stripIndent().strip()

        expect:
        new Day17(input).part1() == "0,3,5,4,3,0"
    }

    def "try part 2"() {
        given:
        def input = """
                        Register A: 247839539763386
                        Register B: 0
                        Register C: 0
                        
                        Program: 2,4,1,1,7,5,0,3,4,3,1,6,5,5,3,0
                        """.stripIndent().strip()

        expect:
        new Day17(input).part1() == "2,4,1,1,7,5,0,3,4,3,1,6,5,5,3,0"
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day17.txt")

        when:
        def day = new Day17(input)

        then:
        day.part1() == "2,0,7,3,0,3,1,3,7"
        day.part2() == 247839539763386
    }
}
