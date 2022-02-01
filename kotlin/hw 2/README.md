# csc-kotlin2021-homework2

* Объявите класс IntPair1 такой, чтобы он
    - содержал неявные методы hashCode(), equals(), toString(), component1(), component2() и copy()
    - содержал неизменяемые свойства valueX и valueY типа Int
    - конструктор без параметров (valueX и valueY инициализируются значениями -1)
    - конструктор с двумя параметрами - valueX и valueY
    
* Объявите класс IntPair2 такой, чтобы он
    - допускал наследование
    - содержал изменяемые свойства valueX и valueY типа Int,
    - имел конструктор с двумя параметрами - valueX и valueY
    - имел private конструктор без параметров
    - имел метод переопределяемый sum, возвращающий сумму valueX и valueY - Int
    - имел метод непереопределяемый prod, возвращающий произведение valueX и valueY - Int
    - имел абстрактный метод gcd, возвращающий Int
    
*    Далее объявите класс-наследник DerivedIntPair2 такой, чтобы он
    - не допускал дальнейшего наследования
    - имел конструктор с двумя параметрами - valueX и valueY
    - переопределял метод gcd и реализовывал в нём вычисление наибольшего общего делителя свойств valueX и valueY
    (см. https://younglinux.info/algorithm/euclidean)


* Ознакомьтесь с кодом. Найдите и исправьте ошибку в функции <code>runExam</code>, приводящую к тому,
что функция нарушает утверждения в комментариях-документации.

* Модифицируйте тело функции <code>sendMessageToClient</code> так, чтобы там осталось не более одного утверждения <code>if (...)</code>.
Используйте операторы <code>?.</code> и <code>?:</code> вместе с <code>return</code>

* Почему не работают smart casts на свойствах <code>serviceVisitorsStats</code>, <code>loggingService</code>, <code>visitorsCounter</code>?
* Почему плохо наличие not-null assertions (<code>!!</code>) в этих местах?
* Модифицируйте код <code>MessagingService.handleRequest</code> и <code>ServiceVisitorsStats.visitorsCounter</code> так, чтобы они более не содержали ни одного утверждения об отсутствии <code>null</code> (not-null assertion, <code>!!</code>)
* Используйте оператор безопасного доступа <code>?.</code>, а также <code>?.let { ... }</code> и присваивание значения свойства в локальные переменные.
