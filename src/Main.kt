import java.time.Instant

fun main(args: Array<String>) {
    println("Processing tree with GC...")
    val timeStart = Instant.now()
    val withGC = WithGC(8)
    val result = withGC.processTree()
    val timeEnd = Instant.now()
    println("Finished with result = " + result)
    println("Used time: " + (timeEnd.d - timeStart))
    //readLine()
}
