package facultyofmusic.example.fieldinjection;

import facultyofmusic.example.SimpleUtilities;
import facultyofmusic.example.fieldinjection.FIRunner.RunWithPerson.Person;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(FIRunner.class)
public class FITest {

    @Rule @FIRunner.InjectPersonRule
    public FIRule runnerRule;

    @Test
    @FIRunner.RunWithPerson({Person.DANIEL})
    public void addNumbers() {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getFive();
        assert (result == 5);
    }

    @Test
    @FIRunner.RunWithPerson({
            Person.DANIEL,
            Person.STEFAN
    })
    public void subtractNumbers() throws Exception {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getTen();
        Thread.sleep(1000);
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
