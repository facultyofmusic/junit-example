package facultyofmusic.example.fieldinjection;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class FIRule implements TestRule {
    private final String whoIsRunning;

    public FIRule(String whoIsRunning) {
        this.whoIsRunning = whoIsRunning;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("Running with " + whoIsRunning);
                base.evaluate();
            }
        };
    }
}
