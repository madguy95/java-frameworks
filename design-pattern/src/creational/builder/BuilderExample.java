package creational.builder;

public class BuilderExample {

	private String name;
	
	private long id;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id + " -- " + name;
	}
	
	public static class Builder {
		
		private BuilderExample builder;
		
		public Builder() {
			builder = new BuilderExample();
		}
		
		public Builder setName(String name) {
			builder.name = name;
			return this;
		}
		
		public Builder setId(long id) {
			builder.id = id;
			return this;
		}
		
		public BuilderExample build() {
			return builder;
		}
	}
}
