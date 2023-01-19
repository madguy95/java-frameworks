package creational.factory_method;

import creational.factory.model.Pizza;

/**
 * Creates objects without specifying the exact class to create.
 * @author TinhNX
 *
 */
public class PizzaFactoryTest {

	public static void main(String[] args) {
		BasePizzaFactory pizzaFactory = new PizzaFactory();
        Pizza cheesePizza = pizzaFactory.createPizza("cheese");
        Pizza veggiePizza = pizzaFactory.createPizza("veggie");
	}
}
