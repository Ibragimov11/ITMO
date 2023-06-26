mkdir "tempDir"

javac -d tempDir --module-path ../../java-advanced-2022/lib:../../java-advanced-2022/artifacts \
../java-solutions/info/kgeorgiy/ja/ibragimov/implementor/Implementor.java ../java-solutions/module-info.java

jar cmf MANIFEST.MF implementor.jar tempDir/*.class

rm -r -f tempDir
