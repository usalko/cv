class ThreadStatistic {

    final byte[] seed;

    int readsCount;
    int writesCount;

    long startNanoTime;
    long startReadsNanoTime;
    long startWritesNanoTime;

    long nanoTime;
    long readsNanoTime;
    long writesNanoTime;

    public ThreadStatistic(byte[] seed) {
        this.seed = seed;
    }

    public void start() {
        startNanoTime = System.nanoTime();
    }

    public void stop() {
        nanoTime += System.nanoTime() - startNanoTime;
    }

    public void startRead() {
        startReadsNanoTime = System.nanoTime();
    }

    public void stopRead() {
        readsNanoTime += System.nanoTime() - startReadsNanoTime;
    }

    public void startWrite() {
        startWritesNanoTime = System.nanoTime();
    }

    public void stopWrite() {
        writesNanoTime += System.nanoTime() - startWritesNanoTime;
    }

    public double getRatio() {
        double total = this.readsCount + this.writesCount;
        return total > 0 ? this.readsCount / total : 0;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s\n", readsCount, writesCount, this.getRatio(), nanoTime / 1_000_000,
                readsNanoTime / 1_000_000, writesNanoTime / 1_000_000);
    }

}