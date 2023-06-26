import java.util.concurrent.atomic.AtomicReference

class Solution(private val env: Environment) : Lock<Solution.Node> {
    private val tail = AtomicReference<Node>()

    override fun lock(): Node {
        val my = Node()
        val prev = tail.getAndSet(my) ?: return my
        prev.next.value = my

        while (my.locked.get()) env.park()

        return my
    }

    override fun unlock(node: Node) {
        if (node.next.get() == null) {
            if (tail.compareAndSet(node, null)) {
                return
            } else {
                while (node.next.get() == null) continue
            }
        }

        val n = node.next.value
        n.locked.value = false
        env.unpark(n.thread)
    }

    data class Node(
        val thread: Thread = Thread.currentThread(),
        val locked: AtomicReference<Boolean> = AtomicReference(true),
        val next: AtomicReference<Node> = AtomicReference<Node>()
    )
}
