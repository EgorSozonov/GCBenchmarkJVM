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


    fun createTree(payload: Array<Int>): Tree? {
        if (height <= 0) return null;

        var stack = Stack<Tree>()
        val wholeTree = createLeftTree(height, payload, stack)
        while (stack.any()) {
            var bottomElement = stack.peek()
            if (bottomElement.right != null) {
                stack.pop()
                while (stack.any() && stack.peek().right != null) stack.pop()

            }
            if (stack.any()) {
                bottomElement = stack.peek()
                bottomElement.right = createLeftTree(height - stack.count(), payload, stack)
            }
        }
        return wholeTree
    }


    // Populate the tree. Allocates lots of objects for the GC to waste time on.
    fun createLeftTree(height: Int, payload: Array<Int>, stack: Stack<Tree>): Tree? {
        if (height == 0) return null;

        var newArr = payload.copyOf()
        val wholeTree = Tree(null, null, newArr)
        var currTree = wholeTree
        for (i: Int in 1 until height) {
            newArr = payload.copyOf()
            val newTree = Tree(null, null, newArr)
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
        val region = regions[currRegion]
        region[indFree++] = -1
        region[indFree++] = -1
        for (i: Int in payload) {
            region[indFree++] = i
        }
        indFree += 6
        return (currRegion*SIZE_REGION + indFree)
    }


}