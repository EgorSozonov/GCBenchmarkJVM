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
        val stack = Stack<Loc>()
        val wholeTree = createLeftTree(height, payload, stack)
        while (stack.any()) {
            var bottomElement = stack.peek()
            if (bottomElement.arr[bottomElement.ind + 2] > 0) {
                stack.pop()
                while (stack.any() && stack.peek().right != null) stack.pop()

            }
            if (stack.any()) {
                bottomElement = stack.peek()
                bottomElement.arr[bottomElement.ind + 2] = createLeftTree(height - stack.count(), payload, stack)
            }
        }
        return
        // TODO
    }

    class Loc(val arr: Array<Int>, val ind: Int) {}


    // Populate the tree.
    fun createLeftTree(height: Int, payload: Array<Int>, stack: Stack<Loc>): Int {
        if (height == 0) return 0

        val wholeTree = allocateNode(payload)
        for (i: Int in 1 until height) {

            val newTree = allocateNode(payload)
            newTree.arr[newTree.ind + ]
//            currTree = newTree
            stack.push(currTree)
        }
        return wholeTree
    }


    fun allocateNode(payload: Array<Int>): Loc {
        if (indFree == SIZE_REGION) {
            ++currRegion
            indFree = 0
            if (currRegion == regions.size) {
                regions.add(Array<Int>(SIZE_REGION, {x -> 0}))
            }
        }

        val region = regions[currRegion]
        val result = Loc(region, indFree)
        region[indFree++] = -1
        region[indFree++] = -1
        for (i: Int in payload) {
            region[indFree++] = i
        }
        return result
    }


    fun getValue(ind: Int): Loc {
        if (ind < 0) throw Exception("Region index must be non-negative, not " + ind)
        val numRegion = ind / SIZE_REGION
        val offset = ind % SIZE_REGION
        if (numRegion >= regions.size) throw Exception("Nonexistent region " + numRegion)
        return Loc(regions[numRegion], offset)
    }


    fun getValueUnsafe(ind: Int): Loc {
        val numRegion = ind/SIZE_REGION
        val offset = ind % SIZE_REGION
        return Loc(regions[numRegion], offset)
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