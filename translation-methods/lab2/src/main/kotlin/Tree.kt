/**
 *@author Said Ibragimov on 09.12.2022 03:36
 */

data class Tree(
    val node: String,
    val children: MutableList<Tree> = mutableListOf(),
    private var level: Int = 0, // for toString() only
) {
    fun add(child: Tree): Tree {
        children.add(child)
        child.updateLevels(level + 1)

        return this
    }

    fun add(child: String) = add(Tree(child))

    private fun updateLevels(startLevel: Int) {
        level = startLevel
        children.forEach {
            it.updateLevels(startLevel + 1)
        }
    }

    override fun toString(): String {
        return ". ".repeat(level) + node +
                if (children.isNotEmpty()) children.joinToString(
                    prefix = System.lineSeparator(),
                    separator = System.lineSeparator()
                ) else ""
    }
}
