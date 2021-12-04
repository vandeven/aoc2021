
class Day4 {
    fun day4() {
        val fileContent = Day1::class.java.getResource("/day4.txt").readText()

        val lines = fileContent.lines()
        val numbers = lines.first().split(",").map { it.toInt() }
        println(numbers)

        val bingoBoards = lines.drop(1)
            .windowed(6, 6)
            .map {
                it.drop(1)
                    .map {
                        it.split(" ")
                            .filterNot { it.isEmpty() }
                            .map { it.toInt() }
                    }
            }.map { BingoBoard.fromArray(it) }
        println(bingoBoards)

        val winningBoard = getWinningBoardPart1(numbers, bingoBoards)
        println(winningBoard)
        println(winningBoard.first!!.cells.filter { it is UnMarked }.map { it.value }.sum() * winningBoard.second)

        val winningBoardPart2 = getWinningBoardPart2(numbers, bingoBoards)
        println(winningBoardPart2)
        println(winningBoardPart2.first!!.cells.filter { it is UnMarked }.map { it.value }.sum() * winningBoardPart2.second)



    }

    fun getWinningBoardPart1(numbers : List<Int>, boards : List<BingoBoard>) : Pair<BingoBoard?, Int> {
        var boards2 = boards
        for(num in numbers){
            boards2 = boards2.map { it.nextBoard(num) }
            val winningBoard = boards2.firstOrNull { it.hasWon() }
            if (winningBoard != null){
                return winningBoard to num
            }
        }
        return null to 0
    }

    fun getWinningBoardPart2(numbers : List<Int>, boards : List<BingoBoard>) : Pair<BingoBoard?, Int> {
        var boards2 = boards
        for(num in numbers){
            boards2 = boards2.filterNot { it.hasWon() }.map { it.nextBoard(num) }

            if(boards2.size == 1 && boards2.first().hasWon()){
                return boards2.first() to num
            }

        }
        return null to 0
    }

    sealed class Cell {
        abstract val x: Int
        abstract val y: Int
        abstract val value: Int
        abstract fun nextCell(number : Int) : Cell
    }
    data class Marked(override val x: Int, override val y: Int, override val value: Int) : Cell() {
        override fun nextCell(number: Int): Cell {
            return this
        }
    }

    data class UnMarked(override val x: Int,override val y: Int, override val value: Int) : Cell() {
        override fun nextCell(number: Int): Cell {
            if(number == value){
                return Marked(x,y,value)
            }
            return this
        }
    }

    data class BingoBoard(val cells: List<Cell>) {

        fun nextBoard(number : Int) = BingoBoard(cells.map { it.nextCell(number) })

        fun hasWon() : Boolean {
            val y = cells.groupBy { it.y }.filter { it.value.all { it is Marked } }.isNotEmpty()
            val x = cells.groupBy { it.x }.filter { it.value.all { it is Marked } }.isNotEmpty()
            return x || y
        }

        companion object {
            fun fromArray(board: List<List<Int>>) = BingoBoard(board.mapIndexed { y, e ->
                e.mapIndexed { x, el ->
                    UnMarked(x, y, el)
                }
            }.flatten())
        }
    }
}