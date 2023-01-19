package behavior.observer;

import java.util.Observable;
import java.util.Observer;

//This is first observer
class Observer1 implements Observer
{
 public void update(Observable obj, Object arg) 
 {
     System.out.println("Observer1 Notified with value : " + 
             ((Integer)arg).intValue());
 }
}

//This is second observer
class Observer2 implements Observer
{
 public void update(Observable obj, Object arg) 
 {
     System.out.println("Observer2 Notified with value : " + 
             ((Integer)arg).intValue());
 }
}

//This is class being observed
class BeingObserved extends Observable {
	void func1() {
		setChanged();
		/*This method notifies the change to all the 
        observers that are registered and passes an object*/
		notifyObservers(new Integer(10));
	}
}

public class JavaTest {
	// Driver method of the program
	public static void main(String args[]) {
		BeingObserved beingObserved = new BeingObserved();
		Observer1 observer1 = new Observer1();
		Observer2 observer2 = new Observer2();
		beingObserved.addObserver(observer1);
		beingObserved.addObserver(observer2);
		
		int count_observer = beingObserved.countObservers();
		System.out.println("Number of observers is " + count_observer);
		
		beingObserved.func1();

	}
}