import java.util.*
import kotlin.collections.ArrayList

class WithProgressiveRegion(val height: Int) {
    val ELTS_IN_REGION = 200000
    val SIZE_REGION = 6*ELTS_IN_REGION
    val SIZE_PAYLOAD = 4
    val regions: ArrayList<Array<Int>>
    var currRegion: Int
    var indFree: Int
    var sum: Int


    init {
        val numRegions = (Math.pow(2.0, this.height.toDouble()) - 1).toInt()/ELTS_IN_REGION + 1
        regions = ArrayList<Array<Int>>(numRegions)
        for (i: Int in 0 until numRegions) {
            regions.add(Array<Int>(SIZE_REGION) { _ -> 0})
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
            if (bottomElement.arr[bottomElement.ind + 1] > -1 || stack.count() == height) {
                stack.pop()
                while (stack.any()) {
                    bottomElement = stack.peek()
                    if (bottomElement.arr[bottomElement.ind + 1] == -1) break
                    stack.pop()
                }
            }
            if (stack.any() ) {
                bottomElement = stack.peek()
                bottomElement.arr[bottomElement.ind + 1] = createLeftTree(height - stack.count(), payload, stack)
            }
        }
    }


    class Loc(val arr: Array<Int>, val ind: Int) {}


    fun createLeftTree(height: Int, payload: Array<Int>, stack: Stack<Loc>): Int {
        if (height == 0) return -1

        val wholeTree = allocateNode(payload)
        var currTree: Loc = toLoc(wholeTree)
        stack.push(currTree)
        for (i: Int in 1 until height) {
            val newTree = allocateNode(payload)
            currTree.arr[currTree.ind] = newTree
            currTree = toLoc(newTree)
            stack.push(currTree)
        }
        return wholeTree
    }


    fun processTree(): Int {
        if (indFree == 0) {
            println("Oh noes, the tree is null!")
            return -1
        } else {
            val stack = Stack<Loc>()
            processLeftTree(toLoc(0), stack)
            while(stack.any()) {
                val bottomElem = stack.pop()
                val indRight = bottomElem.arr[bottomElem.ind + 1]
                if (indRight > -1) processLeftTree(toLoc(indRight), stack)
            }
        }
        return sum
    }


    fun processLeftTree(root: Loc, stack: Stack<Loc>) {
        stack.push(root)
        for (i: Int in (root.ind + 2)..(root.ind + SIZE_PAYLOAD + 1)) {
            sum += root.arr[i]
        }
        var currLeft = root.arr[root.ind]
        while (currLeft > -1) {
            var currNode = toLoc(currLeft)

            for (i: Int in (currNode.ind + 2)..(currNode.ind + SIZE_PAYLOAD + 1)) {
                sum += currNode.arr[i]
            }
            stack.push(currNode)
            currLeft = currNode.arr[currNode.ind]
        }
    }


    fun toLoc(ind: Int): Loc {
        val numRegion = ind / SIZE_REGION
        val offset = ind % SIZE_REGION
        return Loc(regions[numRegion], offset)
    }


    fun allocateNode(payload: Array<Int>): Int {
        if (indFree == SIZE_REGION) {
            ++currRegion
            indFree = 0
            if (currRegion == regions.size) {
                regions.add(Array<Int>(SIZE_REGION, {x -> 0}))
            }
        }

        val region = regions[currRegion]
        //val result = Loc(region, indFree)
        val result = currRegion*SIZE_REGION + indFree
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


    fun setValue(ind: Int, newValue: Int) {
        if (ind < 0) return
        val numRegion = ind / SIZE_REGION
        val offset = ind % SIZE_REGION
        if (numRegion >= regions.size) return
        regions[numRegion][offset] = newValue
    }
}