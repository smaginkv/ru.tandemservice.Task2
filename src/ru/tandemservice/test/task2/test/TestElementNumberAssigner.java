package ru.tandemservice.test.task2.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.tandemservice.test.task2.ElementExampleImpl;
import ru.tandemservice.test.task2.IElement;
import ru.tandemservice.test.task2.IElementNumberAssigner;
import ru.tandemservice.test.task2.Task2Impl;

public class TestElementNumberAssigner {
    private final static Random RAND = new Random(48);
    private static TestElementNumberAssigner instance;
    private List<IElement> elements;
    private String initialOrder;
    private ElementExampleImpl.Context elementExampleImplContext;

    private TestElementNumberAssigner() {
    };

    public static TestElementNumberAssigner getInstance() {
        if (instance == null) {
            instance = new TestElementNumberAssigner();
        }
        return instance;
    }

    public void test() {
        // initial before use
        elementExampleImplContext = new ElementExampleImpl.Context();
        elements = Arrays.asList(getRandArray());
        initialOrder = iElementToString();

        try {
            Task2Impl.INSTANCE.assignNumbers(elements);
        } catch (IllegalStateException e) {
            throw new RuntimeException(initialOrder + ". " + e.getMessage());
        }

        checkAssigning();
    }

    private String iElementToString() {
        StringBuilder str = new StringBuilder("Initial order: ");
        for (int i = 0; i < elements.size(); i++) {
            str.append(i == 0 ? "" : ", ");
            str.append(elements.get(i).getNumber());
        }
        return str.toString();
    }

    private IElement[] getRandArray() {
        // from 5 to 10
        int arrayLength = RAND.nextInt(5) + 6;
        IElement[] array = new IElement[arrayLength];
        populateArray(array);
        return array;
    }

    private void populateArray(IElement[] array) {
        Map<Integer, Integer> uniqueNumber = new HashMap<Integer, Integer>();
        for (int i = 0; i < array.length; i++) {
            int nextNumber = 0;
            while (true) {
                nextNumber = RAND.nextInt(30) - 10;
                if (uniqueNumber.containsKey(nextNumber) == false) {
                    uniqueNumber.put(nextNumber, 0);
                    break;
                }
            }
            array[i] = new ElementExampleImpl(elementExampleImplContext, nextNumber);
        }
    }

    private void checkAssigning() {
        IElementNumberAssigner task = Task2Impl.INSTANCE;

        // I don't now, can i add extra members to model entity?
        if (task instanceof Task2Impl) {
            int expected = ((Task2Impl) task).getExpectOperationCount();
            int real = elementExampleImplContext.getOperationCount();
            if (real != expected) {
                String message = initialOrder + String.format(". Expected %s, but get %s opertions", expected, real);
                throw new RuntimeException(message);
            }
        }

        for (int index = 0; index < elements.size(); index++) {
            if (elements.get(index).getNumber() != index) {
                String message = initialOrder + String.format(". Wrong order: %s", iElementToString());
                throw new RuntimeException(message);
            }
        }
    }
}
