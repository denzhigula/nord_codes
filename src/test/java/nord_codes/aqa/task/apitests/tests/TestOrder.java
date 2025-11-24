package nord_codes.aqa.task.apitests.tests;

import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;

import java.util.Comparator;

public class TestOrder implements MethodOrderer {
    @Override
    public void orderMethods(MethodOrdererContext context) {
        context.getMethodDescriptors().sort(
                Comparator.comparing((MethodDescriptor m) -> Integer.valueOf(m.getDisplayName().split(": ")[0].substring(3))));
    }
}
