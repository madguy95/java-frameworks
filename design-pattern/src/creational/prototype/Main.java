package creational.prototype;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		PrototypeExample example1 = new PrototypeExample(1, "Nguyen Van A");
		
		PrototypeExample example2 = example1.clone();
		
		System.out.println("Compare address 2 object is: " + (example1 == example2 ? "true" : "false"));
		System.out.println("Compare value 2 object is: " + (example1.equals(example2) ? "true" : "false"));
		
		System.out.println("----- Warning: Always remember about shadow or deep copy with object");
		
		DeepCopyExample deepClone = new DeepCopyExample(2, "Nguyen Xuan A");
		DeepCopyExample deepClone2 = deepClone.clone();
		
		System.out.println("Object field in deep copy is other address refer ? : " +  (deepClone.getPrototype() == deepClone2.getPrototype() ? "true" : "false"));
		
		ShadowCopyExample shadowCopy = new ShadowCopyExample(2, "Nguyen Xuan A");
		ShadowCopyExample shadowCopy2 = shadowCopy.clone();
		
		System.out.println("Object field in shadow copy is other address refer ? : " +  (shadowCopy.getPrototype() == shadowCopy2.getPrototype() ? "true" : "false"));

	}
}
