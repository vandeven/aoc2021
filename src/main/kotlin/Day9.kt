import kotlin.system.measureTimeMillis

class Day9 {
    fun day9() {
        val fileContent = Day9::class.java.getResource("/day9.txt").readText()
        val cells: List<Cell> =
            fileContent.lines().map { it.toCharArray().map { it.digitToInt() } }.flatMapIndexed { y, e ->
                e.mapIndexed { x, e2 -> Cell(e2, x, y) }
            }
        val part1Cells = cells.filter { c ->
            val surroundingLines = cells
                .filter { it.value <= c.value }
                .filter { (it.x + 1 == c.x && it.y == c.y) || (it.x - 1 == c.x && it.y == c.y) || (it.y + 1 == c.y && it.x == c.x) || (it.y - 1 == c.y && it.x == c.x) }
            surroundingLines.isEmpty()
        }
        println(part1Cells.sumOf { it.value + 1 })

        val time = measureTimeMillis {
            //Absolutely horrible implementation that takes forever because it goes through a huge list multiple times and
            //creates more huge lists in the process.. but it works.. didnt have much time today though :(
            val cleanMap = cells.map {
                if (it.value != 9) {
                    Cell(0, it.x, it.y)
                } else {
                    Cell(9999, it.x, it.y)
                }
            }

            var basinNumber = 1
            var filledMap = fillBasin(cleanMap, basinNumber)
            while (filledMap.filter { it.value == 0 }.isNotEmpty()) {
                basinNumber++
                filledMap = fillBasin(filledMap, basinNumber)
            }
            println(filledMap
                .filterNot { it.value == 9999 }
                .groupBy { it.value }
                .mapValues { it.value.size }
                .map { it.value }
                .sortedDescending()
                .take(3)
                .fold(1) { a, b -> a * b }
            )
        }
        println("took: ${time}ms")
    }

    fun fillBasin(cleanMap : List<Cell>, basinNumber: Int): List<Cell>{
        val startPoint = cleanMap.first { it.value == 0 }
        var seededMap = cleanMap.map {
            if (it.x == startPoint.x && it.y == startPoint.y) {
                startPoint.copy(value = basinNumber)
            } else it
        }

        var neighbours = getNeighbours(seededMap, basinNumber)
        while (neighbours.isNotEmpty()){
            seededMap = seededMap.map { if(neighbours.contains(it)) it.copy(value = basinNumber) else it }
            neighbours = getNeighbours(seededMap, basinNumber)
        }
        return seededMap
    }

    fun getNeighbours(seededMap : List<Cell>, basinNumber : Int) : List<Cell>{
        return seededMap.filter { it.value == 0 }.filter {  c->
            val surroundingLines = seededMap
                .filter { it.value == basinNumber }
                .filter { (it.x + 1 == c.x && it.y == c.y) || (it.x - 1 == c.x && it.y == c.y) || (it.y + 1 == c.y && it.x == c.x) || (it.y - 1 == c.y && it.x == c.x) }
            surroundingLines.isNotEmpty()
        }
    }

    data class Cell(val value: Int, val x: Int, val y: Int)
}