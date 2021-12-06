import java.util.concurrent.atomic.AtomicLong

class Day6 {

    fun day6() {
        val fileContent = Day1::class.java.getResource("/day6.txt").readText()
        var lines: List<Int> = fileContent.lines().flatMap { it.split(",").map { it.toInt() } }
        val map = lines.groupBy { it }
        val result = map.mapValues {
            val acc = AtomicLong(0)
            alreadySpawnedFish(it.key, acc, 256)
            acc.get() * it.value.size
        }.values.sum()
        println(result)
    }

    fun alreadySpawnedFish(counter: Int, map: AtomicLong, maxDays: Int) {
        map.incrementAndGet()
        for (i in ((counter + 1)..maxDays).step(7)) {
            spawnFish(i, map, maxDays)
        }
    }

    fun spawnFish(currentDay: Int, map: AtomicLong, maxDays: Int) {
        map.incrementAndGet()
        val nextSpawn = currentDay + 9
        if (nextSpawn <= maxDays) {
            spawnFish(nextSpawn, map, maxDays)
        }
        for (i in ((nextSpawn + 7)..maxDays).step(7)) {
            spawnFish(i, map, maxDays)
        }
    }
}