package facultyofmusic.example.extrarule;

import facultyofmusic.example.SimpleUtilities;
import facultyofmusic.example.extrarule.ERRunner.RunWithPerson.Person;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(ERRunner.class)
public class ERTest {
    @Rule
    public ERRule testRule = new ERRule();

    @Test
    @ERRunner.RunWithPerson({Person.DANIEL})
    public void addNumbers() {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getFive();
        assert (result == 5);
    }

    @Test
    @ERRunner.RunWithPerson({
            Person.DANIEL,
            Person.STEFAN
    })
    public void subtractNumbers() throws Exception {
        SimpleUtilities simple = new SimpleUtilities();
        int result = simple.getTen();
        Thread.sleep(1000);
        assert (result == 10);
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
