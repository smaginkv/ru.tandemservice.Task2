package ru.tandemservice.test.task2;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ElementExampleImpl.Context elementExampleImplContext = new ElementExampleImpl.Context();
        IElement[] array = new IElement[4];
        array[0] = new ElementExampleImpl(elementExampleImplContext, 5);
        array[1] = new ElementExampleImpl(elementExampleImplContext, 19);
        array[2] = new ElementExampleImpl(elementExampleImplContext, 18);
        array[3] = new ElementExampleImpl(elementExampleImplContext, -5);

        Task2Impl.INSTANCE.assignNumbers(Arrays.asList(array));
    }
}
