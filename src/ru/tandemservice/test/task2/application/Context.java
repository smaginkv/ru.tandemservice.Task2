package ru.tandemservice.test.task2.application;

import ru.tandemservice.test.task2.service.MapReplacement;
import ru.tandemservice.test.task2.testing.Task2ImplTest;

/**
 * Класс для реализации dependency injection
 * @author Smagin.K.V
 */
public class Context {
    private static MapReplacement mapReplacement;
    private static Task2ImplTest task2ImplTest;

    public static MapReplacement getMapReplacement() {
        if (mapReplacement == null) {
            mapReplacement = new MapReplacement();
        }
        return mapReplacement;
    }

    public static  Task2ImplTest getTask2ImplTestInstance() {
        if (task2ImplTest == null) {
            task2ImplTest = new Task2ImplTest();
        }
        return task2ImplTest;
    }
}
