package creational.abstract_factory.factory;

import creational.abstract_factory.model.pizza.Pizza;

public abstract class BasePizzaFactory {
    
    public abstract Pizza createPizza(String type);
}
