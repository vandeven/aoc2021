class Day2 {

    data class ForwardDepth(val forward : Int, val depth : Int, val aim : Int)

    fun day2(){
        val fileContent = Day2::class.java.getResource("/day2.txt").readText()



        val parsed = fileContent.lines()
            .map { it.substringBeforeLast(" ") to it.substringAfterLast(" ").toInt() }

        val lines: Map<String, Int> = parsed
            .groupBy { it.first }.mapValues { it.value.map { it.second }.sum() }

        println("part1:" + (lines["down"]!! - lines["up"]!!) * lines["forward"]!!)

        val fold = parsed.fold(ForwardDepth(0, 0, 0)) { acc, e ->
            when (e.first) {
                "down" -> acc.copy( aim = acc.aim + e.second)
                "up" -> acc.copy( aim = acc.aim - e.second)
                "forward" -> acc.copy(forward = acc.forward + e.second, depth = acc.depth + (acc.aim * e.second))
                else -> throw IllegalStateException()
            }
        }
        println("part2" + fold.forward * fold.depth)
    }
}