package creational.abstract_factory.factory;

import creational.abstract_factory.model.cheese.Cheese;
import creational.abstract_factory.model.cheese.GoatCheese;
import creational.abstract_factory.model.sauce.CaliforniaOilSauce;
import creational.abstract_factory.model.sauce.Sauce;

public class GourmetToppingFactory extends BaseToppingFactory{
    @Override
    public Cheese createCheese(){return new GoatCheese();}
    @Override
    public Sauce createSauce(){return new CaliforniaOilSauce();}
}