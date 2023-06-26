fun main() {
    Generator.generate(pathFile = "./src/main/resources/calculator.g5")
    Generator.generate(pathFile = "./src/main/resources/lab2.g5")

    generated.calculator.Parser.parse("2 + 4 * 2").also {
        // MyInt(2 + 4 * 2) === 2 - 4 / 2 === 0
        println("calculated res = ${it.value}")
//        println("-----------")
//        println(it)
    }
//
//    println("\n-------------------------------------\n")
//
//    generated.lab2.Parser.parse("lambda a, b: a + b").also {
//        println(it)
//    }
}
