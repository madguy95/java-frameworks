package behavior.mediator;

/**
 *  Mediator design pattern is used to provide a centralized communication medium between different objects in a system.
 *  <P>
 *  Mediator Pattern Example in JDK
 *  java.util.Timer class scheduleXXX() methods
 *  Java Concurrency Executor execute() method.
 *  java.lang.reflect.Method invoke() method.
 * @author TinhNX
 *
 */
public class ChatClient {
	public static void main(String[] args) {
		ChatMediator mediator = new ChatMediatorImpl();
		User user1 = new UserImpl(mediator, "Pankaj");
		User user2 = new UserImpl(mediator, "Lisa");
		User user3 = new UserImpl(mediator, "Saurabh");
		User user4 = new UserImpl(mediator, "David");
		mediator.addUser(user1);
		mediator.addUser(user2);
		mediator.addUser(user3);
		mediator.addUser(user4);

		user1.send("Hi All");

	}
}
