package ru.zencode;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import ru.zencode.config.OptionsConfigurator;

@State(Scope.Thread)
public class FactorialBenchmark {

    @Param({"10", "20"})
    private int n;

    @Benchmark
    public void loopFactorial(Blackhole bh) {
        bh.consume(loopFactorial(n));
    }

    private long loopFactorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    @Benchmark
    public void recursiveFactorial(Blackhole bh) {
        bh.consume(recursiveFactorial(n));
    }

    private long recursiveFactorial(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * recursiveFactorial(n - 1);
        }
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsConfigurator(FactorialBenchmark.class).fastAndDirtyMode()).run();
    }
}
