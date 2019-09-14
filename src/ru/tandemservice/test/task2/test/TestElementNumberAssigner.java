package ru.tandemservice.test.task2.test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.tandemservice.test.task2.ElementExampleImpl;
import ru.tandemservice.test.task2.IElement;
import ru.tandemservice.test.task2.IElementNumberAssigner;
import ru.tandemservice.test.task2.Task2Impl;

public class TestElementNumberAssigner {
	private final static Random RAND = new Random(48);
	private static TestElementNumberAssigner instance;
	private List<IElement> list;
	private String initialOrder;
	private ElementExampleImpl.Context elementExampleImplContext;

	private TestElementNumberAssigner() {
	};

	public static TestElementNumberAssigner getInstance() {
		if (instance == null) {
			instance = new TestElementNumberAssigner();
		}
		return instance;
	}

	public void test() {
		IElementNumberAssigner task = Task2Impl.INSTANCE;
		elementExampleImplContext = new ElementExampleImpl.Context();

		IElement[] array = getRandArray();

		list = Arrays.asList(array);

		initialOrder = IElementToString();
		task.assignNumbers(list);
		
		checkAssigning(task);

	}

	private String IElementToString() {
		StringBuilder str = new StringBuilder("Initial order: ");
		for (IElement element : list) {
			if (list.indexOf(element) > 0) {
				str.append(", ");
			}
			str.append(element.getNumber());
		}
		return str.toString();
	}

	private IElement[] getRandArray() {
		// from 5 to 10
		int arrayLength = RAND.nextInt(5) + 5;
		IElement[] array = new IElement[arrayLength];
		populateArray(array);
		return array;
	}

	private void populateArray(IElement[] array) {
		Map<Integer, Integer> uniqueNumber = new HashMap<Integer, Integer>();
		for (int i = 0; i < array.length; i++) {
			int nextNumber = 0;
			while (true) {
				nextNumber = RAND.nextInt(30)-10;
				if (uniqueNumber.containsKey(nextNumber) == false) {
					uniqueNumber.put(nextNumber, 0);
					break;
				}
			}
			array[i] = new ElementExampleImpl(elementExampleImplContext, nextNumber);
		}
	}

	private void checkAssigning(IElementNumberAssigner task) {

		if (task instanceof Task2Impl) {
			int expected = ((Task2Impl) task).getExpectOperationCount();
			int real = elementExampleImplContext.getOperationCount();
			if (real != expected) {
				throw new RuntimeException(
						initialOrder + String.format(". Expected count operation %s, but real %s", expected, real));
			}
		}

		int expect = 0;
		for (IElement element : list) {
			if (element.getNumber() != expect++) {
				throw new RuntimeException(initialOrder + String.format(". Wrong order: %s", IElementToString()));
			}
		}
	}
}
