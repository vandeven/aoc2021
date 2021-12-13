import java.util.*


class Day12 {


    fun day12() {
        val fileContent = Day12::class.java.getResource("/day12.txt").readText()
        val lines = fileContent.lines()

        val caves = lines
            .flatMap { it.split("-") }
            .toSet()
            .map { toCave(it) }

        lines
            .map { it.split("-") }
            .forEach { c ->
                val first = caves.first { it.id == c[0] }
                val second = caves.first { it.id == c[1] }
                if (first is Start || second is End) {
                    first.connections.add(second.id)
                } else if (second is Start || first is End) {
                    second.connections.add(first.id)
                } else {
                    first.connections.add(second.id)
                    second.connections.add(first.id)
                }


            }

        val start = caves.filterIsInstance<Start>().first()

        println(findAllPaths(start, mutableListOf(), caves).size)
        println(findAllPaths2(start, mutableListOf(), caves).size)
    }

    fun findAllPaths(node: Cave, currentRoute: List<Cave>, caves: List<Cave>): List<List<Cave>> {
        val paths = mutableListOf<List<Cave>>()
        if (node.connections.isEmpty() && node is SmallCave) {
            val last = currentRoute.last()
            if (last is BigCave) {
                paths.addAll(findAllPaths(last, currentRoute.plus(node), caves).map { listOf(node) + it })
                return paths
            }
        }
        for (nextNode in node.connections.map { c -> caves.first { it.id == c } }) {
            if (!(nextNode is SmallCave && currentRoute.contains(nextNode))) {
                if (nextNode is End) {
                    paths.add(mutableListOf(node, nextNode))
                } else {
                    paths.addAll(findAllPaths(nextNode, currentRoute.plus(node), caves).map { listOf(node) + it })
                }
            }
        }
        return paths
    }


    fun findAllPaths2(node: Cave, currentRoute: List<Cave>, caves: List<Cave>): List<List<Cave>> {
        val paths = mutableListOf<List<Cave>>()
        if (node.connections.isEmpty() && node is SmallCave) {
            val last = currentRoute.last()
            if (last is BigCave) {
                paths.addAll(findAllPaths2(last, currentRoute.plus(node), caves).map { listOf(node) + it })
                return paths
            }
        }
        for (nextNode in node.connections.map { c -> caves.first { it.id == c } }) {
            if (nextNode is SmallCave) {
                if (canVisit(nextNode, currentRoute.plus(node))) {
                    val nextPaths: List<List<Cave>> =
                        findAllPaths2(nextNode, currentRoute.plus(node), caves).map { listOf(node) + it }
                    paths.addAll(nextPaths)
                }
            } else {
                if (nextNode is End) {
                    if (node is SmallCave) {
                        if (canVisit(nextNode, currentRoute.plus(node))) {
                            paths.add(mutableListOf(node, nextNode))
                        }
                    } else {
                        paths.add(mutableListOf(node, nextNode))
                    }
                } else {
                    paths.addAll(findAllPaths2(nextNode, currentRoute.plus(node), caves).map { listOf(node) + it })
                }
            }
        }
        return paths
    }

    fun canVisit(node : Cave, currentRoute : List<Cave>) : Boolean {
        val smallCaves = currentRoute.filterIsInstance<SmallCave>()
        val hasNoDouble = smallCaves.toSet().size == smallCaves.size
        val hasBeenVisited = smallCaves.contains(node)
        return (!hasBeenVisited) || (hasBeenVisited && hasNoDouble)
    }

    fun toCave(it: String) = when (it) {
        "start" -> Start(it, mutableSetOf())
        "end" -> End(it, mutableSetOf())
        it.uppercase() -> BigCave(it, mutableSetOf())
        else -> SmallCave(it, mutableSetOf())
    }

    sealed class Cave {
        abstract val id: String
        abstract val connections: MutableSet<String>
    }

    data class Start(override val id: String, override val connections: MutableSet<String>) : Cave()
    data class End(override val id: String, override val connections: MutableSet<String>) : Cave()
    data class SmallCave(override val id: String, override val connections: MutableSet<String>) : Cave()
    data class BigCave(override val id: String, override val connections: MutableSet<String>) : Cave()
}