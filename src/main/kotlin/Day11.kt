
class Day11 {

    fun day11() {
        val fileContent = Day11::class.java.getResource("/day11.txt").readText()
        val cells: List<Cell> =
            fileContent.lines().map { it.toCharArray().map { it.digitToInt() } }.flatMapIndexed { y, e ->
                e.mapIndexed { x, e2 -> Cell(e2, x, y,false) }
            }

        var grid: MutableMap<Int, MutableMap<Int, Cell>> = cells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap

        var flashCounter = 0
        for (i in 1..100){
            flashCounter += doStep(grid)
        }
        println(flashCounter)

        grid = cells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap

        var flashCounterPart2 = 0
        var stepCounter = 0
        while(flashCounterPart2 != cells.size){
            stepCounter++
            flashCounterPart2 = doStep(grid)
        }
        println(stepCounter)
    }

    fun doStep(grid: MutableMap<Int, MutableMap<Int, Cell>>) : Int{
        var flashCounter = 0
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        for (y in 0..maxX) {
            for (x in 0..maxY) {
                val cell = grid[x]!![y]!!
                grid[x]!!.put(y, cell.copy(value = cell.value + 1))
            }
        }

        var flashingCells = getFlashingCells(grid)
        while(flashingCells.isNotEmpty()){
            flashCounter += flashingCells.size
            flashingCells.forEach{
                doFlash(it, grid)
            }
            flashingCells = getFlashingCells(grid)
        }
        for (y in 0..maxX) {
            for (x in 0..maxY) {
                val cell = grid[x]!![y]!!
                if(cell.flashed){
                    grid[x]!!.put(y, cell.copy(value = 0, flashed = false))
                }
            }
        }
        return flashCounter
    }

    fun doFlash(cell : Cell, grid: MutableMap<Int, MutableMap<Int, Cell>>) {
        updateCell(cell.x -1, cell.y, grid)
        updateCell(cell.x +1, cell.y, grid)
        updateCell(cell.x, cell.y -1, grid)
        updateCell(cell.x, cell.y +1, grid)
        updateCell(cell.x -1, cell.y -1, grid)
        updateCell(cell.x +1, cell.y -1, grid)
        updateCell(cell.x -1, cell.y +1, grid)
        updateCell(cell.x +1, cell.y +1, grid)

        grid[cell.x]?.put(cell.y, cell.copy(flashed = true))
    }

    fun updateCell(x : Int, y : Int, grid : MutableMap<Int, MutableMap<Int, Cell>>){
        val cell = grid[x]?.get(y)
        cell?.let {
            grid[x]?.put(y, it.copy(value = it.value + 1))
        }
    }

    fun getFlashingCells(grid: MutableMap<Int, MutableMap<Int, Cell>>) : List<Cell>{
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        val result = mutableListOf<Cell>()
        for (y in 0..maxX) {
            for (x in 0..maxY) {
                val cell = grid[x]!![y]!!
                if (!cell.flashed && cell.value > 9){
                    result.add(cell)
                }
            }
        }
        return result
    }



    data class Cell(val value: Int, val x: Int, val y: Int, val flashed : Boolean)
}