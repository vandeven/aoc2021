class Day5 {
    fun day5(){
        val fileContent = Day1::class.java.getResource("/day5.txt").readText()
        val lines = fileContent.lines()
            .map {
                val start = it.substringBeforeLast(" ->").split(",").map { it.toInt() }
                val end = it.substringAfterLast("-> ").split(",").map { it.toInt() }
                Line(Point(start[0], start[1]), Point(end[0], end[1]))
            }.filter { it.isLine() }
            .flatMap { it.getAllLinePoints() }
            .groupBy { it }
            .count { it.value.size > 1 }

        println(lines)


        val lines2 = fileContent.lines()
            .map {
                val start = it.substringBeforeLast(" ->").split(",").map { it.toInt() }
                val end = it.substringAfterLast("-> ").split(",").map { it.toInt() }
                Line(Point(start[0], start[1]), Point(end[0], end[1]))
            }
            .flatMap { it.getAllLinePoints() }
            .groupBy { it }
            .count { it.value.size > 1 }

        println(lines2)
    }

    data class Line(val from: Point, val to : Point) {

        fun isLine() =from.x == to.x || from.y == to.y

        fun getAllLinePoints() = if(from.x == to.x){
                IntRange(Math.min(from.y, to.y), Math.max(from.y, to.y)).map { Point(from.x, it) }
            } else if(from.y == to.y) {
                IntRange(Math.min(from.x, to.x), Math.max(from.x, to.x)).map { Point(it, from.y) }
            } else {
            val xRange = if(from.x < to.x){
                from.x..to.x
            } else {
                from.x.downTo(to.x)
            }.toList()
            val yRange = if(from.y < to.y){
                from.y..to.y
            } else {
                from.y.downTo(to.y)
            }.toList()
            xRange.zip(yRange).map { Point(it.first,it.second) }
        }
    }
    data class Point(val x : Int, val y : Int)
}