/**
 * @author Said Ibragimov on 19.04.2022 15:31
 */

interface A {
    void helloA();
}

interface B {
    void helloB();
}

public class Test implements A, B {
    @Override
    public void helloA() {}

    @Override
    public void helloB() {}
}

class Main {
    public static void main(String[] args) {
        A test = new Test();
        test.helloB();
    }
}