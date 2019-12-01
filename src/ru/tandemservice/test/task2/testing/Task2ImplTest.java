package ru.tandemservice.test.task2.testing;

import ru.tandemservice.test.task2.ElementExampleImpl;
import ru.tandemservice.test.task2.IElement;
import ru.tandemservice.test.task2.Task2Impl;
import ru.tandemservice.test.task2.application.Context;

import java.util.*;

/**
 * Класс для реализации тестирования перенумерации
 * Насколько я помню Junit нельзя использовать в теством задании, пришлось топорно делать
 *
 * @author Smagin.K.V
 */
public class Task2ImplTest {

    private final static Random RAND = new Random(48);
    private ElementExampleImpl.Context elementExampleImplContext;
    private List<IElement> elements;
    private String initialOrder;
    private long expectOperationCount;

    /**
     * Генерирует новый контекст для следующего теста
     * @return - возвращаемый контекст
     */
    public ElementExampleImpl.Context getNewElementExampleImplContext() {
        elementExampleImplContext = new ElementExampleImpl.Context();
        return elementExampleImplContext;
    }

    /**
     * Входная точка для запуска теста
     *
     * @param args - аргументы командной строки
     */
    public static void main(String[] args) {
        try {
            shouldCorrectWhenRandomNumber();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            shouldCorrectWhenCornerCase();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Тест на генерацию рандомных списков
     */
    private static void shouldCorrectWhenRandomNumber() {
        for (int index = 0; index < 1_000_000; index++) {
            try {
                Task2ImplTest task2ImplTest = Context.getTask2ImplTestInstance();
                // Выбираем размерность массива случайным образом от 5 до 10
                task2ImplTest.setupTest(RAND.nextInt(5) + 6);
                task2ImplTest.test();
            } catch (RuntimeException e) {
                throw new RuntimeException(String.format("Step(%s). ", index) + e.getMessage());
            }
        }
        System.out.println("random test finished successful");
    }

    /**
     * Тест на генерацию предопределенного списка с "/граничными/" значениями
     */
    private static void shouldCorrectWhenCornerCase() {

        Task2ImplTest task2ImplTest = Context.getTask2ImplTestInstance();
        ElementExampleImpl.Context elementExampleImplContext = task2ImplTest.getNewElementExampleImplContext();
        int number = Integer.MIN_VALUE;
        IElement[] testElement = new IElement[5];

        for (int i = 0; i < testElement.length; i++) {
            testElement[i] = new ElementExampleImpl(elementExampleImplContext, number++);
        }

        task2ImplTest.setupTest(Arrays.asList(testElement));
        task2ImplTest.test();

        System.out.println("corner test finished successful");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Current order: ");
        for (int i = 0; i < elements.size(); i++) {
            str.append(i == 0 ? "" : ", ");
            str.append(elements.get(i).getNumber());
        }
        return str.toString();
    }

    /**
     * Запуск теста
     */
    private void test() {
        try {
            Task2Impl.INSTANCE.assignNumbers(elements);
        } catch (IllegalStateException e) {
            throw new RuntimeException(initialOrder + ". " + e.getMessage());
        }
        checkAssigning();
    }

    /**
     * Настройка теста с заполнением произвольными числами
     *
     * @param arrayLength - размер генерируемого списка элементов
     */
    private void setupTest(int arrayLength) {
        initialOrder = "";
        setupTest(populateArray(arrayLength));

    }

    /**
     * Настройка теста с предопределенными элементами
     *
     * @param elements                  - список элементов который надо протестировать
     */
    private void setupTest(List<IElement> elements) {
        this.elements = elements;

        //Запоминаем как "выглядел" массив, чтобы по логам понять что пошло не так
        initialOrder = toString();

        calculateExpectOperationCount();
    }

    /**
     * Рассчитаем количество операций за которое мы должны перенумеровать массив
     */
    private void calculateExpectOperationCount() {
        //Получим мапу замен - каждая замена = 1 операция
        Map<IElement, IElement> mapReplacement = Context.getStructureConverter().getReplacementMap(elements);
        expectOperationCount = mapReplacement.size();

        //Получим количество цепочек замен, каждая лишняя цепочка +1 операция записи
        elements.forEach(element -> {
            if (mapReplacement.containsKey(element)) {
                expectOperationCount++;
                removeRecursively(mapReplacement, element);
            }
        });
    }

    /**
     * @param map     - мапа которую надо пройти на предмет наличия циклических ссылок
     * @param element - элемент с которого надо начать "распутывать" цепочку
     */
    private void removeRecursively(Map<IElement, IElement> map, IElement element) {
        IElement exchangeElement = map.remove(element);
        if (exchangeElement != null) {
            removeRecursively(map, exchangeElement);
        }
    }

    /**
     * Наполняем массив случайными номерами
     *
     * @param arrayLength - размер List'a номеров
     * @return - unmodifiable list со случайными элементами
     */
    private List<IElement> populateArray(int arrayLength) {
        IElement[] array = new IElement[arrayLength];
        elementExampleImplContext = getNewElementExampleImplContext();
        Set<Integer> uniqueNumber = new HashSet<>();

        for (int i = 0; i < arrayLength; i++) {
            int nextNumber = getRandNumber(uniqueNumber);
            array[i] = new ElementExampleImpl(elementExampleImplContext, nextNumber);
        }
        return Arrays.asList(array);
    }

    /**
     * Генерируем произвольный номер, каждый номер уникален
     *
     * @param uniqueNumber - cтруктура с номерами которые уже повторялись
     * @return - новый номер которого не было в структуре с уникальными номерами
     */
    private int getRandNumber(Set<Integer> uniqueNumber) {
        int nextNumber;
        while (true) {
            nextNumber = RAND.nextInt(30) - 10;
            if (uniqueNumber.contains(nextNumber) == false) {
                uniqueNumber.add(nextNumber);
                break;
            }
        }
        return nextNumber;
    }

    /**
     * Проверка результатов перенумерации
     * В случае какого либо несоответствия кидаем RuntimeException
     */
    private void checkAssigning() {
        long actualOperationCount = elementExampleImplContext.getOperationCount();

        //Проверка на лишние операции
        if (actualOperationCount != expectOperationCount) {
            String message = initialOrder + String.format(". Expected %s, but get %s opertions", expectOperationCount, actualOperationCount);
            throw new RuntimeException(message);

        }

        //Проверка на правильный парядок номеров
        for (int index = 1; index < elements.size(); index++) {
            if (elements.get(index - 1).getNumber() > elements.get(index).getNumber()) {
                String message = initialOrder + String.format(". Wrong order: %s", toString());
                throw new RuntimeException(message);

            }
        }
    }
}
