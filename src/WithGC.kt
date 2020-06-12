import java.util.*

class WithGC(val height: Int) {
    val theTree: Tree?
    var sum: Int

    init {
        theTree = createTree(arrayOf(1, 2, -1, -1))
        sum = 0
    }


    class Tree(var left: Tree?, var right: Tree?, val payload: Array<Int>) {

    }


    fun createTree(payload: Array<Int>): Tree? {
        if (height <= 0) return null;

        var stack = Stack<Tree>()
        val wholeTree = createLeftTree(height, payload, stack)
        while (stack.any()) {
            var bottomElement = stack.peek()
            if (bottomElement.right != null || stack.count() == height) {
                stack.pop()
                while (stack.any() && stack.peek().right != null) stack.pop()
            }
            if (stack.any() && stack.count() < height) {
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
        stack.push(wholeTree)
        for (i: Int in 1 until height) {
            newArr = payload.copyOf()
            val newTree = Tree(null, null, newArr)
            currTree.left = newTree
            currTree = newTree
            stack.push(currTree)
        }
        return wholeTree
    }


    fun processTree(): Int {
        if (theTree == null) {
            println("Oh noes, the tree is null!")
            return -1
        } else {
            val stack = Stack<Tree>()
            processLeftTree(theTree, stack)
            while(stack.any()) {
                val bottomElem = stack.pop().right
                if (bottomElem != null) processLeftTree(bottomElem, stack)
            }
        }
        return sum
    }


    fun processLeftTree(tree: Tree, stack: Stack<Tree>) {
        var currElem: Tree? = tree
        if (currElem != null) {
            stack.push(currElem)
            for (i: Int in currElem.payload) {
                sum += i
            }
            while (currElem?.left != null) {
                currElem = currElem.left
                if (currElem != null) {
                    for (i: Int in currElem.payload) {
                        sum += i
                    }
                    stack.push(currElem)
                }
            }
        }
    }
}