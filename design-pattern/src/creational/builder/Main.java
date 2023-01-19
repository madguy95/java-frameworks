package creational.builder;

public class Main {

	public static void main(String[] args) {
		BuilderExample example = new BuilderExample.Builder()
				.setName("Nguyen Van A")
				.setId(1)
				.build();
		
		System.out.println("Object created by builder : " + example);
	}
}
