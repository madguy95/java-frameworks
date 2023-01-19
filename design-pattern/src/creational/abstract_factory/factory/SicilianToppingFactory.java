package creational.abstract_factory.factory;

import creational.abstract_factory.model.cheese.Cheese;
import creational.abstract_factory.model.cheese.MozzarellaCheese;
import creational.abstract_factory.model.sauce.Sauce;
import creational.abstract_factory.model.sauce.TomatoSauce;

public class SicilianToppingFactory extends BaseToppingFactory{
    @Override
    public  Cheese createCheese(){return new MozzarellaCheese();}
    @Override
    public  Sauce createSauce(){return new TomatoSauce();}
}