package creational.abstract_factory;

import creational.abstract_factory.factory.BasePizzaFactory;
import creational.abstract_factory.factory.GourmetPizzaFactory;
import creational.abstract_factory.model.pizza.Pizza;

/**
 * Allows the creation of objects without specifying their concrete type.
 * @author TinhNX
 *
 */
public class GourmetPizzaFactoryTest {

	public static void main(String[] args) {
		BasePizzaFactory pizzaFactory = new GourmetPizzaFactory();
		Pizza cheesePizza = pizzaFactory.createPizza("cheese");
		Pizza veggiePizza = pizzaFactory.createPizza("veggie");
	}
}
