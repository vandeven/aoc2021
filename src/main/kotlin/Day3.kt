class Day3 {
    fun day3(){
        val fileContent = Day3::class.java.getResource("/day3.txt").readText()
        val lines: List<List<Int>> = fileContent.lines().map { it.toCharArray().map { it.digitToInt() } }
        val mostCommmon = findMostCommon(lines,0)
        val gamma  = Integer.parseInt(
            mostCommmon.first.joinToString(separator = ""),
            2
        )
        val epsilon  = Integer.parseInt(
            mostCommmon.second.joinToString(separator = ""),
            2
        )
        println("gamma: $gamma")
        println("epsilon: $epsilon")
        println("power: ${gamma * epsilon}" )

        println("====part 2=====" )
        val findOxygen = findLifeSupport(lines) { e -> findMostCommon(e, 1).first }
        val findCo2 = findLifeSupport(lines) { e -> findMostCommon(e, 0).second }

        println("oxygen: $findOxygen")
        println("co2: $findCo2")
        println("lifesupport: ${findOxygen * findCo2}")
    }

    fun findLifeSupport(lines : List<List<Int>>, blah : (List<List<Int>>) -> List<Int>) : Int{
        var i = 0
        var lines2 = lines
        while(lines2.size > 1){
            val mostCommon = blah.invoke(lines2)
            lines2 = lines2.filter { it[i] == mostCommon[i] }
            i++
        }
        return Integer.parseInt(
            lines2.flatten().joinToString(separator = ""),
            2
        )
    }

    fun findMostCommon(lines : List<List<Int>>, default : Int) : Pair<List<Int>, List<Int>>  {
        val fold = lines
            .fold(listOf<Int>()) { acc, e ->
                if (acc.isEmpty()) {
                    e
                } else {
                    acc.zip(e) { e1, e2 ->
                        e1 + e2
                    }
                }
            }
        val size: Double = lines.size.toDouble() / 2.toDouble()
        val mostCommon = fold.map { if(size.equals(it.toDouble())) default else if(it > size) 1 else 0 }
        val leastCommon = fold.map { if(size.equals(it.toDouble())) default else if(it < size) 1 else 0 }
        return mostCommon to leastCommon
    }
}