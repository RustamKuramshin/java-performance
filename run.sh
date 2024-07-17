#!/usr/bin/env bash

mvn clean install && \
java -jar target/benchmarks.jar FactorialBenchmark