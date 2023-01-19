package behavior.strategy;

/**
 * We could have used composition to create instance variable for strategies but
 * we should avoid that as we want the specific strategy to be applied for a
 * particular task. Same is followed in Collections.sort() and Arrays.sort()
 * method that take comparator as argument.
 * 
 * @author TinhNX
 *
 */
public class ShoppingCartTest {
	public static void main(String[] args) {
		ShoppingCart cart = new ShoppingCart();

		Item item1 = new Item("1234", 10);
		Item item2 = new Item("5678", 40);

		cart.addItem(item1);
		cart.addItem(item2);

		// pay by paypal
		cart.pay(new PaypalStrategy("myemail@example.com", "mypwd"));

		// pay by credit card
		cart.pay(new CreditCardStrategy("Pankaj Kumar", "1234567890123456", "786", "12/15"));
	}
}
