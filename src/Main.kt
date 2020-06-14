import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    val treeHeight = 25
    run(treeHeight, "GC", ::runWithGC)
    //run(treeHeight, "Regions", ::runWithRegions)
    //run(treeHeight, "Smart Regions", ::runWithSmartRegions)
    //run(treeHeight, "Progressive", ::runWithProgressiveRegions)
    //run(treeHeight, "Smart Progressive Regions", ::runWithSmartProgressiveRegions)

}


fun run(height: Int, designator: String, coreFun: (h: Int) -> Int) {
    println("Processing tree with $designator...")
    val timeStart: Date = Date.from(Instant.now())

    val result = coreFun(height);

    val timeEnd = Date.from(Instant.now())

    println("Finished with result = " + result)
    println("Used time = " + getDateDiff(timeStart, timeEnd, TimeUnit.SECONDS) + " s")
}


fun runWithGC(height: Int): Int {
    val withGC = WithGC(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    return withGC.processTree()
}


fun runWithRegions(height: Int): Int {
    val withRegion = WithRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    return withRegion.processTree()
}


fun runWithSmartRegions(height: Int): Int {
    val withRegion = WithSmartRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    return withRegion.processTree()
}

fun runWithProgressiveRegions(height: Int): Int {
    val withRegion = WithProgressiveRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    return withRegion.processTree()
}


fun runWithSmartProgressiveRegions(height: Int): Int {
    val withRegion = WithSmartProgRegion(height)

    val runtime = Runtime.getRuntime()
    val memory: Long = runtime.totalMemory() - runtime.freeMemory()
    println("Used memory = " + (memory / 1024L / 1024L) + " MB")

    return withRegion.processTree()
}


fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies: Long = date2.getTime() - date1.getTime()
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}
