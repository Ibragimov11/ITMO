/**
 *@author Said Ibragimov on 10.12.2022 08:06
 */

fun main() {
    Graphviz.createDigraph(Parser.parse("lambda a, b : a + 932153 | b * (b & a)"))
}
