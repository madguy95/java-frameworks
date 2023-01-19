package creational.factory_method;

import creational.factory.model.Pizza;

public abstract class BasePizzaFactory {
	public abstract Pizza createPizza(String type);
}
