package behavior.template_method;

/**
 * Template Method design pattern is used to create a method stub and deferring
 * some of the steps of implementation to the subclasses.
 * <P>
 * Template Method Design Pattern in JDK
 * <P>
 * All non-abstract methods of java.io.InputStream, java.io.OutputStream,
 * java.io.Reader and java.io.Writer. All non-abstract methods of
 * java.util.AbstractList, java.util.AbstractSet and java.util.AbstractMap.
 * 
 * @author TinhNX
 *
 */
public class HousingClient {
	public static void main(String[] args) {

		HouseTemplate houseType = new WoodenHouse();

		// using template method
		houseType.buildHouse();
		System.out.println("************");

		houseType = new GlassHouse();

		houseType.buildHouse();
	}
}
