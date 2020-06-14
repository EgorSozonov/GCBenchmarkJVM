import java.util.*
import kotlin.collections.ArrayList

class WithSmartRegion(val height: Int) {
    val ELTS_IN_REGION = 200000
    val SIZE_PAYLOAD = 4
    val SIZE_REGION = (SIZE_PAYLOAD + 2)*ELTS_IN_REGION

    val regions: ArrayList<Array<Int>>
    var lastRegion: Array<Int>
    var indRegion = 0
    var indFree = 0
    var sum: Int


    init {
        val numRegions = (Math.pow(2.0, this.height.toDouble()) - 1).toInt()/ELTS_IN_REGION + 1
        regions = ArrayList<Array<Int>>(numRegions)
        for (i: Int in 0 until numRegions) {
            regions.add(Array<Int>(SIZE_REGION) { _ -> 0})
        }
        lastRegion = regions[0]

        createTree(arrayOf(1, 2, -1, -1))
        sum = 0
    }


    class Loc(val reg: Array<Int>, val ind: Int)


    fun createTree(payload: Array<Int>) {
        if (height <= 0) return;
        val stack = Stack<Loc>()
        val wholeTree = createLeftTree(height, payload, stack)
        while (stack.any()) {
            var bottomElement = stack.peek()
            if (bottomElement.reg[bottomElement.ind + 1] > -1 || stack.count() == height) {
                stack.pop()
                while (stack.any()) {
                    bottomElement = stack.peek()
                    if (bottomElement.reg[bottomElement.ind + 1] == -1) break
                    stack.pop()
                }
            }
            if (stack.any()) {
                bottomElement = stack.peek()
                val newSubTree = createLeftTree(height - stack.count(), payload, stack)
                bottomElement.reg[bottomElement.ind + 1] = newSubTree.ind
            }
        }
    }


    fun toShortInd(currRegion: Array<Int>, loc: Loc): Int {
        if (loc.reg == currRegion) return loc.ind

    }


    fun createLeftTree(height: Int, payload: Array<Int>, stack: Stack<Loc>): Loc {
        if (height == 0) return -1

        val wholeTree = allocateNode(payload)
        var currTree: Loc = wholeTree
        stack.push(currTree)
        for (i: Int in 1 until height) {
            val newTree = allocateNode(payload)
            currTree.reg[currTree.ind] = newTree.ind
            stack.push(newTree)
            currTree = newTree
        }
        return wholeTree
    }


    fun processTree(): Int {
        if (indFree == 0) {
            println("Oh noes, the tree is null!")
            return -1
        } else {
            val stack = Stack<Loc>()
            processLeftTree(Loc(regions[0], 0), stack)
            while(stack.any()) {
                val bottomElem = stack.pop()
                val indRight = bottomElem.reg[bottomElem.ind + 1]
                if (indRight > -1) processLeftTree(Loc(bottomElem.reg, indRight), stack)
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


    fun allocateNode(payload: Array<Int>): Loc {
        val result: Int
        if (indFree == SIZE_REGION) {
            ++indRegion
            lastRegion = regions[indRegion]
            indFree = 0
            result = (indRegion + 1)*SIZE_REGION
        } else {
            result = indFree
        }

        lastRegion[indFree++] = -1
        lastRegion[indFree++] = -1
        for (i: Int in payload) {
            lastRegion[indFree++] = i
        }
        return Loc(lastRegion, result)
    }


    fun getValue(ind: Int, region: Array<Int>): Int {
        if (ind < 0) throw Exception("Region index must be non-negative, not " + ind)

        if (ind < SIZE_REGION) {
            return ind
        } else {
            val absoluteInd = ind - SIZE_REGION
            lastRegion = regions[absoluteInd/SIZE_REGION]
            val iInregion = absoluteInd%SIZE_REGION
            return iInregion
        }
    }
}