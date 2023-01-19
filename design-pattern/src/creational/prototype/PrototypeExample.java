package creational.prototype;

public class PrototypeExample implements Cloneable {

	private long id;
	
	private String name;
	
	public PrototypeExample() {
	}

	public PrototypeExample(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected PrototypeExample clone() throws CloneNotSupportedException {
		return (PrototypeExample) super.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PrototypeExample) || obj == null) return false;
		PrototypeExample objUnbox = (PrototypeExample) obj;
		return this.id == objUnbox.getId() && this.name == objUnbox.getName();
	}
}
