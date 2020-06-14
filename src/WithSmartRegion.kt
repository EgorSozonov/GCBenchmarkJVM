import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.currentStackTrace

class WithSmartRegion(val height: Int) {
    val ELTS_IN_REGION = 4
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

        createTree(arrayOf(10, -3, -3, -3))
        sum = 0
    }


    // ind кодирует как отн., так и абс. индекс
    class Loc(val reg: Array<Int>, val ind: Int) {}


    fun getExactValue(ind: Int, region: Array<Int>): Int {
        if (ind < 0) throw Exception("Region index must be non-negative, not " + ind)

        if (ind < SIZE_REGION) {
            return ind
        } else {
            val absoluteInd = ind - SIZE_REGION
            val theRegion = regions[absoluteInd / SIZE_REGION]
            val iInregion = absoluteInd % SIZE_REGION
            return iInregion
        }
    }


    fun toExact(loc: Loc): ExactLoc {
        var region = loc.reg
        val exactInd = getExactValue(loc.ind, region)
        return ExactLoc(region, exactInd)
    }


    // ind < SIZE_REGION
    class ExactLoc(val reg: Array<Int>, val ind: Int)




    fun createTree(payload: Array<Int>) {
        if (height <= 0) return;
        val stack = Stack<ExactLoc>()
        val a = createLeftTree(height, payload, regions[0], stack)
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
                val newSubTree = createLeftTree(height - stack.count(), payload, bottomElement.reg, stack)
                bottomElement.reg[bottomElement.ind + 1] = newSubTree.ind
            }
        }
    }


    fun createLeftTree(height: Int, payload: Array<Int>, baseReg: Array<Int>, stack: Stack<ExactLoc>): Loc {
        val wholeTree = allocateNode(payload, baseReg)
        var currTree: ExactLoc = toExact(wholeTree)
        stack.push(currTree)
        for (i: Int in 1 until height) {
            val newTree = allocateNode(payload, currTree.reg)
            currTree.reg[currTree.ind] = newTree.ind
            currTree = toExact(newTree)
            stack.push(currTree)
        }
        return wholeTree
    }


    fun processTree(): Int {
        if (indFree == 0) {
            println("Oh noes, the tree is null!")
            return -1
        } else {
            val stack = Stack<ExactLoc>()
            processLeftTree(Loc(regions[0], 0), stack)
            while(stack.any()) {
                val bottomElem = stack.pop()
                val indRight = bottomElem.reg[bottomElem.ind + 1]
                if (indRight > -1) processLeftTree(Loc(bottomElem.reg, indRight), stack)
            }
        }
        return sum
    }


    fun processLeftTree(root: Loc, stack: Stack<ExactLoc>) {
        val exactRoot = toExact(root)
        stack.push(exactRoot)
        var currRegion: Array<Int> = exactRoot.reg
        for (i: Int in (exactRoot.ind + 2)..(exactRoot.ind + SIZE_PAYLOAD + 1)) {
            sum += currRegion[i]
        }

        var currLeft = currRegion[exactRoot.ind]
        while (currLeft > -1) {
            var indInRegion = getExactValue(currLeft, currRegion)

            for (i: Int in (indInRegion + 2)..(indInRegion + SIZE_PAYLOAD + 1)) {
                sum += currRegion[i]
            }
            stack.push(ExactLoc(currRegion, indInRegion))
            currLeft = currRegion[indInRegion]
        }
    }


    fun allocateNode(payload: Array<Int>, baseRegion: Array<Int>): Loc {
        val resultInd: Int
        if (indFree == SIZE_REGION) {
            lastRegion = regions[++indRegion]
            indFree = 0
        }
        val resultReg: Array<Int> = lastRegion
        if (lastRegion == baseRegion) {
            resultInd = indFree
        } else {
            resultInd = (indRegion + 1)*SIZE_REGION + indFree
        }

        lastRegion[indFree++] = -1
        lastRegion[indFree++] = -1
        for (i: Int in payload) {
            lastRegion[indFree++] = i
        }
        return Loc(resultReg, resultInd)
    }

}