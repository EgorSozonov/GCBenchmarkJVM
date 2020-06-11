import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    println("Processing tree with GC...")
    val timeStart: Date = Date.from(Instant.now())
    val withGC = WithGC(26)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    val result = withGC.processTree()
    val timeEnd = Date.from(Instant.now())

    println("Finished with result = " + result)
    println("Used time = " + getDateDiff(timeStart, timeEnd, TimeUnit.SECONDS) + " s")
    //readLine()
}


fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies: Long = date2.getTime() - date1.getTime()
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}
