package ru.zencode.config;

import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

public class OptionsConfigurator {

    private final Class<?> clazz;

    public OptionsConfigurator(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Options fastAndDirtyMode() {

        return new OptionsBuilder()
                .include(clazz.getSimpleName())
                .timeUnit(TimeUnit.SECONDS)
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(5))
                .measurementIterations(1)
                .measurementTime(TimeValue.seconds(5))
                .forks(1)
                .build();
    }

    public Options defaultMode() {

        return new OptionsBuilder()
                .include(clazz.getSimpleName())
                .timeUnit(TimeUnit.SECONDS)
                .forks(5)
                .build();
    }
}
