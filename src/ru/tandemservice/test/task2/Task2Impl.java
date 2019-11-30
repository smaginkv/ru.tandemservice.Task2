package ru.tandemservice.test.task2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>Задание №2</h1>
 * Реализуйте интерфейс {@link IElementNumberAssigner}.
 *
 * <p>Помимо качества кода, мы будем обращать внимание на оптимальность предложенного алгоритма по времени работы
 * с учетом скорости выполнения операции присвоения номера:
 * большим плюсом (хотя это и не обязательно) будет оценка числа операций, доказательство оптимальности
 * или указание области, в которой алгоритм будет оптимальным.</p>
 */
public class Task2Impl implements IElementNumberAssigner {

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IElementNumberAssigner INSTANCE = new Task2Impl();
    private HashMap<Integer, Integer> sortedNumbers;
    private List<IElement> elements;
    private IElement[] sortedElements;

    private int expectOperationCount = 0;//убрать в сервисную сущность

    @Override
    public void assignNumbers(final List<IElement> elements) {
        // напишите здесь свою реализацию. Мы ждем от вас хорошо структурированного, документированного и понятного кода, работающего за разумное время.
        this.elements = elements;
        this.expectOperationCount = 0;//todo убрать в сервисную сущность

        //todo скинуть в отдельную функцию
        IElement[] sortedElements = new IElement[elements.size()];
        elements.toArray(sortedElements);
        Arrays.sort(sortedElements);

        sortedNumbers = new HashMap();
        for (int i = 0; i < sortedElements.length; i++) {
            sortedNumbers.put(sortedElements[i].getNumber(), i);
        }

        findFirstCollision();

    }

    private void findFirstCollision() {
        for (int i = 0; i < elements.size(); i++) {

            //Тут мы можем использовать ссылки, потому что если ссылки разные то и номера будут разными
            if (elements.get(i) != sortedElements[i]) {
                int collisionNumber = sortedElements[i].getNumber();
                sortedElements[i].setupNumber(0);
                exchangeNumber(collisionNumber);
                break;
            }
        }
    }

    private void exchangeNumber(int collisionNumber) {
        Integer index = sortedNumbers.get(collisionNumber);
        if (index == null) {
            return;
        }
        IElement element = elements.get(index);

        int nextCollisionNumber = element.getNumber();
        element.setupNumber(collisionNumber);
        exchangeNumber(nextCollisionNumber);

    }
}
