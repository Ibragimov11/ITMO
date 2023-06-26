import java.io.File

/**
 *@author Said Ibragimov on 10.12.2022 07:21
 */

object Graphviz {
    private fun treeToDigraph(tree: Tree): String =
        buildString {
            appendLine("\t0 [label = \"${tree.node}\"]")

            val deque: ArrayDeque<Pair<Int, Tree>> = ArrayDeque<Pair<Int, Tree>>().apply {
                add(0 to tree)
            }
            var id = 1

            while (deque.isNotEmpty()) {
                val (parentId, parent) = deque.removeFirst()

                for (child in parent.children) {
                    val childId = id++
                    deque.addLast(childId to child)
                    appendLine("\t$childId [label = \"${child.node}\"]")
                    appendLine("\t$parentId -> $childId")
                }
            }
        }

    fun createDigraph(tree: Tree) =
        File("src/main/kotlin/graph.txt").outputStream().writer().use {
            it.write(
                buildString {
                    appendLine("digraph {")
                    append(treeToDigraph(tree))
                    append("}")
                }
            )
        }
}