import kotlin.system.measureTimeMillis

class Day9 {
    val MAX_CELL = 9999

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
            val cleanMap = cells.map {
                if (it.value != 9) {
                    Cell(0, it.x, it.y)
                } else {
                    Cell(MAX_CELL, it.x, it.y)
                }
            }

            val grid: MutableMap<Int, MutableMap<Int, Cell>> = cleanMap.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap

            var basinNumber = 1
            part1Cells.forEach{
                fillBasin(it.x, it.y, grid, basinNumber++)
            }

            println(grid.values.flatMap { it.values }
                .filterNot { it.value == MAX_CELL }
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

    fun fillBasin(x : Int, y : Int, grid : MutableMap<Int, MutableMap<Int, Cell>>, basinNumber: Int){
        grid[x]?.put( y, grid[x]?.get(y)?.copy(value = basinNumber)!!)
        fillNeighbours(x, y,basinNumber,grid)
    }

    fun fillNeighbours(x : Int, y : Int, basinNumber: Int, grid : MutableMap<Int, MutableMap<Int, Cell>>){
        getNeighbours(x,y,grid).forEach {
            grid[it.x]?.put(it.y, it.copy(value = basinNumber))
            fillNeighbours(it.x, it.y, basinNumber, grid)
        }
    }

    fun getNeighbours(x : Int, y : Int, grid : MutableMap<Int, MutableMap<Int, Cell>>) : List<Cell>{
        return listOf(grid[x - 1]?.get(y), grid[x + 1]?.get(y),grid[x]?.get(y - 1),grid[x]?.get(y + 1))
            .filterNotNull()
            .filter { it.value ==0 }
    }

    data class Cell(val value: Int, val x: Int, val y: Int)

    fun print(grid: MutableMap<Int, MutableMap<Int, Cell>>) : String{
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        return IntRange(0, maxY).map { y ->
            IntRange(0, maxX).map { x ->
                if(grid[x]?.get(y)?.value == MAX_CELL){
                    "***"
                } else {
                    "${grid[x]?.get(y)?.value!!}".padEnd(3)
                }
            }.joinToString(separator = "")
        }.joinToString(separator = "\n")
    }
}