class Day10 {

    val END_CHAR_MAP = mapOf('{' to '}', '<' to '>', '[' to ']', '(' to ')')

    val POINTS_MAP = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    fun day10(){
        val fileContent = Day1::class.java.getResource("/day10.txt").readText()
        val lines = fileContent.lines()

        //part1
        println(lines.map {
            getFirstIllegalChar(it)
        }.filterNotNull()
            .map { POINTS_MAP[it]!! }.sum())


        lines.filter {
            getFirstIllegalChar(it) == null
        }
    }

    fun getFirstIllegalChar(s : String) : Char? {
        if(s.isEmpty()) {
            return null
        }
        var st = s
        END_CHAR_MAP.map { "${it.key}${it.value}" }.forEach{
           st = st.replace(it, "")
        }
        if(s == st){
            val firstEndChar = st.toCharArray().firstOrNull(){END_CHAR_MAP.values.contains(it)}
            if(firstEndChar == null){
                return null
            }
            return firstEndChar
        }
        return getFirstIllegalChar(st)
    }
}