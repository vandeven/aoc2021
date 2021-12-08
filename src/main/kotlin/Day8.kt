class Day8 {

    val numberToLetters = mapOf(
        9 to "abcdfg".toCharArray().toSortedSet(),
        8 to "abcdefg".toCharArray().toSortedSet(),
        7 to "acf".toCharArray().toSortedSet(),
        6 to "abdefg".toCharArray().toSortedSet(),
        5 to "abdfg".toCharArray().toSortedSet(),
        4 to "bcdf".toCharArray().toSortedSet(),
        3 to "acdfg".toCharArray().toSortedSet(),
        2 to "acdeg".toCharArray().toSortedSet(),
        1 to "cf".toCharArray().toSortedSet(),
        0 to "abcefg".toCharArray().toSortedSet()
    )

    fun day8() {
        val fileContent = Day8::class.java.getResource("/day8.txt").readText()

        val puzzles = fileContent.lines().map {
            it.substringBeforeLast(" |").split(" ") to it.substringAfterLast("| ").split(" ")
        }

        //part1
        println(puzzles.flatMap { it.second }.filter {
            it.length == 4 || it.length == 3 || it.length == 7 || it.length == 2
        }.count())

        //part2
        println(puzzles.map {
            val d = createDictionary(it.first)
            it.second.map { translateWord(it, d) }
                .map { getFromNumberTable(it) }
                .joinToString(separator = "")
                .toInt()
        }.sum())

    }

    fun translateWord(s: String, dictionary: Map<Char, Char>) =
        s.toCharArray()
            .map { translateChar(it, dictionary) }
            .sorted()
            .joinToString(separator = "")

    fun translateChar(c: Char, dictionary: Map<Char, Char>) = dictionary.filterValues { it == c }
        .map { it.key }
        .first()

    fun getFromNumberTable(s: String) =
        numberToLetters.mapValues { it.value.joinToString(separator = "") }
            .filterValues { it == s }
            .map { it.key }
            .first()

    fun createDictionary(values: List<String>): Map<Char, Char> {
        val dictionary = mutableMapOf(
            'a' to ' ',
            'b' to ' ',
            'c' to ' ',
            'd' to ' ',
            'e' to ' ',
            'f' to ' ',
            'g' to ' '
        )
        //Find a
        val one = values.filter { numberToLetters[1]!!.size == it.length }.first()
        val seven = values.filter { numberToLetters[7]!!.size == it.length }.first()
        dictionary['a'] = seven.toCharArray().filterNot { one.contains(it) }.first()

        //find g
        values.filter { it.length > 4 }
            .flatMap { it.toCharArray().toList() }
            .groupBy { it }
            .mapValues { it.value.size }
            .filterValues { it == 7 }
            .filterNot { it.key == dictionary['a']!! }
            .forEach {
                dictionary['g'] = it.key
            }

        //find d
        val foundThree =
            values.filter { numberToLetters[3]!!.size == it.length }.filter { v -> one.all { v.contains(it) } }.first()
        dictionary['d'] = foundThree.toCharArray()
            .filterNot { it == dictionary['a'] }
            .filterNot { it == dictionary['g'] }
            .filterNot { one.contains(it) }
            .first()

        // find b
        val foundNine = values.map { it.toCharArray().toList() }
            .filter { numberToLetters[9]!!.size == it.size }
            .filter { it.contains(dictionary['a']!!) }
            .filter { it.contains(dictionary['g']!!) }
            .filter { it.contains(dictionary['d']!!) }
            .filter { it.contains(one.toCharArray()[0]) }
            .filter { it.contains(one.toCharArray()[1]) }
            .first()
        dictionary['b'] = foundNine.toCharArray()
            .filterNot { it == dictionary['a'] }
            .filterNot { it == dictionary['g'] }
            .filterNot { it == dictionary['d'] }
            .filterNot { one.contains(it) }
            .first()

        //find e
        val foundEight = values.filter { numberToLetters[8]!!.size == it.length }.first()
        dictionary['e'] = foundEight.toCharArray()
            .filterNot { it == dictionary['a'] }
            .filterNot { it == dictionary['g'] }
            .filterNot { it == dictionary['d'] }
            .filterNot { it == dictionary['b'] }
            .filterNot { one.contains(it) }
            .first()

        //find f
        val foundSix = values.filter { numberToLetters[6]!!.size == it.length }
            .map { it.toCharArray() }
            .filter { it.contains(dictionary['a']!!) }
            .filter { it.contains(dictionary['b']!!) }
            .filter { it.contains(dictionary['d']!!) }
            .filter { it.contains(dictionary['e']!!) }
            .filter { it.contains(dictionary['g']!!) }
            .first()
        dictionary['f'] = foundSix
            .filterNot { it == dictionary['a'] }
            .filterNot { it == dictionary['g'] }
            .filterNot { it == dictionary['d'] }
            .filterNot { it == dictionary['e'] }
            .filterNot { it == dictionary['b'] }
            .first()

        dictionary['c'] = one.toCharArray().filterNot { it == dictionary['f'] }.first()
        return dictionary
    }
}