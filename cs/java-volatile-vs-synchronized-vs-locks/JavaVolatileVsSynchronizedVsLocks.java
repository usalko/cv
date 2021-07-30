import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class JavaVolatileVsSynchronizedVsLocks {

	interface Accessor {
		int readValue();
		void writeValue(int value);
	}
	
	static class VolatiledValue implements Accessor {

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
			synchronized(this) {
				return value;
			}
		}

		@Override
		public void writeValue(int value) {
			synchronized(this) {
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

	private static void testAccessor(Accessor accessor) {
		accessor.writeValue(1);
		System.out.printf("Value is %s\n", accessor.readValue());
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Simple check");
		testAccessor(new VolatiledValue());
		testAccessor(new SynchronizedValue());
		testAccessor(new LockedValue());

	}
}
