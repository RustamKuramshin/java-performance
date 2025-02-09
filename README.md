# Исследование производительности Java-кода с помощью микробенчмарков на JMH

Читать о Java Microbenchmark Harness (JMH) здесь -> [https://github.com/openjdk/jmh](https://github.com/openjdk/jmh)

Хорошая литература по производительности JVM:

- Скотт Оукс **"Эффективный Java. Тюнинг кода на Java 8, 11 и дальше"**

- Эванс, Гоф, Ньюланд **"Java. Оптимизация программ. Практические методы повышения производительности приложений в JVM"**

### Запуск бенчмарков

Перед запуском каждого бенчмарка выполнить в терминале:
```shell
mvn clean install
java -jar target/benchmarks.jar [имя класса бенчмарка]
```

### Интерпретация результатов

Результаты бенчмарков JMH обычно содержат несколько ключевых столбцов, каждый из которых предоставляет важную информацию.

- **Benchmark**: Это имя теста. В вашем случае, FactorialBenchmark.loopFactorial и FactorialBenchmark.recursiveFactorial указывают на два разных бенчмарка: один с циклической реализацией факториала, а другой с рекурсивной.

(n): Это параметр, который вы передали в бенчмарк. В вашем случае, это значение n для факториала. Значения 10 и 20 показывают, что бенчмарк был выполнен для факториалов этих чисел.

Mode: Режим, в котором выполнялся бенчмарк. thrpt означает "throughput", т.е. пропускная способность. Это мера того, сколько операций в секунду было выполнено (операций в секунду).

Cnt: Количество итераций (measurements) бенчмарка, которые были выполнены.

Score: Среднее значение пропускной способности для всех итераций. Это основной показатель производительности. В вашем случае, он показывает, сколько раз метод факториала может быть выполнен за секунду. Например, для loopFactorial с n = 10 ваш код может выполняться примерно 236 миллионов раз в секунду.

Error: Погрешность (стандартное отклонение) измерений. Она показывает, насколько стабильны были результаты между различными итерациями бенчмарка. Меньшая ошибка указывает на более стабильные результаты.

Units: Единицы измерения для показателя Score. В вашем случае, это ops/s, то есть операции в секунду.

Интерпретация Результатов

Пропускная Способность: Чем выше значение в столбце Score, тем лучше производительность метода. В вашем случае, циклическая реализация (loopFactorial) оказалась быстрее рекурсивной (recursiveFactorial) для обоих значений n.

Стабильность: Меньшая погрешность (Error) указывает на более предсказуемую и стабильную производительность.

Влияние Размера Задачи: Вы также можете заметить, что производительность обоих методов снижается с увеличением n. Это ожидаемо, так как большие значения n требуют больше вычислений для расчета факториала.

Эти результаты помогают понять, как различные реализации алгоритмов и изменение входных данных могут влиять на производительность в Java.
