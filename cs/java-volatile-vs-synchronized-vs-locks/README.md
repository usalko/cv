# Java test
Volatile vs synchronized vs locks

Performance test on jvm 11:
x86 architecture: volatile vs synchronized vs Lock
Cores on CPU > 2
Run 2 threads: random read/write value.

Pseudorandom sequence based on random.org seed.

1) Read/Write Ratio: 0.25
2) Read/Write Ratio: 0.5
3) Read/Write Ratio: 0.75

1M operations
Both thread read and write values
Gathering statistics

RandomNumbers from https://random.org

## Files are:
|----------|---------|
|Name|Description|
|JavaVolatileVsSynchronizedVsLocks.java|Test|
|test|Batch file for bash|
|RandomNumbers|Bytesequence for seed from https://random.org|
