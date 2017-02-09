package com.dan.missileCommand.types;

public class HeadingVector {

	private final Location source;
	private final int heading;
	
	public HeadingVector(Location source, int heading) {
		super();
		this.source = source;
		this.heading = heading;
	}
	
	public Location getSource() {
		return source;
	}
	public int getHeading() {
		return heading;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + heading;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		HeadingVector other = (HeadingVector) obj;
		if (heading != other.heading)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HeadingVector [source=" + source + ", heading=" + heading + "]";
	}
	
	
	
}
