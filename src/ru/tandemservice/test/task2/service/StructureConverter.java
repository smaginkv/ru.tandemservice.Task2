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
public class StructureConverter {

    /**
     * формирование матрицы замен
     * @param elements - список элементов на основании которых нужно создать матрицу замен
     * @return - матрица замен
     */
    public Map<IElement, IElement> getReplacementMap(List<IElement> elements) {
        IElement[] sortedElements = getSortedArray(elements);
        return initReplacementMap(elements, sortedElements);
    }

    /**
     * Создание отсортированного массива элементов
     * @param elements - список элементов на основнии которого нужно создать упорядоченный массив элементов
     * @return - упорядоченный массив элементов
     */
    public IElement[] getSortedArray(List<IElement> elements) {
        IElement[] sortedElements = new IElement[elements.size()];
        elements.toArray(sortedElements);
        Arrays.sort(sortedElements);

        return sortedElements;
    }

    /**
     * формирование матрицы замен на основе отсортированного списка
     * @param elements - исходный список элементов
     * @param sortedElements - отсортированный массив элементов
     * @return - матрица замен
     */
    public Map<IElement, IElement> initReplacementMap(List<IElement> elements, IElement[] sortedElements) {
        Map<IElement, IElement> replacementMap = new HashMap<>();
        for (int i = 0; i < sortedElements.length; i++) {
            if (sortedElements[i] != elements.get(i)) {
                replacementMap.put(sortedElements[i], elements.get(i));
            }
        }
        return replacementMap;
    }
}