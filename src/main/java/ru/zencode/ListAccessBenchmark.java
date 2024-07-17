package ru.zencode;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import ru.zencode.config.OptionsConfigurator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@State(Scope.Benchmark)
public class ListAccessBenchmark {

    @Param({"10000", "100000", "1000000"})
    private int size;

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private Random random;

    @Setup
    public void setup() {
        random = new Random();
        arrayList = new ArrayList<>(size);
        linkedList = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            int value = random.nextInt();
            arrayList.add(value);
            linkedList.add(value);
        }
    }

    @Benchmark
    public void accessArrayList(Blackhole bh) {
        bh.consume(arrayList.get(size/2));
    }

    @Benchmark
    public void accessLinkedList(Blackhole bh) {
        bh.consume(linkedList.get(size/2));
    }

    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsConfigurator(ListAccessBenchmark.class).fastAndDirtyMode()).run();
    }
}