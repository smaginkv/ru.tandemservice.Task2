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

	@Override
	public synchronized void assignNumbers(final List<IElement> elements) {
		this.elements = elements;
		this.expectOperationCount = 0;
		
		// 1 round.
		populateMapCollisions();

		// 2 Round. Resolve non-transitive collisions
		resolveNonTransitiveCollisions();

		// 3 round. Resolve transitive collisions
		closeTransitiveCollision();
	}

	public int getExpectOperationCount() {
		return expectOperationCount;
	}

	private void populateMapCollisions() {
		collisions = new ConcurrentHashMap<Integer, Integer>();
		int index = -1;
		for (IElement element : elements) {
			int elementNumber = element.getNumber();

			if (elementNumber != ++index) {
				expectOperationCount++;
			}

			if (elementNumber != index && elementNumber >= 0 && elementNumber < elements.size()) {
				collisions.put(elementNumber, index);
			}
		}
	}

	private void resolveNonTransitiveCollisions() {
		int index = -1;
		for (IElement element : elements) {
			if (collisions.containsKey(++index) == false && element.getNumber() != index) {
				// To free number in index freeNumber recursively
				processFreeNumber(index);
			}
		}
	}

	private void processFreeNumber(int freeIndex) {
		IElement element = elements.get(freeIndex);
		int freeNumber = element.getNumber();// 0

		element.setupNumber(freeIndex);
		if (collisions.remove(freeNumber) != null) {
			processFreeNumber(freeNumber);
		}
	}

	private void resolveResolveCollisionsOnStep(int key) {
		collisions.remove(key);
		int checkedValue = elements.get(key).getNumber();

		elements.get(key).setupNumber(key);

		if (checkedValue > 0) {
			resolveResolveCollisionsOnStep(checkedValue);
		}
	}

	private void closeTransitiveCollision() {

		int step = 0;

		while (true) {
			Iterator<Entry<Integer, Integer>> collisionsIterator = collisions.entrySet().iterator();

			if (!collisionsIterator.hasNext()) {
				break;
			}

			Entry<Integer, Integer> checkedEntry = collisionsIterator.next();
			int indexCurrentElement = checkedEntry.getValue();
			elements.get(indexCurrentElement).setupNumber(++step * -1);
			resolveResolveCollisionsOnStep(checkedEntry.getKey());

			expectOperationCount++;
		}
	}
}
