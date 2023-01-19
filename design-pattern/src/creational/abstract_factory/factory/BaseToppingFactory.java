package creational.abstract_factory.factory;

import creational.abstract_factory.model.cheese.Cheese;
import creational.abstract_factory.model.sauce.Sauce;

public abstract class BaseToppingFactory {
    public abstract Cheese createCheese();
    public abstract Sauce createSauce();
}
