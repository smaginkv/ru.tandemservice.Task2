## Task text

The method sets numbers <code>IElement#setupNumber(int)</code> for elements in the collection in the order of the elements. Initially, the collection does not contain elements whose numbers are repeated. The following conditions are provided:

• the method only works with existing elements (does not create new ones),

• throughout the work of the method, the uniqueness of element numbers is ensured: call <code>element.setNumber(i)</code> is allowed <code>⇔ ∀ e ∊ elements (e.number ≠ i)</code>,

• the method is resistant to passing into it as a parameter <code>java.util.Collections.unmodifiableList (List)</code> and any other implementation of immutable-list,

• the method should work in an “acceptable” time (<code>IElement.setupNumber (int)</code> is a labor-intensive operation and should be used rationally)

## Using it
Run the command line, go to the project folder and create bin folder
Compile..

    javac -sourcepath src -d bin src/ru/tandemservice/test/task2/testing/Task2ImplTest.java

Run bytecode

    java -cp bin ru.tandemservice.test.task2.testing.Task2ImplTest
        