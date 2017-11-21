package facultyofmusic.example;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

public class SimpleTestRunner extends Suite {
    public static final RunWithPerson.Person[] DEFAULT_PERSON = {RunWithPerson.Person.DANIEL};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface RunWithPerson {
        enum Person {
            DANIEL, STEFAN
        }

        Person[] value();
    }

    private List<Runner> runners;

    public SimpleTestRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
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

        public BSRunner(Class<?> klass, RunWithPerson.Person person) throws InitializationError {
            super(klass);
            this.person = person;
        }

        @Override
        protected List<FrameworkMethod> getChildren() {
            List<FrameworkMethod> methods = new ArrayList<>();
            for (FrameworkMethod m : computeTestMethods()) {
                if (isPersonRelevant(m)) {
                    methods.add(m);
                }
            }
            System.out.println(methods);
            return methods;
        }

        private boolean isPersonRelevant(FrameworkMethod method) {
            System.out.println("> " + method + " compatible?");
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
            return getTestClass().getName() + " [" + person + "]";
        }
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }
}
