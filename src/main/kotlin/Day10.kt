class Day10 {

    val END_CHAR_MAP = mapOf('{' to '}', '<' to '>', '[' to ']', '(' to ')')

    val POINTS_MAP = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    val POINTS_MAP_PART2 = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)


    fun day10() {
        val fileContent = Day10::class.java.getResource("/day10.txt").readText()
        val lines = fileContent.lines()

        //part1
        println(
            lines.map { getFirstIllegalChar(it) }
                .filterNotNull()
                .map { POINTS_MAP[it]!! }.sum()
        )

        //part2
        val scores = lines.filter {
            getFirstIllegalChar(it) == null
        }.map { getEnding(it) }
            .map { it.toCharArray().map { END_CHAR_MAP[it]!! }.reversed().joinToString(separator = "") }
            .map { calculatePoints(it) }
            .sorted()

        println(scores[scores.size / 2])
    }

    fun calculatePoints(s: String) =s.toCharArray()
        .map { POINTS_MAP_PART2[it]!! }
        .fold(0L) { acc, e ->
            val newAcc = acc * 5
            newAcc + e
        }

    tailrec fun getEnding(s: String): String {
        var st = s
        END_CHAR_MAP.map { "${it.key}${it.value}" }.forEach {
            st = st.replace(it, "")
        }
        if (s == st) {
            return st
        }
        return getEnding(st)
    }

    tailrec fun getFirstIllegalChar(s: String): Char? {
        if (s.isEmpty()) {
            return null
        }
        var st = s
        END_CHAR_MAP.map { "${it.key}${it.value}" }.forEach {
            st = st.replace(it, "")
        }
        if (s == st) {
            return st.toCharArray().firstOrNull() { END_CHAR_MAP.values.contains(it) }
        }
        return getFirstIllegalChar(st)
    }
}