class Day13 {
    fun day13(){
        val fileContent = Day13::class.java.getResource("/day13.txt").readText()

        val initCells: List<Cell> =
            fileContent.lines().takeWhile { it.isNotEmpty() }.map { it.split(",") }.map { Cell('#', it[0].toInt(), it[1].toInt()) }
        val maxX = initCells.maxOf { it.x }
        val maxY = initCells.maxOf { it.y }

        val emptyCells = mutableListOf<Cell>()
        val asGrid = initCells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first() } as MutableMap } as MutableMap
        for(y in 0..maxY){
            for(x in 0..maxX){
                if(asGrid[x]?.get(y) == null){
                    emptyCells.add(Cell('.', x, y))
                }
            }
        }

        val instructions = fileContent.lines()
            .takeLast(fileContent.lines().size - initCells.size - 1)
            .map { it.substringAfter("fold along ") }
            .map { it.split("=") }

        var cells = initCells.plus(emptyCells)

        var grid = cells.groupBy { it.x }.mapValues { it.value.groupBy { it.y } }.mapValues { it.value.mapValues { it.value.first().value } as MutableMap } as MutableMap
        instructions.take(1).forEach {
            if(it[0] == "y"){
                grid = foldUp(it[1].toInt(), grid)
            } else {
                grid = foldLeft(it[1].toInt(), grid)
            }
        }
        println(grid.values.flatMap { it.values }.count { it =='#' })

        instructions.forEach {
            if(it[0] == "y"){
                grid = foldUp(it[1].toInt(), grid)
            } else {
                grid = foldLeft(it[1].toInt(), grid)
            }
        }

        println(print(grid))
    }

    fun foldUp(amount : Int, cells: MutableMap<Int, MutableMap<Int, Char>>) :  MutableMap<Int, MutableMap<Int, Char>> {
        val nextGrid = cells.mapValues { it.value.filter { it.key < amount }.toMutableMap() }.toMutableMap()
        val maxYOld = cells.flatMap { it.value.keys }.maxOrNull()!!
        val maxX = nextGrid.keys.maxOrNull()!!
        val maxY = nextGrid.flatMap { it.value.keys }.maxOrNull()!!
        for(y in 0..maxY) {
            for (x in 0..maxX) {
                val counterPart = cells.get(x)!!.get(maxYOld - y)!!
                if(counterPart != '.'){
                        nextGrid[x]!!.put(y, counterPart)
                }
            }
        }
        return nextGrid
    }

    fun foldLeft(amount : Int, cells : MutableMap<Int, MutableMap<Int, Char>>) : MutableMap<Int, MutableMap<Int, Char>> {
        val nextGrid = cells.filter {  it.key < amount }.toMutableMap()
        val maxXOld = cells.keys.maxOrNull()!!
        val maxX = nextGrid.keys.maxOrNull()!!
        val maxY = nextGrid.flatMap { it.value.keys }.maxOrNull()!!
        for(y in 0..maxY) {
            for (x in 0..maxX) {
                val counterPart = cells.get(maxXOld - x)!!.get(y)!!
                if(counterPart != '.'){
                    nextGrid[x]!!.put(y, counterPart)
                }
            }
        }
        return nextGrid
    }

    data class Cell(val value: Char, val x: Int, val y: Int)

    fun print(grid: MutableMap<Int, MutableMap<Int, Char>>) : String{
        val maxX = grid.keys.maxOrNull()!!
        val maxY = grid.flatMap { it.value.keys }.maxOrNull()!!
        return IntRange(0, maxY).map { y ->
            IntRange(0, maxX).map { x ->

                    "${grid[x]?.get(y)!!}"

            }.joinToString(separator = "")
        }.joinToString(separator = "\n")
    }

}