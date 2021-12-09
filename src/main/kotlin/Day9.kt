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

            val grid: MutableMap<Int, MutableMap<Int, Cell>> = cleanMap.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap
            val maxX = grid.keys.maxOrNull()!!
            val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
            for(basinNumber in 1..(maxX * maxY)){
                fillBasin(grid, basinNumber)
            }
            println(grid.values.flatMap { it.values }
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

    fun print(grid: MutableMap<Int, MutableMap<Int, Cell>>) : String{
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        return IntRange(0, maxY).map { y ->
            IntRange(0, maxX).map { x ->
                if(grid[x]?.get(y)?.value == 9999){
                    "***"
                } else {
                    "${grid[x]?.get(y)?.value!!}".padEnd(3)
                }
            }.joinToString(separator = "")
        }.joinToString(separator = "\n")
    }

    fun findStartPoint(grid : MutableMap<Int, MutableMap<Int, Cell>>) :Cell? {
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!

        for( y in 0..maxY) {
            for (x in 0..maxX) {
                if(grid[x]?.get(y)?.value!! == 0){
                    return grid[x]?.get(y)!!
                }
            }
        }
        return null
    }

    fun fillBasin(grid : MutableMap<Int, MutableMap<Int, Cell>>, basinNumber: Int){
        val startPoint = findStartPoint(grid)
        if(startPoint == null){
            return
        }
        grid[startPoint.x]?.put(startPoint.y,startPoint.copy(value = basinNumber))
        fill(startPoint.x,startPoint.y,basinNumber,grid)
    }
    fun fill(x : Int, y : Int, basinNumber: Int, grid : MutableMap<Int, MutableMap<Int, Cell>>){
        val neighbours = getNeighbours(x,y,grid)
        if(neighbours.isEmpty()){
            return
        }
        neighbours.forEach {
            grid[it.x]?.put(it.y, it.copy(value = basinNumber))
            fill(it.x, it.y, basinNumber, grid)
        }
    }

    fun getNeighbours(x : Int, y : Int, grid : MutableMap<Int, MutableMap<Int, Cell>>) : List<Cell>{
        return listOf(grid[x - 1]?.get(y), grid[x + 1]?.get(y),grid[x]?.get(y - 1),grid[x]?.get(y + 1))
            .filterNotNull()
            .filter { it.value ==0 }
    }

    data class Cell(val value: Int, val x: Int, val y: Int)
}