# Assignment 2 — Design and Analysis of Algorithms

A simple Java project with DynamicArray, MyLinkedList and MinHeap.
The structures store primitive int values and count operations with Metrics.

## Requirements

- JDK 25 or newer (pom.xml compiles for Java 25).
- Maven, or the local Maven in .tools.
- Python 3 and matplotlib for plots. No pandas or seaborn is needed.

Run all commands from the project root.

## Build

```powershell
mvn compile
```

## Run tests

```powershell
mvn test
```

JUnit reports are saved in target/surefire-reports.
The tests cover empty structures, one element, duplicates, boundary indexes,
invalid indexes, random correctness and heap order after insertion and extraction.

## Local Maven alternative

If mvn is not available on PATH:

```powershell
& .\.tools\apache-maven-3.9.9\bin\mvn.cmd '-Dmaven.repo.local=.tools/repository' compile
& .\.tools\apache-maven-3.9.9\bin\mvn.cmd '-Dmaven.repo.local=.tools/repository' test
```

## Run benchmark

Build first, then run:

```powershell
java -cp target/classes benchmark.Benchmark
```

The benchmark writes results/results.csv with this header:

```text
workload,variant,structure,n,time_ms,steps,moves,comparisons
```

There are 36 rows: nine cases for each size 100, 1000, 10000 and 100000.
Each case has two warm-up runs and five measured runs.
Only the five measured times are sorted; the middle value is the median.
System.nanoTime() measures time, and division by 1000000 converts it to milliseconds.

For each n, one int[] is generated with new Random(42) and values in 0..n-1.
W1, W2 and both W3 variants fill the array and list with this same dataset.
Random access indexes and search queries are also reproducible.

| Workload | Operations |
|---|---|
| W1 | 10000 random get calls |
| W2 | 1000 contains calls: 500 values chosen from the dataset, 500 values in n..2n-1 |
| W3 head | 1000 add(0, x), then 1000 remove(0) |
| W3 middle | 1000 add(n / 2, x), then 1000 remove(n / 2) |
| W4 | n heap inserts, then n extractions; output is checked for non-decreasing order |

Setup and correctness checks are outside timing. W4 times both inserts and extractions.
The middle index stays fixed throughout W3. Each benchmark run overwrites the CSV.
Timing includes the cost of metrics and can vary with JVM compilation, GC and background tasks.

## Generate plots

Install matplotlib once if needed:

```powershell
python -m pip install matplotlib
```

Then run:

```powershell
python results/generate_plots.py
```

If Python is not on PATH, the interpreter used on this computer is:

```powershell
& 'C:\Users\bekza\AppData\Local\Python\pythoncore-3.14-64\python.exe' results/generate_plots.py
```

The script reads results/results.csv and creates ten PNG files in results/plots.
Plots use a logarithmic n axis. Time plots also use a logarithmic time axis;
operation plots use a linear operation axis so zero counts remain visible.

## Where to find things

- src/main/java/metrics/Metrics.java: steps, moves, comparisons and reset().
- src/main/java/structures: the three structure implementations.
- src/main/java/benchmark/Benchmark.java: one method per workload and CSV output.
- src/test/java: JUnit 5 tests.
- results/results.csv: measured times and operation counts.
- results/plots: PNG charts for all four workloads.
- results/generate_plots.py: the small plotting script.
- REPORT.md: complexity tables, invariant proofs, plots and discussion.
- target: generated classes and test reports.
- .tools: local Maven and cached dependencies, not assignment source code.

## Metrics and methods

steps counts a read of one internal array cell or following a list next link,
including a traversal to null. moves counts array shifts, resize copies and
heap element relocation, or a change to head, tail or a node next link.
comparisons counts value comparisons, including comparison with a contains query.
Index checks and loop bounds do not count as element comparisons.

Writing a new array value is not a move. Swapping two values is two moves.
Changing a local traversal variable is not a structural pointer update.
Reading a node value is not a step under the assignment definition.

W1-W3 reset counters after filling. W4 counts all inserts and extractions.
CSV counters come from the last measured run. All five runs use the same input,
so operation counts are identical even when elapsed times differ.

remove(index) returns the removed int. size() returns the current element count.
getMetrics() returns the structure's counters. hasHeapProperty() is a diagnostic
used by tests; its checks deliberately do not affect workload counters.
Insertion permits indexes 0..size; reading and removal permit 0..size-1.

## Git and scope

main contains the completed assignment. feature/array, feature/list, feature/heap
and feature/metrics point to the existing baseline commit with these implementations.
These branches do not pretend to reconstruct an earlier development history.
The v1.0 tag identifies the completed version. Nothing is pushed automatically.

No JOL, memory-footprint bonus or buildHeap has been added.
