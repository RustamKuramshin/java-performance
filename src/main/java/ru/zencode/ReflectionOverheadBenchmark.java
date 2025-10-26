package ru.zencode;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgsAppend = {"-XX:+UnlockDiagnosticVMOptions"})
@State(Scope.Thread)
public class ReflectionOverheadBenchmark {

    public static class Target {
        public int state = 1;

        public int work(int a, int b) {
            return a + b + state;
        }

        public Target() {
        }

        public Target(int s) {
            this.state = s;
        }
    }

    private Target target;

    private Method publicMethod_checked;
    private Method publicMethod_accessible;
    private MethodHandle mh_findVirtual;
    private MethodHandle mh_unreflect;

    private Field field_checked;
    private Field field_accessible;
    private VarHandle vh_state;

    private Constructor<Target> ctor_checked;
    private Constructor<Target> ctor_accessible;
    private MethodHandle mh_ctor_find;
    private MethodHandle mh_ctor_unreflect;

    @Setup(Level.Trial)
    public void setup() throws Throwable {
        target = new Target();

        publicMethod_checked = Target.class.getMethod("work", int.class, int.class);
        publicMethod_accessible = Target.class.getMethod("work", int.class, int.class);
        publicMethod_accessible.setAccessible(true);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        mh_findVirtual = lookup.findVirtual(Target.class, "work",
                MethodType.methodType(int.class, int.class, int.class));
        mh_unreflect = lookup.unreflect(publicMethod_accessible);

        field_checked = Target.class.getField("state");
        field_accessible = Target.class.getField("state");
        field_accessible.setAccessible(true);

        vh_state = MethodHandles.lookup().findVarHandle(Target.class, "state", int.class);

        ctor_checked = Target.class.getConstructor(int.class);
        ctor_accessible = Target.class.getConstructor(int.class);
        ctor_accessible.setAccessible(true);

        mh_ctor_find = lookup.findConstructor(Target.class,
                MethodType.methodType(void.class, int.class));
        mh_ctor_unreflect = lookup.unreflectConstructor(ctor_accessible);
    }

    @Benchmark
    public int M01_direct_call() {
        return target.work(1, 2);
    }

    @Benchmark
    public Object M02_reflection_call_accessible() throws Exception {
        return publicMethod_accessible.invoke(target, 1, 2);
    }

    @Benchmark
    public Object M03_reflection_call_checked() throws Exception {
        return publicMethod_checked.invoke(target, 1, 2);
    }

    @Benchmark
    public int M04_methodHandle_findVirtual_invokeExact() throws Throwable {
        return (int) mh_findVirtual.invokeExact(target, 1, 2);
    }

    @Benchmark
    public int M05_methodHandle_unreflect_invokeExact() throws Throwable {
        return (int) mh_unreflect.invokeExact(target, 1, 2);
    }

    @Benchmark
    public int F01_field_direct_getset(Blackhole bh) {
        target.state = 10;
        int v = target.state;
        bh.consume(v);
        return v;
    }

    @Benchmark
    public int F02_field_reflection_accessible_getset(Blackhole bh) throws Exception {
        field_accessible.setInt(target, 10);
        int v = field_accessible.getInt(target);
        bh.consume(v);
        return v;
    }

    @Benchmark
    public int F03_field_reflection_checked_getset(Blackhole bh) throws Exception {
        field_checked.setInt(target, 10);
        int v = field_checked.getInt(target);
        bh.consume(v);
        return v;
    }

    @Benchmark
    public int F04_field_varHandle_getset(Blackhole bh) {
        vh_state.set(target, 10);
        int v = (int) vh_state.get(target);
        bh.consume(v);
        return v;
    }

    @Benchmark
    public Target C01_ctor_direct_new() {
        return new Target(42);
    }

    @Benchmark
    public Object C02_ctor_reflection_accessible() throws Exception {
        return ctor_accessible.newInstance(42);
    }

    @Benchmark
    public Object C03_ctor_reflection_checked() throws Exception {
        return ctor_checked.newInstance(42);
    }

    @Benchmark
    public Object C04_ctor_methodHandle_find_invokeExact() throws Throwable {
        return (Target) mh_ctor_find.invokeExact(42);
    }

    @Benchmark
    public Object C05_ctor_methodHandle_unreflect_invokeExact() throws Throwable {
        return (Target) mh_ctor_unreflect.invokeExact(42);
    }
}
