import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

class JavaVolatileVsSynchronizedVsLocks {

	interface Accessor {
		int readValue();

		void writeValue(int value);
	}

	static class VolatileValue implements Accessor {

		volatile int value;

		@Override
		public int readValue() {
			return value;
		}

		@Override
		public void writeValue(int value) {
			this.value = value;
		}
	}

	static class SynchronizedValue implements Accessor {

		int value;

		@Override
		public int readValue() {
			synchronized (this) {
				return value;
			}
		}

		@Override
		public void writeValue(int value) {
			synchronized (this) {
				this.value = value;
			}
		}
	}

	static class LockedValue implements Accessor {

		int value;
		final Lock lock = new ReentrantLock();

		@Override
		public int readValue() {
			lock.lock();
			try {
				return value;
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void writeValue(int value) {
			lock.lock();
			try {
				this.value = value;
			} finally {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) throws Throwable {

		ArgsParser parseResults = new ArgsParser(args);
		final int threadsCount = parseResults.getIntOption("Threads count", "threads-count", 2);
		final int cyclesCount = parseResults.getIntOption("Cycles count", "cycles-count", 1_000_000);
		final Accessor accessor = parseResults.getTypeOption("Accessor type", "accessor-type", stringValue -> {
			switch (stringValue.replaceAll("-", "").toUpperCase()) {
				case "VOLATILEVALUE":
					return VolatileValue.class;
				case "SYNCHRONIZEDVALUE":
					return SynchronizedValue.class;
				case "LOCKEDVALUE":
					return LockedValue.class;
			}
			throw new IllegalStateException(String.format("The value %s can't parse", stringValue));
		});
		parseResults.print();

		Map<Integer, ThreadStatistic> statistic = new HashMap<>(threadsCount) {
			{
				IntStream.range(0, threadsCount).forEach(i -> {
					try {
						put(i, new ThreadStatistic(Files.readAllBytes(Path.of("RandomNumbers" + (i + 1)))));
					}
					catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
			}
		};
		IntStream.range(0, threadsCount).parallel().forEach((threadNumber) -> {
			ThreadStatistic threadStatistic = statistic.get(threadNumber);
			SecureRandom randomSequence = new SecureRandom(threadStatistic.seed);
			int value = 0;

			for (int i = 0; i < cyclesCount; i++) {
				boolean isWrite = randomSequence.nextBoolean();
				threadStatistic.start();
				if (isWrite) {
					threadStatistic.startWrite();
					accessor.writeValue(value);
					threadStatistic.stopWrite();
					threadStatistic.writesCount++;
				} else {
					threadStatistic.startRead();
					value = accessor.readValue() + 1;
					threadStatistic.stopRead();
					threadStatistic.readsCount++;
				}
				threadStatistic.stop();
			}
		});
		// Print results
		IntStream.range(0, threadsCount).forEach(i -> {
			System.out.println(statistic.get(i));
		});
	}
}
