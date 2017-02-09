package com.dan.missileCommand.types;

public class Target {
	private static final int velocity = 15;
	private final String id;
	private final int heading;
	
	public Target(String id, int heading) {
		this.id = id;
		this.heading = heading;
	}

	public String getId() {
		return id;
	}
	
	public int getHeading() {
		return heading;
	}
	
	public int getVelocity() {
		return velocity;
	}

	@Override
	public String toString() {
		return "Target [id=" + id + ", heading=" + heading + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Target other = (Target) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
