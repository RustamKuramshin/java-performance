package ru.zencode;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import ru.zencode.config.OptionsConfigurator;

import java.util.Arrays;
import java.util.Random;

@State(Scope.Thread)
public class SortBenchmark {

    @Param({"1000", "10000", "100000"})
    private int size;

    private int[] array;

    @Setup
    public void setup() {
        array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt();
        }
    }

    @Benchmark
    public void standardSort() {
        int[] copy = Arrays.copyOf(array, array.length);
        Arrays.sort(copy);
    }

    @Benchmark
    public void parallelSort() {
        int[] copy = Arrays.copyOf(array, array.length);
        Arrays.parallelSort(copy);
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsConfigurator(SortBenchmark.class).fastAndDirtyMode()).run();
    }
}