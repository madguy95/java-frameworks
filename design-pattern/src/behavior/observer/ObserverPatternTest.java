package behavior.observer;

import behavior.observer.model.MyTopic;
import behavior.observer.model.MyTopicSubscriber;
import behavior.observer.model.Observer;

/**
 * Observer design pattern is also called as publish-subscribe pattern. Some of it’s implementations are;
 *	java.util.EventListener in Swing
 *	javax.servlet.http.HttpSessionBindingListener
 *	javax.servlet.http.HttpSessionAttributeListener
 * @author TinhNX
 *
 */
public class ObserverPatternTest {
	public static void main(String[] args) {
		//create subject
		MyTopic topic = new MyTopic();
		
		//create observers
		Observer obj1 = new MyTopicSubscriber("Obj1");
		Observer obj2 = new MyTopicSubscriber("Obj2");
		Observer obj3 = new MyTopicSubscriber("Obj3");
		
		//register observers to the subject
		topic.register(obj1);
		topic.register(obj2);
		topic.register(obj3);
		
		//attach observer to subject
		obj1.setSubject(topic);
		obj2.setSubject(topic);
		obj3.setSubject(topic);
		
		//check if any update is available
		obj1.update();
		
		//now send message to subject
		topic.postMessage("New Message");
	}
}
