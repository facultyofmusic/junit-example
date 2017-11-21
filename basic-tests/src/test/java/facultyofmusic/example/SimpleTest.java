package facultyofmusic.example;

import facultyofmusic.example.SimpleTestRunner.RunWithPerson.Person;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(SimpleTestRunner.class)
public class SimpleTest {
    @Test
    @SimpleTestRunner.RunWithPerson({Person.DANIEL})
    public void addNumbers() {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getFive();
        assert (result == 5);
    }

    @Test
    @SimpleTestRunner.RunWithPerson({
            Person.DANIEL,
            Person.STEFAN
    })
    public void subtractNumbers() throws Exception {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getTen();
        Thread.sleep(10000);
        assert (result == 5);
    }

    @Test
    public void exceptionallyDank() {
        SimpleUtilities simple = new SimpleUtilities();
        try {
            simple.throwException();
            fail("NOT DANK ENOUGH.");
        } catch (IllegalArgumentException e) {
            //YAY!
        }
    }
}
