package creational.prototype;

import java.util.Objects;

public class ShadowCopyExample implements Cloneable {

	private long id;
	
	private String name;
	
	private PrototypeExample prototype;
	
	public ShadowCopyExample() {
	}

	public ShadowCopyExample(long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.prototype = new PrototypeExample(id, name);
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

	public PrototypeExample getPrototype() {
		return prototype;
	}

	public void setPrototype(PrototypeExample prototype) {
		this.prototype = prototype;
	}

	@Override
	protected ShadowCopyExample clone() throws CloneNotSupportedException {
		return (ShadowCopyExample) super.clone();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShadowCopyExample) || obj == null) return false;
		ShadowCopyExample objUnbox = (ShadowCopyExample) obj;
		return this.id == objUnbox.getId() && this.name == objUnbox.getName() && Objects.equals(this.prototype, objUnbox.getPrototype());
	}
}
