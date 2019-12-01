package ru.tandemservice.test.task2.application;

import ru.tandemservice.test.task2.service.StructureConverter;
import ru.tandemservice.test.task2.testing.Task2ImplTest;

/**
 * Класс для реализации dependency injection
 * @author Smagin.K.V
 */
public class Context {
    private static StructureConverter structureConverter;
    private static Task2ImplTest task2ImplTest;

    /**
     * @return - экземпляр конвертера
     */
    public static StructureConverter getStructureConverter() {
        if (structureConverter == null) {
            structureConverter = new StructureConverter();
        }
        return structureConverter;
    }

    /**
     * @return - экземпляр теста
     */
    public static  Task2ImplTest getTask2ImplTestInstance() {
        if (task2ImplTest == null) {
            task2ImplTest = new Task2ImplTest();
        }
        return task2ImplTest;
    }
}
