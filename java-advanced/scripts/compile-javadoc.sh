rm -r -f ../javaDoc

Link="https://docs.oracle.com/en/java/javase/17/docs/api/"
package="../../java-advanced-2022/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/"

javadoc -link "$Link" -html5 -private -d ../javaDoc ../java-solutions/info/kgeorgiy/ja/ibragimov/implementor/Implementor.java \
"$package"Impler.java "$package"JarImpler.java "$package"ImplerException.java
