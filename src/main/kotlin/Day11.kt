class Day11 {

    fun day11() {
        val fileContent = Day11::class.java.getResource("/day11.txt").readText()
        val cells: List<Cell> =
            fileContent.lines().map { it.toCharArray().map { it.digitToInt() } }.flatMapIndexed { y, e ->
                e.mapIndexed { x, e2 -> Cell(e2, x, y, false) }
            }

        var grid: MutableMap<Int, MutableMap<Int, Cell>> =
            cells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }
                .mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap

        var flashCounter = 0
        for (i in 1..100) {
            flashCounter += doStep(grid)
        }
        println(flashCounter)

        grid = cells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }
            .mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap

        var flashCounterPart2 = 0
        var stepCounter = 0
        while (flashCounterPart2 != cells.size) {
            stepCounter++
            flashCounterPart2 = doStep(grid)
        }
        println(stepCounter)
    }

    private fun doStep(grid: MutableMap<Int, MutableMap<Int, Cell>>): Int {
        var flashCounter = 0
        traverseAllCells(grid) {
            grid[it.x]!!.put(it.y, it.copy(value = it.value + 1))
        }

        var flashingCells = getFlashingCells(grid)
        while (flashingCells.isNotEmpty()) {
            flashCounter += flashingCells.size
            flashingCells.forEach {
                doFlash(it, grid)
            }
            flashingCells = getFlashingCells(grid)
        }
        traverseAllCells(grid) {
            if (it.flashed) {
                grid[it.x]!!.put(it.y, it.copy(value = 0, flashed = false))
            }
        }
        return flashCounter
    }

    private fun doFlash(cell: Cell, grid: MutableMap<Int, MutableMap<Int, Cell>>) {
        updateCell(cell.x - 1, cell.y, grid)
        updateCell(cell.x + 1, cell.y, grid)
        updateCell(cell.x, cell.y - 1, grid)
        updateCell(cell.x, cell.y + 1, grid)
        updateCell(cell.x - 1, cell.y - 1, grid)
        updateCell(cell.x + 1, cell.y - 1, grid)
        updateCell(cell.x - 1, cell.y + 1, grid)
        updateCell(cell.x + 1, cell.y + 1, grid)

        grid[cell.x]?.put(cell.y, cell.copy(flashed = true))
    }

    private fun updateCell(x: Int, y: Int, grid: MutableMap<Int, MutableMap<Int, Cell>>) {
        val cell = grid[x]?.get(y)
        cell?.let {
            grid[x]?.put(y, it.copy(value = it.value + 1))
        }
    }

    private fun getFlashingCells(grid: MutableMap<Int, MutableMap<Int, Cell>>): List<Cell> {
        val result = mutableListOf<Cell>()
        traverseAllCells(grid) {
            if (!it.flashed && it.value > 9) {
                result.add(it)
            }
        }
        return result
    }

    private fun traverseAllCells(grid: MutableMap<Int, MutableMap<Int, Cell>>, f: (Cell) -> Unit) {
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        for (y in 0..maxX) {
            for (x in 0..maxY) {
                f(grid[x]!![y]!!)
            }
        }
    }

    private data class Cell(val value: Int, val x: Int, val y: Int, val flashed: Boolean)
}