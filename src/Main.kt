import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    val treeHeight = 26
    //run(treeHeight, "GC", ::runWithGC)
    run(treeHeight, "Regions", ::runWithRegions)
    //run(treeHeight, "Smart Regions", ::runWithSmartRegions)
    //run(treeHeight, "Progressive", ::runWithProgressiveRegions)
    //run(treeHeight, "Smart Progressive Regions", ::runWithSmartProgressiveRegions)

}


fun run(height: Int, designator: String, coreFun: (h: Int, t: Date) -> Int) {
    println("Processing tree with $designator...")
    val timeStart: Date = Date.from(Instant.now())

    val result = coreFun(height, timeStart);

    val timeEnd = Date.from(Instant.now())

    println("Finished with result = " + result)
    println("Used time = " + getDateDiff(timeStart, timeEnd, TimeUnit.SECONDS) + " s")
}


fun runWithGC(height: Int, tStart: Date): Int {
    val withGC = WithGC(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")
    println("Time for alloc = " + getDateDiff(tStart, Date.from(Instant.now()), TimeUnit.SECONDS) + " s")

    return withGC.processTree()
}


fun runWithRegions(height: Int, tStart: Date): Int {
    val withRegion = WithRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")
    println("Time for alloc = " + getDateDiff(tStart, Date.from(Instant.now()), TimeUnit.SECONDS) + " s")

    return withRegion.processTree()
}


fun runWithSmartRegions(height: Int, tStart: Date): Int {
    val withRegion = WithSmartRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")
    println("Time for alloc = " + getDateDiff(tStart, Date.from(Instant.now()), TimeUnit.SECONDS) + " s")

    return withRegion.processTree()
}

fun runWithProgressiveRegions(height: Int, tStart: Date): Int {
    val withRegion = WithProgressiveRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")
    println("Time for alloc = " + getDateDiff(tStart, Date.from(Instant.now()), TimeUnit.SECONDS) + " s")

    return withRegion.processTree()
}


fun runWithSmartProgressiveRegions(height: Int, tStart: Date): Int {
    val withRegion = WithSmartProgRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")
    println("Time for alloc = " + getDateDiff(tStart, Date.from(Instant.now()), TimeUnit.SECONDS) + " s")

    return withRegion.processTree()
}


fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies: Long = date2.getTime() - date1.getTime()
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}
