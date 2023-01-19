package creational.singleton;

public class SingletonExample {

	private SingletonExample() {}
	
	/**
	 * volatile : for multi thread
	 */
	private static volatile SingletonExample instance;
	
	public static SingletonExample getInstance() {
		if(instance == null) {
			synchronized (instance) {
				if(instance == null) {
					instance = new SingletonExample();
				}
			}
		}
		
		return instance;
	}
	
}
