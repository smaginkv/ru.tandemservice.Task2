package ru.tandemservice.test.task2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>Задание №2</h1> Реализуйте интерфейс {@link IElementNumberAssigner}.
 *
 * <p>
 * Помимо качества кода, мы будем обращать внимание на оптимальность
 * предложенного алгоритма по времени работы с учетом скорости выполнения
 * операции присвоения номера: большим плюсом (хотя это и не обязательно) будет
 * оценка числа операций, доказательство оптимальности или указание области, в
 * которой алгоритм будет оптимальным.
 * </p>
 */
public class Task2Impl implements IElementNumberAssigner {

    public static final IElementNumberAssigner INSTANCE = new Task2Impl();
    private List<IElement> elements;
    private Map<Integer, Integer> collisions;
    private int expectOperationCount = 0;

    /**
     * Assign number to elements according to their order in the collection 
     */
    @Override
    public synchronized void assignNumbers(final List<IElement> elements) {
        this.elements = elements;
        this.expectOperationCount = 0;

        // 1 stage
        populateMapCollisions();

        // 2 stage
        resolveSimpleCollisions();

        // 3 stage
        resolveCyclicDependantCollisions();
    }

    /**
     * @return estimated number of setupNumber operations
     */
    public int getExpectOperationCount() {
        return expectOperationCount;
    }

    /**
     * Populate map of collisions. 
     * Collision - duplication of numbers that occurs when an element is assigned a number in order
     */
    private void populateMapCollisions() {
        collisions = new ConcurrentHashMap<Integer, Integer>();
        for (int index = 0; index < elements.size(); index++) {
            int elementNumber = elements.get(index).getNumber();

            if (elementNumber != index) {
                expectOperationCount++;
            }

            if (elementNumber != index && elementNumber >= 0 && elementNumber < elements.size()) {
                collisions.put(elementNumber, index);
            }
        }
    }

    /**
     * Simple collision - to resolve that you havn't use extra setupNumber operations {0,17,1}->{0,17,2}->{0,1,2} 
     */
    private void resolveSimpleCollisions() {
        for (int index = 0; index < elements.size(); index++) {
            if (collisions.containsKey(index) == false && elements.get(index).getNumber() != index) {
                releaseNumberByIndex(index);
            }
        }
    }

    /**
     * Releasing the current element number will resolve a simple collision for another element.
     * @param nextIndex - current number, maybe index of next simple collision
     */
    private void releaseNumberByIndex(final int nextIndex) {
        IElement element = elements.get(nextIndex);
        int nextNumber = element.getNumber();

        element.setupNumber(nextIndex);
        if (collisions.remove(nextNumber) != null) {
            releaseNumberByIndex(nextNumber);
        }
    }

    /**
     * Cyclic dependant - to resolve that you have to use extra setupNumber operations
     * {1,2,0}-> extra operation {-1,2,0}->{-1,1,0}->{-1,1,2}->{0,1,2}
     */
    private void resolveCyclicDependantCollisions() {
        while (true) {
            Iterator<Entry<Integer, Integer>> collisionsIterator = collisions.entrySet().iterator();
            if (!collisionsIterator.hasNext()) {
                break;
            }
            Entry<Integer, Integer> checkedEntry = collisionsIterator.next();

            // in collisions key - number, value - position
            int currentPosition = checkedEntry.getValue();

            // we have to break cyclic dependence, release first element in cyclic dependency
            // at this stage we cannot have elements with a negative number, it's difficult
            // to imagine an array with negative index!
            elements.get(currentPosition).setupNumber(-1);

            // at now, we have to recursively bypass whole collision and set the
            // corresponding number
            releaseNumberByIndex(checkedEntry.getKey());
            
            //Release first element in cyclic dependency 
            collisions.remove(checkedEntry.getKey());

            expectOperationCount++;
        }
    }
}
