package facultyofmusic.example.extrarule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ERRule implements TestRule, ERPersonReceiver {
    private ERRunner.RunWithPerson.Person who = null;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("Running with " + who);
                base.evaluate();
            }
        };
    }

    @Override
    public void runnerAvailable(ERRunner.RunWithPerson.Person whoIsRunning) {
        System.out.println("receiving person to run with: " + whoIsRunning);
        this.who = whoIsRunning;
    }
}
