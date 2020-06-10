import java.util.*
import kotlin.collections.ArrayList

class WithRegion(val height: Int) {
    val SIZE_REGION = 6*200000
    val regions: ArrayList<Array<Int>>
    var currRegion: Int
    var indFree: Int
    var sum: Int


    init {
        regions = ArrayList<Array<Int>>(10)
        for (i: Int in 0 until 10) {

        }
        currRegion = 0
        indFree = 0

        createTree(arrayOf(1, 2, -1, -1))
        sum = 0
    }


    fun createTree(payload: Array<Int>) {
        if (height <= 0) return;
        return
        // TODO
    }


    // Populate the tree. Allocates lots of objects for the GC to waste time on.
    fun createLeftTree(height: Int, payload: Array<Int>, stack: Stack<Int>) {
        if (height == 0) return

        val wholeTree = allocateNode(payload)
        for (i: Int in 1 until height) {

            val newTree = allocateNode(payload)
            currTree.left = newTree
            currTree = newTree
            stack.push(currTree)
        }
        return wholeTree
    }


    fun allocateNode(payload: Array<Int>): Int {
        if (indFree == SIZE_REGION) {
            ++currRegion
            indFree = 0
            if (currRegion == regions.size) {
                regions.add(Array<Int>(SIZE_REGION))
            }
        }
        val result = (currRegion*SIZE_REGION + indFree)
        val region = regions[currRegion]
        region[indFree++] = -1
        region[indFree++] = -1
        for (i: Int in payload) {
            region[indFree++] = i
        }
        return result
    }


    fun getValue(ind: Int): Int {
        if (ind < 0) return -1
        val numRegion = ind / SIZE_REGION
        val offset = ind % SIZE_REGION
        if (numRegion >= regions.size) return -1;
        return regions[numRegion][offset]
    }


    fun getValueUnsafe(ind: Int): Int {
        val numRegion = ind/SIZE_REGION
        val offset = ind % SIZE_REGION
        return regions[numRegion][offset]
    }


    fun setValue(ind: Int, newValue: Int) {
        if (ind < 0) return
        val numRegion = ind / SIZE_REGION
        val offset = ind % SIZE_REGION
        if (numRegion >= regions.size) return
        regions[numRegion][offset] = newValue
    }


    fun setValueUnsafe(ind: Int, newValue: Int) {
        val numRegion = ind/SIZE_REGION
        val offset = ind % SIZE_REGION
        regions[numRegion][offset] = newValue
    }

}