package ru.tandemservice.test.task2;
import ru.tandemservice.test.task2.exception.AssignerException;
import ru.tandemservice.test.task2.test.TestElementNumberAssigner;

public class Main {

	public static void main(String[] args) throws AssignerException {
		int index = 0;
		TestElementNumberAssigner testElementNumberAssigner = TestElementNumberAssigner.getInstance();
		while (++index < 1000000) {			
			testElementNumberAssigner.test();
		}
		System.out.printf("%s test operations finished", index);
	}	
}
