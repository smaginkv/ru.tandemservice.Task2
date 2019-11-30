package ru.tandemservice.test.task2;

import ru.tandemservice.test.task2.application.Context;
import ru.tandemservice.test.task2.service.PerformAndSet;

import java.util.List;
import java.util.Map;

/**
 * <h1>Задание №2</h1>
 * Реализуйте интерфейс {@link IElementNumberAssigner}.
 *
 * <p>Помимо качества кода, мы будем обращать внимание на оптимальность предложенного алгоритма по времени работы
 * с учетом скорости выполнения операции присвоения номера:
 * большим плюсом (хотя это и не обязательно) будет оценка числа операций, доказательство оптимальности
 * или указание области, в которой алгоритм будет оптимальным.</p>
 * @author Smagin.K.V
 */
public class Task2Impl implements IElementNumberAssigner {

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IElementNumberAssigner INSTANCE = new Task2Impl();
    private Map<IElement, IElement> replacementMap;
    private List<IElement> elements;
    private Integer temporaryNumber;

    public void setTemporaryNumber(Integer temporaryNumber) {
        this.temporaryNumber = temporaryNumber;
    }

    @Override
    public void assignNumbers(final List<IElement> elements) {
        // напишите здесь свою реализацию. Мы ждем от вас хорошо структурированного, документированного и понятного кода, работающего за разумное время.
        initialize(elements);
        exchangeElements();
    }

    /**
     * Наолняет внутренние структуры элементами, для удобства перенумерации
     *
     * @param elements элементы, которым нужно выставить номера
     */
    private void initialize(final List<IElement> elements) {

        this.elements = elements;
        temporaryNumber = null;

        //Лямбды исключительно ради фана, с нового года перехожу на проект java8, потихоньку готовлюсь
        PerformAndSet<IElement[], Object> setTemporaryNumber = (sortedElements, caller) -> {
            Integer temporaryNumber = null;
            for (int i = 0; i < sortedElements.length; i++) {
                if (i == 0) {
                    if (sortedElements[i].getNumber() != Integer.MIN_VALUE) {
                        temporaryNumber = Integer.MIN_VALUE;
                        break;
                    }
                } else {
                    if (sortedElements[i].getNumber() != sortedElements[i - 1].getNumber() + 1) {
                        temporaryNumber = sortedElements[i - 1].getNumber() + 1;
                    }
                }
            }
            ((Task2Impl) caller).setTemporaryNumber(temporaryNumber);
        };

        //Получаем мапу, в которой указано какой элемент на какой будет заменяться
        //temporaryNumber - Это номер который нам придется испльзовать чтобы не задвоились номера
        //3:2:1->4:2:1->4:2:3->1:2:3 в данном примере временный номер = 4
        replacementMap = Context.getMapReplacement().get(elements, this, setTemporaryNumber);
    }

    /**
     * Обходим массив, проверяем, находится ли элемент на правильном месте
     * Запуск обхода цепочки приводит к лишней записи (временный номер)
     */
    private void exchangeElements() {
        elements.forEach(element -> {
            if (replacementMap.containsKey(element)) {
                exchangeElement(temporaryNumber, element);
            }
        });
    }

    /**
     * Рекурсивно вызываем замену номера, до тех пор пока не пройдем всю цепочку
     * @param collisionNumber - номер который стоял не на своем месте
     * @param element - новое место для номера который стоял не на своем месте
     */
    private void exchangeElement(int collisionNumber, IElement element) {
        if (element == null) {
            return;
        }

        int nextCollisionNumber = element.getNumber();
        element.setupNumber(collisionNumber);
        exchangeElement(nextCollisionNumber, replacementMap.remove(element));
    }
}
