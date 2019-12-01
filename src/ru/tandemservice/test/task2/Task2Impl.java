package ru.tandemservice.test.task2;

import ru.tandemservice.test.task2.application.Context;

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
 *
 * @author Smagin.K.V
 */
public class Task2Impl implements IElementNumberAssigner {

    // ваша реализация должна работать, как singleton. даже при использовании из нескольких потоков.
    public static final IElementNumberAssigner INSTANCE = new Task2Impl();
    private Map<IElement, IElement> replacementMap;
    private List<IElement> elements;
    private Integer temporaryNumber;

    @Override
    /**
     * Перенумерация элементов списка
     * @param elements - элементы, которым нужно выставить номера
     */
    public void assignNumbers(final List<IElement> elements) {
        // напишите здесь свою реализацию. Мы ждем от вас хорошо структурированного, документированного и понятного кода, работающего за разумное время.
        initialize(elements);
        exchangeElements();
    }

    /**
     * Наполняет внутренние структуры элементами, для удобства перенумерации
     *
     * @param elements элементы, которым нужно выставить номера
     */
    private void initialize(final List<IElement> elements) {

        this.elements = elements;
        IElement[] sortedElements = Context.getStructureConverter().getSortedArray(elements);
        replacementMap = Context.getStructureConverter().initReplacementMap(elements, sortedElements);
        temporaryNumber = calculateTemporaryNumber(sortedElements);
    }

    /**
     * Вычисление номера, который не встречается в заданном списке элементов ("временный номер")
     * Нужен для промежуточной вставки которая не вызывает дублирования номеров
     *
     * @param sortedElements - список из отсортированных элементов
     * @return - рассчетный номер который не встречается списке элементов
     */
    private Integer calculateTemporaryNumber(IElement[] sortedElements) {
        Integer tempNumber = null;
        for (int i = 0; i < sortedElements.length && tempNumber == null; i++) {
            if (i == 0 && sortedElements[i].getNumber() != Integer.MIN_VALUE)
                tempNumber = Integer.MIN_VALUE;
            else if (i > 0 && sortedElements[i].getNumber() != sortedElements[i - 1].getNumber() + 1)
                tempNumber = sortedElements[i - 1].getNumber() + 1;
        }
        if (tempNumber == null) {
            if (sortedElements.length > 0 && sortedElements[sortedElements.length - 1].getNumber() != Integer.MAX_VALUE)
                tempNumber = sortedElements[sortedElements.length - 1].getNumber() + 1;
            else
                throw new RuntimeException("Не удалось вычислить \"свободный\" номер для промежуточной вставки");
        }
        return tempNumber;
    }

    /**
     * Обходим массив, проверяем, находится ли элемент на правильном месте
     * Запуск обхода цепочки приводит к лишней записи из за "временного номера"
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
     *
     * @param collisionNumber - номер который стоял не на своем месте
     * @param element         - новое место для номера который стоял не на своем месте
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
