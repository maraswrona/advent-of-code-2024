package net.woroniecki.aoc2024


import spock.lang.Specification

class Day9Test extends Specification {

    static  {
       List<Integer>.metaClass.encode = {-> delegate.collect { it -> it == -1 ? "." : it.toString() }.join("") }
    }

    def "test"() {
        given:
        def input = """
                        2333133121414131402
                        """.stripIndent().strip()

        when:
        def day = new Day9(input)

        then:
        day.files.encode() == "00...111...2...333.44.5555.6666.777.888899";

        when:
        day.defrag()

        then:
        day.files.encode() =="0099811188827773336446555566.............."

        and:
        day.part1() == 1928
        day.part2() == 0
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day9.txt")

        when:
        def day = new Day9(input)

        then:
        day.part1() == 6331212425418
        day.part2() == 0
    }

}
