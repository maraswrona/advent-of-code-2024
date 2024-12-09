package net.woroniecki.aoc2024


import spock.lang.Specification

class Day9Test extends Specification {

    static {
        List<Integer>.metaClass.encode1 = { -> delegate.collect { it -> it == -1 ? "." : it.toString() }.join("") }
        List<Day9.File>.metaClass.encode2 = { ->
            delegate.collect { it -> it.id == -1 ? "." * it.size : it.id.toString() * it.size }.join("") }
    }

    def "test"() {
        given:
        def input = """
                        2333133121414131402
                        """.stripIndent().strip()

        when:
        def day = new Day9(input)

        then:
        day.unpackBlocks().encode1() == "00...111...2...333.44.5555.6666.777.888899";
        day.defragBlocks().encode1() == "0099811188827773336446555566.............."
        day.part1() == 1928

        and:
        day.unpackFiles().encode2() == "00...111...2...333.44.5555.6666.777.888899";
        day.defragFiles().encode2() == "00992111777.44.333....5555.6666.....8888.."
        day.part2() == 2858
    }

    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day9.txt")

        when:
        def day = new Day9(input)

        then:
        day.part1() == 6331212425418
        day.part2() == 6363268339304
    }

}
