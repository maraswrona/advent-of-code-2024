package net.woroniecki.aoc2024


import net.woroniecki.Util
import spock.lang.Specification

class Day15Test extends Specification {

    def "example large full part"() {
        given:
        def input = """
                        ##########
                        #..O..O.O#
                        #......O.#
                        #.OO..O.O#
                        #..O@..O.#
                        #O#..O...#
                        #O..O..O.#
                        #.OO.O.OO#
                        #....O...#
                        ##########
                        
                        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                        """.stripIndent().strip()

        expect:
        new Day15(input, false).part1() == 10092
        new Day15(input, true).part2() == 9021
    }

    def "example large only last stage and gps part 1"() {
        given:
        def input = """
                        ##########
                        #.O.O.OOO#
                        #........#
                        #OO......#
                        #OO@.....#
                        #O#.....O#
                        #O.....OO#
                        #O.....OO#
                        #OO....OO#
                        ##########
                        
                        >
                        """.stripIndent().strip()

        expect:
        new Day15(input, false).calculateGPS() == 10092
    }

    def "example small full part 1"() {
        given:
        def input = """
                        ########
                        #..O.O.#
                        ##@.O..#
                        #...O..#
                        #.#.O..#
                        #...O..#
                        #......#
                        ########
                        
                        <^^>>>vv<v>>v<<
                        """.stripIndent().strip()

        when:
        def day = new Day15(input, false)

        then:
        day.part1() == 2028
    }

    def "example small only last stage and gps"() {
        given:
        def input = """
                        ########
                        #....OO#
                        ##.....#
                        #.....O#
                        #.#O@..#
                        #...O..#
                        #...O..#
                        ########
                        
                        >
                        """.stripIndent().strip()

        when:
        def day = new Day15(input, false)

        then:
        day.calculateGPS() == 2028
    }

    def "example 2"() {
        given:
        def input = """
                        ########
                        #...O..#
                        #......#
                        ########
                        
                        >
                        """.stripIndent().strip()

        when:
        def day = new Day15(input, false)

        then:
        day.calculateGPS() == 104
    }


    def "puzzle"() {
        given:
        def input = Util.readFileToString("/aoc2024/day15.txt")

        expect:
        new Day15(input, false).part1() == 1430536
        new Day15(input, true).part2() == 0
    }

}
