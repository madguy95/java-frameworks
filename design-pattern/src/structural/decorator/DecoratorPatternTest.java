package structural.decorator;

/**
 * Decorator design pattern is helpful in providing runtime modification
 * abilities and hence more flexible. Its easy to maintain and extend when the
 * number of choices are more.
 * <p>
 * The disadvantage of decorator design pattern is that it uses a lot of similar
 * kind of objects (decorators).
 * <p>
 * Decorator pattern is used a lot in Java IO classes, such as FileReader,
 * BufferedReader etc.
 * 
 * @author TinhNX
 *
 */
public class DecoratorPatternTest {
	public static void main(String[] args) {
		Car sportsCar = new SportsCar(new BasicCar());
		sportsCar.assemble();
		System.out.println("\n*****");

		Car sportsLuxuryCar = new SportsCar(new LuxuryCar(new BasicCar()));
		sportsLuxuryCar.assemble();
		/**
		 * Notice that client program can create different kinds of Object at runtime
		 * and they can specify the order of execution too
		 */
	}
}
