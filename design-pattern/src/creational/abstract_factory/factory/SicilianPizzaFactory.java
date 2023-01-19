package creational.abstract_factory.factory;

import creational.abstract_factory.model.pizza.CheesePizza;
import creational.abstract_factory.model.pizza.PepperoniPizza;
import creational.abstract_factory.model.pizza.Pizza;
import creational.abstract_factory.model.pizza.VeggiePizza;

public class SicilianPizzaFactory extends BasePizzaFactory {
	@Override
	public Pizza createPizza(String type) {
		Pizza pizza;
		BaseToppingFactory toppingFactory = new SicilianToppingFactory();
		switch (type.toLowerCase()) {
		case "cheese":
			pizza = new CheesePizza(toppingFactory);
			break;
		case "pepperoni":
			pizza = new PepperoniPizza(toppingFactory);
			break;
		case "veggie":
			pizza = new VeggiePizza(toppingFactory);
			break;
		default:
			throw new IllegalArgumentException("No such pizza.");
		}
		pizza.addIngredients();
		pizza.bakePizza();
		return pizza;
	}
}
