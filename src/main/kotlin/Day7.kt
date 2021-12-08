class Day7 {
    fun day7(){
        val fileContent = Day1::class.java.getResource("/day7.txt").readText()
        val lines: List<Int> = fileContent.lines().flatMap { it.split(",").map { it.toInt() } }
        val max = lines.maxOrNull()!!

        val part1 = (0..max).toList().map { it to calclulateFuelCost(lines, it) }.minByOrNull { it.second }!!.second
        println(part1)

        val map = (0..max).toList().map { it to calclulateFuelCostPart2(lines, it) }
        val part2 = map.minByOrNull { it.second }!!.second
        println(part2)
    }

    fun calclulateFuelCost(crabs : List<Int>, position : Int)= crabs.map { Math.max(position, it) - Math.min(position,it) }.sum()

    fun calclulateFuelCostPart2(crabs : List<Int>, position : Int): Int {
        val map = crabs.map {
            val totalSteps = Math.max(position, it) - Math.min(position, it)
            (1..totalSteps).sum()
        }
        return map.sum()
    }
}