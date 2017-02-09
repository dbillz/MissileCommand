package com.dan.missileCommand.types;

public class Location {
	private final Integer xLoc;
	private final Integer yLoc;

	private Location() {
		xLoc = null;
		yLoc = null;
	}
	public Location(int xLoc, int yLoc) {
		super();
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	
	public Integer getxLoc() {
		return xLoc;
	}
	
	public Integer getyLoc() {
		return yLoc;
	}
	
	@Override
	public String toString() {
		return "Location [xLoc=" + xLoc + ", yLoc=" + yLoc + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((xLoc == null) ? 0 : xLoc.hashCode());
		result = prime * result + ((yLoc == null) ? 0 : yLoc.hashCode());
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
		Location other = (Location) obj;
		if (xLoc == null) {
			if (other.xLoc != null)
				return false;
		} else if (!xLoc.equals(other.xLoc))
			return false;
		if (yLoc == null) {
			if (other.yLoc != null)
				return false;
		} else if (!yLoc.equals(other.yLoc))
			return false;
		return true;
	}
	
	

}
