import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

internal class IntPair1Test {
    @Test
    fun testIntPair1() {
        val clazz = IntPair1::class

        assertTrue(clazz.isData, "Класс должен содержать неявные методы hashCode(), equals(), toString(), component1(), component2() и copy()")
        assertTrue(clazz.constructors.size >= 2, "Должно быть как минимум два конструктора")
        assertTrue(clazz.constructors.any { it.parameters.isEmpty() }, "Должно быть конструктор без параметров")
        assertTrue(clazz.constructors.any { it.parameters.size == 2 && isInt(it.parameters[0].type) && isInt(it.parameters[1].type)}, "Должно быть конструктор (Int, Int)")
        assertEquals( 2, clazz.memberProperties.size, "Должно быть два свойства!")
        assertTrue(clazz.memberProperties.any { it !is KMutableProperty<*> && it.name == "valueX" && isInt(it.returnType) }, "Должно быть неизменяемое свойство valueX : Int")
        assertTrue(clazz.memberProperties.any { it !is KMutableProperty<*> && it.name == "valueY" && isInt(it.returnType) }, "Должно быть неизменяемое свойство valueY : Int")

        val check = IntPair1()
        assertEquals("IntPair1(valueX=-1, valueY=-1)", "$check", "Неверная инициализация в конструкторе без параметров")
    }

    private fun isInt(type: KType) =
        type.javaType.typeName == "int" || type.javaType.typeName == "java.lang.Integer"
}