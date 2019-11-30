package ru.tandemservice.test.task2.service;

import ru.tandemservice.test.task2.IElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервисные функции по формированию матрицы замен номеров
 * @author Smagin.K.V
 */
public class MapReplacement {

    /**
     * api для формирования матрицы без лямбд
     * @param elements - список элементов на основании которых нужно создать матрицу замен
     * @return - матрица замен
     */
    public Map<IElement, IElement> get(List<IElement> elements) {
        IElement[] sortedElements = getSortedArray(elements);
        return initReplacementMap(elements, sortedElements);
    }

    /**
     * api для формирования матрицы с лямбдой
     * @param elements - список элементов на основании которых нужно создать матрицу замен
     * @param caller - вызывающий экземпляр, в который надо инжектить данные
     * @param lambda - метод который определяет какие данные и куда нужно инжектить
     * @return - матрица замен
     */
    public Map<IElement, IElement> get(List<IElement> elements, Object caller, PerformAndSet<IElement[], Object> lambda) {
        IElement[] sortedElements = getSortedArray(elements);
        lambda.apply(sortedElements, caller);
        return initReplacementMap(elements, sortedElements);
    }

    /**
     * Создание отсортированного массива элементов
     * @param elements - список элементов на основнии которого нужно создать упорядоченный массив элементов
     * @return - упорядоченный массив элементов
     */
    private IElement[] getSortedArray(List<IElement> elements) {
        IElement[] sortedElements = new IElement[elements.size()];
        elements.toArray(sortedElements);
        Arrays.sort(sortedElements);

        return sortedElements;
    }

    /**
     * формирование матрицы замен
     * @param elements - исходный список элементов
     * @param sortedElements - отсортированный массив элементов
     * @return - матрица замен
     */
    private Map<IElement, IElement> initReplacementMap(List<IElement> elements, IElement[] sortedElements) {
        Map<IElement, IElement> replacementMap = new HashMap<>();
        for (int i = 0; i < sortedElements.length; i++) {
            if (sortedElements[i] != elements.get(i)) {
                replacementMap.put(sortedElements[i], elements.get(i));
            }
        }
        return replacementMap;
    }
}