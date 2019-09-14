package ru.tandemservice.test.task2;

import ru.tandemservice.test.task2.test.TestElementNumberAssigner;

public class Main {

    public static void main(String[] args) {
        int index = 0;
        TestElementNumberAssigner testElementNumberAssigner = TestElementNumberAssigner.getInstance();
        while (++index < 1000000) {
            try {
                testElementNumberAssigner.test();
            } catch (RuntimeException e) {
                throw new RuntimeException(String.format("Step(%s). ", index) + e.getMessage());
            }
        }
        System.out.printf("%s test operations finished", index);
    }
}
