class Day1 {
    fun day1(){
        val fileContent = Day1::class.java.getResource("/day1.txt").readText()
        val lines = fileContent.lines().map { it.toInt() }

        println("part1:" + lines.filterIndexed{ i,e ->  i == 0 || e > lines[i-1] }.drop(1).count())

        val lines2 = lines.windowed(3, 1).map { it.sum() }
        println("part2:" +lines2.filterIndexed{ i, e ->  i == 0 || e > lines2[i-1] }.drop(1).count())
    }
}