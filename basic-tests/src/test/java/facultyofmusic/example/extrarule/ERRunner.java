package facultyofmusic.example.extrarule;

import org.junit.rules.TestRule;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.*;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;

public class ERRunner extends Suite {
    public static final RunWithPerson.Person[] DEFAULT_PERSON = {RunWithPerson.Person.DANIEL};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface RunWithPerson {
        enum Person {
            DANIEL, STEFAN
        }

        Person[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface InjectPersonTestRule {
    }

    private List<Runner> runners;

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    public ERRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, Collections.emptyList());

        Set<RunWithPerson.Person> relevantPeople = new TreeSet<>();
        for (Method m : klass.getMethods()) {
            RunWithPerson annotation = m.getAnnotation(RunWithPerson.class);
            if (annotation != null) relevantPeople.addAll(Arrays.asList(annotation.value()));
        }

        System.out.println("Relevant people for this test: " + relevantPeople);

        runners = new ArrayList<>();
        for (RunWithPerson.Person b : relevantPeople) {
            System.out.println("Making runner for " + b);
            runners.add(new BSRunner(klass, b));
        }
    }


    private static class BSRunner extends BlockJUnit4ClassRunner {
        private final RunWithPerson.Person person;
        private final String name;

        public BSRunner(Class<?> klass, RunWithPerson.Person person) throws InitializationError {
            super(klass);
            this.person = person;

            this.name = "[" + person + "]";
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            List<FrameworkMethod> methods = new ArrayList<>();
            for (FrameworkMethod m : computeTestMethods()) {
                if (isPersonRelevant(m)) {
                    methods.add(m);
                }
            }
            return methods;
        }

        private boolean isPersonRelevant(FrameworkMethod method) {
            RunWithPerson annotation = method.getAnnotation(RunWithPerson.class);
            if (annotation == null || annotation.value().length == 0)
                return Arrays.asList(DEFAULT_PERSON).contains(person);

            for (RunWithPerson.Person b : annotation.value()) {
                if (person.equals(b)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected String getName() {
            return "[" + person + "]";
        }

        @Override
        protected String testName(FrameworkMethod method) {
            return method.getName() + getName();
        }

        @Override
        protected Statement methodInvoker(FrameworkMethod method, Object test) {
            List<TestRule> rules = getTestRules(test);
            for(TestRule rule : rules) {
                if (rule instanceof ERPersonReceiver) {
                    ((ERPersonReceiver) rule).runnerAvailable(person);
                }
            }

            return super.methodInvoker(method, test);
        }

        @Override
        protected Statement classBlock(RunNotifier notifier) {
            return childrenInvoker(notifier);
        }

        @Override
        protected Annotation[] getRunnerAnnotations() {
            return new Annotation[0];
        }

        @Override
        protected Object createTest() throws Exception {
            Object test = super.createTest();
            return test;
        }

        private FrameworkField getSimpleTestRule() {
            List<FrameworkField> personRuleFields = getTestClass().getAnnotatedFields(InjectPersonTestRule.class);
            if (personRuleFields.size() != 1) {
                System.err.println("There must be exactly 1 InjectPersonRule field per test class!");
            }
            FrameworkField field = personRuleFields.get(0);
            if (field.getType() != ERRule.class) {
                System.err.println("InjectPersonRule can only be used with ERRule!");
            }
            return field;
        }
    }
}
