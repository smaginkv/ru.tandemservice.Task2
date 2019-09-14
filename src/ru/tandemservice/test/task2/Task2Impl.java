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

		// 1 stage
		populateMapCollisions();

		// 2 stage
		resolveNonDependantCollisions();

		// 3 stage
		resolveCyclicDependantCollisions();
	}

	public int getExpectOperationCount() {
		return expectOperationCount;
	}

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

	private void resolveNonDependantCollisions() {
		for (int index = 0; index < elements.size(); index++) {
			if (collisions.containsKey(index) == false && elements.get(index).getNumber() != index) {
				freeNumber(index);
			}
		}
	}

	private void freeNumber(int freeIndex) {
		IElement element = elements.get(freeIndex);
		int freeNumber = element.getNumber();// 0

		element.setupNumber(freeIndex);
		if (collisions.remove(freeNumber) != null) {
			freeNumber(freeNumber);
		}
	}

	private void resolveCyclicDependantCollisions() {
		while (true) {
			Iterator<Entry<Integer, Integer>> collisionsIterator = collisions.entrySet().iterator();
			if (!collisionsIterator.hasNext()) {
				break;
			}
			Entry<Integer, Integer> checkedEntry = collisionsIterator.next();

			// in collisions key - number, value - position
			int currentPosition = checkedEntry.getValue();

			// we have to break cyclic dependence
			// at this stage we cannot have elements with a negative number, it's difficult
			// to imagine an array with negative index!
			elements.get(currentPosition).setupNumber(-1);

			// at now, we have to recursively bypass whole collision and set the
			// corresponding number
			resolveNextCollisionElement(checkedEntry.getKey());

			expectOperationCount++;
		}
	}

	private void resolveNextCollisionElement(int currentPosition) {
		collisions.remove(currentPosition);
		int currentNumber = elements.get(currentPosition).getNumber();

		elements.get(currentPosition).setupNumber(currentPosition);

		// we have reached the end of collision
		if (currentNumber > 0) {
			resolveNextCollisionElement(currentNumber);
		}
	}
}
