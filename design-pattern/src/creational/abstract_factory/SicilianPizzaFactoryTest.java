package creational.abstract_factory;

import creational.abstract_factory.factory.BasePizzaFactory;
import creational.abstract_factory.factory.SicilianPizzaFactory;
import creational.abstract_factory.model.pizza.Pizza;

public class SicilianPizzaFactoryTest {

	public static void main(String[] args) {

		BasePizzaFactory pizzaFactory = new SicilianPizzaFactory();
		Pizza cheesePizza = pizzaFactory.createPizza("cheese");
		Pizza pepperoniPizza = pizzaFactory.createPizza("pepperoni");
	}
}
