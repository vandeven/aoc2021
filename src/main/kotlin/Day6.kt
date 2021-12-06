import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

class Day6 {

    fun day6() {
        val fileContent = Day1::class.java.getResource("/day6.txt").readText()
        var lines: List<Int> = fileContent.lines().flatMap { it.split(",").map { it.toInt() } }
        val map = lines.groupBy { it }
        val time = measureTimeMillis {
            val result2 = map.entries.parallelStream().map {
                val acc = AtomicLong(0)
                alreadySpawnedFish(it.key, acc, 256)
                acc.get() * it.value.size
            }.collect(Collectors.toList()).sum()
            println(result2)

//            val result = map.mapValues {
//                val acc = AtomicLong(0)
//                alreadySpawnedFish(it.key, acc, 256)
//                acc.get() * it.value.size
//            }.values.sum()
//            println(result)
        }
        println("took: ${time}ms")
    }

    //Handle the already spawned fish
    fun alreadySpawnedFish(counter: Int, acc: AtomicLong, maxDays: Int) {
        acc.incrementAndGet()

        //Spawn the fish after that at 7 day intervals
        for (i in ((counter + 1)..maxDays).step(7)) {
            spawnFish(i, acc, maxDays)
        }
    }

    fun spawnFish(currentDay: Int, acc: AtomicLong, maxDays: Int) {
        acc.incrementAndGet()

        //spawn first fish after 9 days
        val nextSpawn = currentDay + 9
        if (nextSpawn <= maxDays) {
            spawnFish(nextSpawn, acc, maxDays)
        }
        //Spawn the fish after that at 7 day intervals
        for (i in ((nextSpawn + 7)..maxDays).step(7)) {
            spawnFish(i, acc, maxDays)
        }
    }
}