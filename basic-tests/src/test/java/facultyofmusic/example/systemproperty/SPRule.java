package facultyofmusic.example.systemproperty;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SPRule implements TestRule {
    private final String whoIsRunning;

    public SPRule() {
        whoIsRunning = System.getProperty("who-is-running");
        if (whoIsRunning == null) {
            System.err.println("COULD NOT GET PROPERTY!!");
        }
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
